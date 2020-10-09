package com.bhsd.bustayo.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.location.LocationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bhsd.bustayo.MainActivity;
import com.bhsd.bustayo.R;
import com.bhsd.bustayo.application.GpsTracker;
import com.bhsd.bustayo.application.SetAlarmDialog;
import com.bhsd.bustayo.adapter.StationListAdapter;
import com.bhsd.bustayo.application.APIManager;
import com.bhsd.bustayo.dto.BusPositions;
import com.bhsd.bustayo.dto.StationListItem;
import com.google.gson.internal.$Gson$Preconditions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class GetOffAlarmFragment extends Fragment {

    private MainActivity activity;
    private ArrayList<StationListItem> stationListItems;
    private AutoCompleteTextView busAuto;
    private ArrayList<String> stationitem;
    private ArrayList<BusPositions> busPos;
    private ImageView mapping;
    private RecyclerView busStation;
    private StationListAdapter slAdapter = new StationListAdapter();
    private StationListAdapter alarmAdapter;

    private ArrayList<HashMap<String, String>> busInfo;
    private ArrayList<HashMap<String, String>> bus;
    private ArrayList<HashMap<String, String>> stationInfo;

    private String busRouteId;
    private int routeType;

    private String myBus;

    private GpsTracker gpsTracker;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSION_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSION = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);

        activity = (MainActivity) getActivity();
        stationListItems = new ArrayList<StationListItem>();
        busAuto = view.findViewById(R.id.search_bus_auto);
        mapping = view.findViewById(R.id.search_bus_button);
        busStation = view.findViewById(R.id.station_list);
        alarmAdapter = new StationListAdapter();
        busPos = new ArrayList<>();

        final RecyclerView slRecyclerView = view.findViewById(R.id.station_list);

        slRecyclerView.setHasFixedSize(true);
        slRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));


        new Thread() {
            @Override
            public void run() {
                busInfo = APIManager.getAPIArray(APIManager.GET_BUS_ROUTE_LIST, new String[]{""}, new String[]{"busRouteId", "routeType", "busRouteNm", "edStationNm"});

                stationitem = new ArrayList<>();
                for (HashMap<String, String> item : busInfo) {
                    stationitem.add(item.get("busRouteNm") + "  " + item.get("edStationNm") + "방면");
                }

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter adapter = new ArrayAdapter(activity, android.R.layout.simple_dropdown_item_1line, stationitem);
                        busAuto.setAdapter(adapter);
                    }
                });
            }
        }.start();

        mapping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                busPos.clear();

                if (!checkLocationServicesStatus()) {
                    showDialogForLocationServiceSetting();
                } else {
                    checkRunTimePermission();
                }

                gpsTracker = new GpsTracker(getActivity());
//                          위도
                final double latitude = Double.parseDouble(String.format("%.6f", gpsTracker.getLatitude()));
