package com.bhsd.bustayo;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bhsd.bustayo.application.Common;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.util.MarkerIcons;
import com.naver.maps.map.widget.LocationButtonView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class NMapFragment extends Fragment implements OnMapReadyCallback {

    //사용자 위치 접근 관련
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationSource locationSource;
    private LatLng oldLatLng;

    //네이버맵 관련
    private MapView mapView;
    private NaverMap naverMap;
    private LocationButtonView locationButtonView;
    private ArrayList<StationItem> stationItems;

    //XML 처리 관련
    private URL url;
    private boolean b_arsId, b_gpsX, b_gpsY, b_stationId, b_stationNm;
    private String s_arsId, s_gpsX, s_gpsY, s_stationId, s_stationNm;
    private XmlPullParserFactory parserCreator;
    private XmlPullParser parser;

    //UI 스레드
    private Handler handler;

    /////
    //기존에 있던 마커 삭제하기

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_nmap, container, false);

        //사용자 현재위치 접근 권한 요청
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        //네이버맵 UI 요소
        stationItems = new ArrayList<>();
        locationButtonView = view.findViewById(R.id.nMapLocation);

        //네이버맵 콜백메서드 호출
        mapView = view.findViewById(R.id.nMap);
        mapView.getMapAsync(this);

        //UI 스레드 생성
        handler = new Handler(Looper.getMainLooper());

        return view;
    }

    @Override
    public void onMapReady(@NonNull final NaverMap naverMap) {
        this.naverMap = naverMap;
        oldLatLng = new LatLng(0, 0);

        setMapUI();           //네이버맵 UI 설정
        setZoom(16, 19);      //줌 최소, 최대 레벨 설정
        setCurrentLocation(); //사용자 현재위치 설정
        //naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, true); 지도상에 대중교통 표시 오차 문제 있음

        //정류장 API 호출
        naverMap.addOnLocationChangeListener(new NaverMap.OnLocationChangeListener() {
            @Override
            public void onLocationChange(@NonNull Location location) {
                if(!oldLatLng.equals(naverMap.getCameraPosition().target)){
                    oldLatLng = naverMap.getCameraPosition().target;
                    new stationXmlpares().execute(); //API 호출은 워커 스레드에게 할당
                }
            }
        });
    }

    private void setCurrentLocation(){
        ////////////////////////////////
        //사용자 현재위치 버튼 설정
        //버튼 클릭 시 사용자 현재 위치로 이동
        locationButtonView.setMap(naverMap);
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
    }

    private void setMapUI(){
        //네이버맵 UI 설정
        UiSettings ui = naverMap.getUiSettings();
        ui.setLocationButtonEnabled(false);
        ui.setCompassEnabled(false);
        ui.setScaleBarEnabled(false);
        ui.setZoomControlEnabled(true); //임시로 설정한거
    }

    private void setZoom(int min, int max){
        //줌 레벨별 거리 레벨:거리 (15:400, 16:200, 17:100, 18:50, 19:25)
        naverMap.setMinZoom(min);
        naverMap.setMaxZoom(max);
    }

    private void setMarker(){
        //////////////////////////////
        //지도상에 마커 표시
        //마커 클릭 시 정류장 화면으로 이동
        handler.post(new Runnable() {
            @Override
            public void run() {
                for(final StationItem item : stationItems){
                    //item.marker.setIcon(OverlayImage.fromResource(R.drawable.marker_icon)); 아이콘 변경
                    item.marker.setIcon(MarkerIcons.BLACK);
                    item.marker.setIconTintColor(getContext().getColor(R.color.colorAccent));
                    item.marker.setPosition(item.latlng);
                    item.marker.setMap(naverMap);
                    item.marker.setOnClickListener(new Overlay.OnClickListener() {
                        @Override
                        public boolean onClick(@NonNull Overlay overlay) {
                            Intent intent = new Intent(getContext(), StationActivity.class);
                            intent.putExtra("arsId", item.arsId);
                            intent.putExtra("stationNm", item.stationNm);
                            startActivity(intent);

                            return true;
                        }
                    });
                }
            }
        });
    }

    private class StationItem {
        //정류장 정보 클래스
        Marker marker;
        LatLng latlng;
        String arsId, stationNm;

        StationItem(Marker marker, LatLng latlng, String arsId, String stationNm){
            this.marker = marker;
            this.latlng = latlng;
            this.arsId = arsId;
            this.stationNm = stationNm;
        }
    }

    private class stationXmlpares extends AsyncTask {
        //////////////////////////////////////////////////////
        //네트워크 통신을 해야하므로 파싱 작업은 Worker 스레드에게 할당
        //API 요청 변수
        double lat, lng;
        int radius;

        @Override
        protected Object doInBackground(Object[] objects) {
            lat = naverMap.getCameraPosition().target.latitude;
            lng = naverMap.getCameraPosition().target.longitude;

            //줌 레벨별 반경(m) 설정
            switch((int)naverMap.getCameraPosition().zoom){
                case 16: radius = 200; break;
                case 17: radius = 100; break;
                case 18: radius = 50; break;
                case 19: radius = 25; break;
            }

            try {
                //API 호출 URL
                url = new URL("http://ws.bus.go.kr/api/rest/stationinfo/getStationByPos?tmX=" + lng + "&tmY=" + lat + "&radius=" + radius + "&serviceKey=" + Common.SERVICE_KEY);

                //XML 파싱 변수
                parserCreator = XmlPullParserFactory.newInstance();
                parser = parserCreator.newPullParser();
                parser.setInput(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
                int parserEvent = parser.getEventType();

                //XML 파싱
                while (parserEvent != XmlPullParser.END_DOCUMENT) {
                    switch (parserEvent) {
                        //시작 태그
                        case XmlPullParser.START_TAG:
                            if (parser.getName().equals("arsId")) {
                                b_arsId = true;
                            }
                            if (parser.getName().equals("gpsX")) {
                                b_gpsX = true;
                            }
                            if (parser.getName().equals("gpsY")) {
                                b_gpsY = true;
                            }
                            if (parser.getName().equals("stationId")) {
                                b_stationId = true;
                            }
                            if (parser.getName().equals("stationNm")) {
                                b_stationNm = true;
                            }
                            break;
                        //내용
                        case XmlPullParser.TEXT:
                            if (b_arsId) {
                                s_arsId = parser.getText();
                                b_arsId = false;
                            }
                            if (b_gpsX) {
                                s_gpsX = parser.getText();
                                b_gpsX = false;
                            }
                            if (b_gpsY) {
                                s_gpsY = parser.getText();
                                b_gpsY = false;
                            }
                            if (b_stationId) {
                                s_stationId = parser.getText();
                                b_stationId = false;
                            }
                            if (b_stationNm) {
                                s_stationNm = parser.getText();
                                b_stationNm = false;
                            }
                            break;
                        //종료 태그
                        case XmlPullParser.END_TAG:
                            if(parser.getName().equals("itemList")){
                                //정류장 정보 저장
                                Marker marker = new Marker();
                                LatLng latlng = new LatLng(Double.parseDouble(s_gpsY), Double.parseDouble(s_gpsX));
                                stationItems.add(new StationItem(marker, latlng, s_arsId, s_stationNm));
                            }
                            break;
                    }
                    parserEvent = parser.next();
                }
                setMarker(); //마커 설정

            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,  @NonNull int[] grantResults) {
        //권한 요청 확인
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) //권한 거부
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
