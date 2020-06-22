package com.bhsd.bustayo.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bhsd.bustayo.R;
import com.bhsd.bustayo.activity.StationActivity;
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

///////////////////////////////////////////////////////////////////////////
// 바텀네비게이션 메뉴 -> 주변 정류장
//   - 현재 내 위치 확인할 수 있음
//   - 지도 상단에 있는 "주변 정류장 확인하기" 클릭하면 주변에 있는 정류장 위치를 보여줌
//   - 네이버맵 사용
///////////////////////////////////////////////////////////////////////////
public class NMapFragment extends Fragment implements OnMapReadyCallback {

    //사용자 위치 접근 관련 변수
    private static final int       LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationSource    locationSource;

    //네이버맵 관련 변수
    private MapView                mapView;
    private NaverMap               naverMap;
    private LocationButtonView     locationButtonView;
    private ArrayList<StationItem> stationItems;
    private Button                 aroundStation;

    //XML 처리 관련
    private URL                    url;
    private boolean                b_arsId, b_gpsX, b_gpsY, b_stationId, b_stationNm;
    private String                 s_arsId, s_gpsX, s_gpsY, s_stationId, s_stationNm;
    private XmlPullParserFactory   parserCreator;
    private XmlPullParser          parser;

    //UI 스레드
    private Handler                handler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        //필요한 뷰 인플레이션
        View view          = inflater.inflate(R.layout.fragment_nmap, container, false);
        locationButtonView = view.findViewById(R.id.nMapLocation);
        aroundStation      = view.findViewById(R.id.aroundStation);
        mapView            = view.findViewById(R.id.nMap);

