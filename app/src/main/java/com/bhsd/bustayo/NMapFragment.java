package com.bhsd.bustayo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.naver.maps.map.MapFragment;

public class NMapFragment extends Fragment{


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState); //이거 왜 하는거지 (질문)

        return inflater.inflate(R.layout.fragment_nmap, container, false); //attachToRoot 이게 뭐지 (질문)
    }
}