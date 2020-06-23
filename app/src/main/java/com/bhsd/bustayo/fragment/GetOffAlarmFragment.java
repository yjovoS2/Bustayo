package com.bhsd.bustayo.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bhsd.bustayo.R;
import com.bhsd.bustayo.activity.MainActivity;
import com.bhsd.bustayo.application.SetAlarmDialog;
import com.bhsd.bustayo.adapter.StationListAdapter;
import com.bhsd.bustayo.application.APIManager;
import com.bhsd.bustayo.dto.StationListItem;

import java.util.ArrayList;
import java.util.HashMap;


public class GetOffAlarmFragment extends Fragment {

    private MainActivity activity;
    private ArrayList<StationListItem> stationListItems;
    private AutoCompleteTextView busAuto;
    private ArrayList<String> stationitem;
    private ImageView mapping;
    private RecyclerView busStation;
    private StationListAdapter slAdapter = new StationListAdapter();

    private ArrayList<HashMap<String, String>> busInfo;
    private ArrayList<HashMap<String, String>> bus;
    private ArrayList<HashMap<String, String>> stationInfo;

    private String busRouteId;
    private int routeType;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);

        activity = (MainActivity)getActivity();
        stationListItems = new ArrayList<StationListItem>();
        busAuto = view.findViewById(R.id.search_bus_auto);
        mapping = view.findViewById(R.id.search_bus_button);
        busStation = view.findViewById(R.id.station_list);

        final RecyclerView slRecyclerView = view.findViewById(R.id.station_list);

        slRecyclerView.setHasFixedSize(true);
        slRecyclerView.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));


        new Thread() {
            @Override
            public void run() {
                busInfo = APIManager.getAPIArray(APIManager.GET_BUS_ROUTE_LIST, new String[]{""}, new String[]{"busRouteId","routeType","busRouteNm","edStationNm"});

                stationitem = new ArrayList<>();
                for(HashMap<String,String> item : busInfo) {
                    stationitem.add(item.get("busRouteNm") + "  " + item.get("edStationNm") + "방면");
                }

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter adapter = new ArrayAdapter(activity, android.R.layout.simple_dropdown_item_1line,stationitem);
                        busAuto.setAdapter(adapter);
                    }
                });
            }
        }.start();

        mapping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //버스 번호에 해당하는 값이 있는 경우 리스트를 출력하고 아니면 출력하지 않고 메시지를 띄운다
                busStation.setVisibility(View.VISIBLE);

                new Thread() {
                    @Override
                    public void run() {
                        HashMap<String, String> busmap = APIManager.getAPIMap(APIManager.GET_BUS_ROUTE_LIST, new String[]{busAuto.getText().toString().split(" ")[0]}, new String[]{"busRouteId","routeType"});
                        busRouteId = busmap.get("busRouteId");
                        routeType = Integer.parseInt(busmap.get("routeType"));

                        bus = APIManager.getAPIArray(APIManager.GET_BUSPOS_BY_RT_ID, new String[]{busRouteId}, new String[]{"lastStnId", "congetion"});
                        stationInfo = APIManager.getAPIArray(APIManager.GET_STATION_BY_ROUTE, new String[]{busRouteId}, new String[]{"station", "arsId", "stationNm"});
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                slAdapter = new StationListAdapter();
                                for(HashMap<String,String> map : stationInfo) {
                                    slAdapter.addItem(new StationListItem(map.get("stationNm"), "station", "arsId", busRouteId, routeType, 4, 5, bus));
                                }
                                slRecyclerView.setAdapter(slAdapter);
                                slAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }.start();
            }
        });

        slAdapter.setOnListItemSelected(new StationListAdapter.OnListItemSelected() {
            @Override
            public void onItemSelected(View v, int position) {

                //몇 정류장전에서 알람을 받을지 설정
                SetAlarmDialog alarmsetting = new SetAlarmDialog();
                alarmsetting.Dialog(activity);
            }
        });
        return view;
    }
}
