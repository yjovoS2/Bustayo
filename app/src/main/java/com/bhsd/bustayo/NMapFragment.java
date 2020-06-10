package com.bhsd.bustayo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;

public class NMapFragment extends Fragment implements OnMapReadyCallback {

    MapFragment mapFragment;

    MapView mapView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_nmap, container, false); //attachToRoot 이게 뭐지 (질문)

        mapView = (MapView)rootView.findViewById(R.id.nmap);
        mapView.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        Marker marker = new Marker();
        marker.setPosition(new LatLng(37.487936, 126.825071));
        marker.setMap(naverMap);
        LatLng location = new LatLng(37.487936, 126.825071);
        CameraPosition cameraPosition = new CameraPosition(location, 17);
        naverMap.setCameraPosition(cameraPosition);
    }
}