//                            경도
                final double longitude = Double.parseDouble(String.format("%.6f", gpsTracker.getLongitude()));

                new Thread() {
                    @Override
                    public void run() {
                        HashMap<String, String> busmap = APIManager.getAPIMap(APIManager.GET_BUS_ROUTE_LIST, new String[]{busAuto.getText().toString().split(" ")[0]}, new String[]{"busRouteId", "routeType"});
                        if (busmap.isEmpty())
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "버스 번호를 확인해주세요!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        else {
                            busRouteId = busmap.get("busRouteId");
                            routeType = Integer.parseInt(busmap.get("routeType"));
                            bus = APIManager.getAPIArray(APIManager.GET_BUSPOS_BY_RT_ID, new String[]{busRouteId}, new String[]{"lastStnId", "congetion", "plainNo", "gpsX", "gpsY", "sectionId"});
                            stationInfo = APIManager.getAPIArray(APIManager.GET_STATION_BY_ROUTE, new String[]{busRouteId}, new String[]{"station", "arsId", "stationNm", "section"});
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    slAdapter = new StationListAdapter();
                                    StationListItem stationListItem;
                                    for (HashMap<String, String> map : stationInfo) {
                                        stationListItem = new StationListItem(map.get("stationNm"), "station", "arsId", busRouteId, routeType, 4, 5, bus);
                                        stationListItem.setSectionId(map.get("section"));
                                        slAdapter.addItem(stationListItem);
                                    }
                                    slAdapter.notifyDataSetChanged();
                                }
                            });

                            //현재 위치를 받아서 가장 가까운 버스를 매칭칭
                            for (HashMap<String, String> item : bus) {
                                String plainNo = item.get("plainNo");
                                String posX = item.get("gpsX");
                                String posY = item.get("gpsY");
                                String sectionId = item.get("sectionId");
                                busPos.add(new BusPositions(plainNo, posX, posY, sectionId));
                            }

                            //가장 가까운 버스와 매칭

                            myBus = "";

                            for (final BusPositions item : busPos) {
                                double currentBusPosX = item.getPosX();
                                double currentBusPosY = item.getPosy();

                                if ((longitude - 0.001 <= currentBusPosX && longitude + 0.001 >= currentBusPosX) && (currentBusPosY >= latitude - 0.001 && latitude + 0.001 >= currentBusPosY)) {
                                    myBus = item.getPlainNo();
                                    final String mySectionId = item.getSectionId();
                                    alarmAdapter.clearList();

                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Iterator<StationListItem> iter = slAdapter.getList().iterator();

                                            while(iter.hasNext()){
                                                StationListItem stationItem = iter.next();
                                                if (stationItem.getSectionId().equals(mySectionId)) {
                                                    alarmAdapter.addItem(stationItem);
                                                    while (iter.hasNext()){
                                                        stationItem = iter.next();
                                                        alarmAdapter.addItem(stationItem);
                                                        alarmAdapter.notifyDataSetChanged();
                                                    }
                                                }
                                            }
                                            slRecyclerView.setAdapter(alarmAdapter);
                                        }
                                    });

                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getContext(), myBus + "와 매칭이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                            busStation.setVisibility(View.VISIBLE);
                                        }
                                    });
                                    break;
                                }
                            }

                            if (myBus.equals(""))
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), "매칭에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                        if(busStation.getVisibility()==View.VISIBLE)
                                            busStation.setVisibility(View.INVISIBLE);
                                    }
                                });
                        }
                    }
                }.start();
                //버스 번호에 해당하는 값이 있는 경우 리스트를 출력하고 아니면 출력하지 않고 메시지를 띄운다
            }
        });

        alarmAdapter.setOnListItemSelected(new StationListAdapter.OnListItemSelected() {
            @Override
            public void onItemSelected(View v, int position) {
                //몇 정류장전에서 알람을 받을지 설정
                SetAlarmDialog alarmsetting = new SetAlarmDialog();
                alarmsetting.Dialog(activity);
            }
        });
        return view;
    }

    //        ActivityCompat.requestPermission를 사용한 퍼미션 요청의 결과를 리턴받는 메소드
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length == REQUIRED_PERMISSION.length) {
            boolean check_result = true;

            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if (check_result) {
                //위치 값을 가지고올 수 있음
                ;
            } else {
//                거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSION[0]) || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSION[1])) {
                    Toast.makeText(getContext(), "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "퍼미션이 거부되었습니다. 설정에서 퍼미션을 허용해야 합니다.", Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    void checkRunTimePermission() {
//        런타임 퍼미션 처리
//        1.위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
//            2. 이미 퍼미션을 가지고 있다면 위치값을 가지고올 수 있음
        } else {
//            퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요하다.
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSION[0])) {
//                사용자가 퍼미션 거부를 한 적이 있는 경우 요청을 진행하기 전에 사용자에게 퍼미션이 필요한 이유를 설명
                Toast.makeText(getActivity(), "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSION, PERMISSION_REQUEST_CODE);
            } else
//                사용자가 퍼미션 거부를 한적이 없는 경우 퍼미션 요청
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSION, PERMISSION_REQUEST_CODE);
        }
    }

    //    GPS활성화를 위한 메소드
    private void showDialogForLocationServiceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n" + "위치 설정을 수정하시겠습니까?");
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent callGPSSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case GPS_ENABLE_REQUEST_CODE:
//                사용자가 GPS를 활성화 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d("@@@", "GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }
                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}
