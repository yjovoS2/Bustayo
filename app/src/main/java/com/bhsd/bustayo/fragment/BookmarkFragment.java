package com.bhsd.bustayo.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bhsd.bustayo.dto.BookmarkInfo;
import com.bhsd.bustayo.adapter.BookmarkRecyclerViewAdapter;
import com.bhsd.bustayo.dto.CurrentBusInfo;
import com.bhsd.bustayo.activity.MainActivity;
import com.bhsd.bustayo.R;
import com.bhsd.bustayo.activity.StationActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class BookmarkFragment extends Fragment {

    MainActivity activity;
    ArrayList<BookmarkInfo> bookmarkInfos;
    FloatingActionButton refresh_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);

        activity = (MainActivity)getActivity();
        bookmarkInfos = new ArrayList<BookmarkInfo>();
        activity.fab = refresh_btn = view.findViewById(R.id.refresh_btn);


        insertData();       //하드코딩한 부분 - 메소드는 그대로 쓰면서 내용만 바꿀예정
        final RecyclerView bmRecyclerView = view.findViewById(R.id.bookmark);

        final BookmarkRecyclerViewAdapter bmAdapter = new BookmarkRecyclerViewAdapter(bookmarkInfos,activity.context_main);
        bmRecyclerView.setHasFixedSize(true);
        bmRecyclerView.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));
        bmRecyclerView.setAdapter(bmAdapter);

        //recyclerView 클릭이벤트
        bmAdapter.setOnListItemSelected(new BookmarkRecyclerViewAdapter.OnListItemSelected() {
            @Override
            public void onItemSelected(View v, int position, RecyclerView selected) {
                Log.e("ljh", "onItemSelected");
                Intent intent = new Intent(activity, StationActivity.class);
//                값을 넘겨주는 부분
//                intent.putExtra("stationNm", stationName.getText().toString());
//                intent.putExtra("arsId", stationId);
                activity.startActivity(intent);
            }
        });

        //floatingActionButton 클릭이벤트
        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bmAdapter.notifyDataSetChanged();
                bmRecyclerView.setAdapter(bmAdapter);
            }
        });

        return view;
    }

    //test용
    public void insertData(){
        ArrayList<CurrentBusInfo> currentBusInfos = new ArrayList<>();

        currentBusInfos.add(new CurrentBusInfo(Color.BLUE,0,"601","우리집","곧 도착", "이따 도착",true));
        currentBusInfos.add(new CurrentBusInfo(Color.GREEN,10,"6716","등촌역", "곧도착","막차",true));

        bookmarkInfos.add(new BookmarkInfo("등촌역", currentBusInfos));

        ArrayList<CurrentBusInfo> currentBusInfos1 = new ArrayList<>();

        currentBusInfos1.add(new CurrentBusInfo(Color.GREEN,20,"6715","등촌역", "곧도착","막차", true));

        bookmarkInfos.add(new BookmarkInfo("목동역", currentBusInfos1));

        ArrayList<CurrentBusInfo> currentBusInfos2 = new ArrayList<>();

        currentBusInfos2.add(new CurrentBusInfo(Color.GREEN,20,"7734","옥수역", "곧도착","막차", true));

        bookmarkInfos.add(new BookmarkInfo("옥수역", currentBusInfos2));
    }
}
