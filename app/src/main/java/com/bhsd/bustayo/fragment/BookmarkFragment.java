package com.bhsd.bustayo.fragment;

import android.content.Intent;
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

import com.bhsd.bustayo.MainActivity;
import com.bhsd.bustayo.R;
import com.bhsd.bustayo.activity.StationActivity;
import com.bhsd.bustayo.adapter.BookmarkRecyclerViewAdapter;
import com.bhsd.bustayo.dto.BookmarkInfo;
import com.bhsd.bustayo.dto.CurrentBusInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class BookmarkFragment extends Fragment {

    MainActivity activity;
    ArrayList<BookmarkInfo> bookmarkInfos;
    FloatingActionButton refresh_btn;
    public static BookmarkRecyclerViewAdapter bmAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);

        activity = (MainActivity)getActivity();
        bookmarkInfos = new ArrayList<BookmarkInfo>();
        activity.fab = refresh_btn = view.findViewById(R.id.refresh_btn);


        insertData();       //하드코딩한 부분 - 메소드는 그대로 쓰면서 내용만 바꿀예정
        final RecyclerView bmRecyclerView = view.findViewById(R.id.bookmark);

        bmAdapter = new BookmarkRecyclerViewAdapter(bookmarkInfos,activity.context_main);
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
//                intent.putExtra("arsId", arsId);
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

        currentBusInfos.add(new CurrentBusInfo(getContext().getColor(R.color.bus_blue),3,"601","개화역광역환승센터방면","곧 도착", "10분36초후[8번째 전]",true));
        currentBusInfos.add(new CurrentBusInfo(getContext().getColor(R.color.bus_green),5,"6716","양천공영차고지방면", "5분28초후[3번째 전]","15분46초후[11번째 전]",true));

        bookmarkInfos.add(new BookmarkInfo("등촌역.강서보건소", currentBusInfos));

        ArrayList<CurrentBusInfo> currentBusInfos1 = new ArrayList<>();

        currentBusInfos1.add(new CurrentBusInfo(getContext().getColor(R.color.bus_green),4,"6715","신월동우성상가방면", "곧 도착","7분51초후[5번째 전]", true));

        bookmarkInfos.add(new BookmarkInfo("목동역", currentBusInfos1));

        ArrayList<CurrentBusInfo> currentBusInfos2 = new ArrayList<>();

        currentBusInfos2.add(new CurrentBusInfo(getContext().getColor(R.color.bus_green),0,"7734","진관공영차고지방면", "1분43초후[1번째 전]","12분35초후[8번째 전]", true));

        bookmarkInfos.add(new BookmarkInfo("옥수역", currentBusInfos2));
    }
}