        //필요한 객체 생성
        locationSource     = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE); //사용자 현재위치 접근 권한 요청
        stationItems       = new ArrayList<>();                                               //API 응답 데이터 저장
        handler            = new Handler(Looper.getMainLooper());                             //UI 스레드 핸들

        //네이버맵 비동기 호출
        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(@NonNull final NaverMap naverMap) {
        this.naverMap = naverMap;

        setMapUI();           //네이버맵 UI 설정
        setZoom(16, 19);      //줌 최소, 최대 레벨 설정
        setCurrentLocation(); //사용자 현재위치 설정

        ///////////////////////////////////////////////////////
        // 사용자가 맵에서 이동 시
        //   - 맵 이동하기 전에는 "주변 정류장 확인하기" 버튼이 보임
        //   - 맵 이동 중에는 "주변 정류장 확인하기" 버튼이 안보이도록 설정
        ///////////////////////////////////////////////////////
        naverMap.addOnCameraChangeListener(new NaverMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(int i, boolean b) {
                if(b)
                    aroundStation.setVisibility(View.VISIBLE);
                else
                    aroundStation.setVisibility(View.GONE);
            }
        });

        /////////////////////////////////////////////////////////////////////
        // "주변 정류장 확인하기" 버튼 클릭 시
        //   - 기존 맵에 설정된 마커 초기화
        //   - 기존 API 호출을 통해 받아온 데이터 초기화
        //   - API 호출은 네트워크 통신이 일어나므로 워커 스레드에 할당하여 데이터를 받아옴
        /////////////////////////////////////////////////////////////////////
        aroundStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeItem();

                new StationXmlparse().execute();
            }
        });
    }

    ///////////////////////////////////////////////////
    // 네이버맵 UI 요소들 설정
    //   - 사용자 현재 위치 버튼은 커스텀하여 사용하므로 비활성화
    //   - zoom 레벨에 대한 거리 비율 비활성화
    //   - zoom 레벨 설정 버튼 비활성화
    ///////////////////////////////////////////////////
    private void setMapUI(){
        UiSettings ui = naverMap.getUiSettings();

        ui.setLocationButtonEnabled(false);
        ui.setScaleBarEnabled(false);
        ui.setZoomControlEnabled(false);
    }

    ///////////////////////////////////////////////////
    // 네이버맵 zoom 설정
    //   - 줌 최소 레벨 설정
    //   - 줌 최대 레벨 설정
    //   - 줌 레벨별 거리 ( 16:200, 17:100, 18:50, 19:25 )
    ///////////////////////////////////////////////////
    private void setZoom(int min, int max){
        naverMap.setMinZoom(min);
        naverMap.setMaxZoom(max);
    }

    ////////////////////////////////////
    // 사용자 현재 위치 버튼 생성
    //   - 커스텀된 사용자 현재 위치 버튼 설정
    //   - 버튼 클릭 시 사용자 현재 위치로 이동
    ////////////////////////////////////
    private void setCurrentLocation(){
        locationButtonView.setMap(naverMap);

        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
    }

    //////////////////////////////////////
    // 지도상에 마커 표시
    //   - 마커에 대한 스타일 설정
    //   - 마커를 맵 상에 등록
    //   - 마커 클릭 시 해당 정류장 화면으로 이동
    //////////////////////////////////////
    private void setMarker(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                for(final StationItem item : stationItems){
                    item.marker.setIcon(MarkerIcons.BLACK);
                    item.marker.setIconTintColor(getContext().getColor(R.color.colorAccent));
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

    //////////////////////////////////////////
    // 맵에 표시된 데이터 초기화
    //   - 기존 맵에 설정된 마커 초기화
    //   - 기존 API 호출을 통해 받아온 데이터 초기화
    //////////////////////////////////////////
    private void initializeItem(){
        for(StationItem item : stationItems)
            item.marker.setMap(null);

        stationItems.clear();
    }

    //////////////////////////////
    // 사용자 현재 위치 접근 권한 확인
    //////////////////////////////
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,  @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated())
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);

            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /////////////////////////////////////////////
    // API를 통해 받아온 데이터를 저장하는 아이템 클래스
    /////////////////////////////////////////////
    private class StationItem {
        Marker marker;
        String arsId, stationNm;

        StationItem(Marker marker, String arsId, String stationNm){
            this.marker    = marker;
            this.arsId     = arsId;
            this.stationNm = stationNm;
        }
    }

    //////////////////////////////////////
    // 네트워크 통신을 하는 워커 스레드 클래스
    //   - API 호출을 통해 받아온 데이터를 저장
    //   - 저장된 데이터를 이용하여 마커 설정
    //////////////////////////////////////
    private class StationXmlparse extends AsyncTask {
        //API 요청 변수
        double lat, lng;
        int    radius;

        @Override
        protected Object doInBackground(Object[] objects) {
            lat = naverMap.getCameraPosition().target.latitude;  //맵의 현재 위도 저장
            lng = naverMap.getCameraPosition().target.longitude; //맵의 현재 경도 저장

            //줌 레벨별 반경(m) 설정
            switch((int)naverMap.getCameraPosition().zoom){
                case 16: radius = 200; break;
                case 17: radius = 100; break;
                case 18: radius = 50;  break;
                case 19: radius = 25;  break;
            }

            try {
                //API 호출 URL
                url = new URL("http://ws.bus.go.kr/api/rest/stationinfo/getStationByPos?tmX=" + lng + "&tmY=" + lat + "&radius=" + radius + "&serviceKey=" + Common.SERVICE_KEY);

                //XML 파싱 변수
                parserCreator = XmlPullParserFactory.newInstance();
                parser        = parserCreator.newPullParser();
                parser.setInput(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));

                //XML 파싱 시작
                while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                    switch (parser.getEventType()) {
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
                            } break;
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
                            } break;
                        //종료 태그
                        case XmlPullParser.END_TAG:
                            if(parser.getName().equals("itemList")){
                                //파싱된 데이터 저장
                                Marker marker = new Marker(new LatLng(Double.parseDouble(s_gpsY), Double.parseDouble(s_gpsX)));
                                stationItems.add(new StationItem(marker, s_arsId, s_stationNm));
                            } break;
                    } parser.next();
                }
            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            setMarker(); //마커 재설정

            return null;
        }
    }
}
