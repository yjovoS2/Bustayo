package com.bhsd.bustayo;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//검색 화면(버스)
public class SearchBusFragment extends Fragment {

    RecyclerView recyclerView;
    LinearLayoutManager a;
    SearchRecyclerAdapter adapter;
    ArrayList<SearchRecyclerItem> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_search_bus, container, false);

        // 리사이클러뷰에 표시할 데이터 리스트 생성.
        list = new ArrayList<SearchRecyclerItem>();
        for (int i=0; i<8; i++)
            list.add(new SearchRecyclerItem(Integer.toString(i), "서울", "일반버스", false));

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        recyclerView = view.findViewById(R.id.searchBusRecycler);

        a = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(a);


        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        adapter = new SearchRecyclerAdapter(list) ;
        recyclerView.setAdapter(adapter) ;

        DividerItemDecoration test = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        test.getDrawable().setTint(Color.parseColor("#777777"));
        recyclerView.addItemDecoration(test);


        return view;
    }
}
