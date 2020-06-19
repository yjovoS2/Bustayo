package com.bhsd.bustayo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bhsd.bustayo.R;
import com.bhsd.bustayo.adapter.SearchRecyclerAdapter;
import com.bhsd.bustayo.dto.SearchRecyclerItem;

import java.util.ArrayList;

public class SearchBusFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<SearchRecyclerItem> data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_search_bus, container, false);

        //검색 전 : 히스토리
        //검색 중 : 검색
        //리사이클러뷰에 표시할 데이터 리스트 생성.
        data = new ArrayList<>();
        for (int i=0; i<8; i++)
            data.add(new SearchRecyclerItem(Integer.toString(i), "서울", "일반버스", false));

        ////////////////////////////
        //출력 방식 설정(리니어 레이아웃)
        //어댑터 생성 후 연결
        recyclerView = view.findViewById(R.id.searchBusRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new SearchRecyclerAdapter(data));

        return view;
    }
}
