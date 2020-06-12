package com.bhsd.bustayo;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.AdapterListUpdateCallback;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookmarkFragment extends Fragment {

    MainActivity activity;
    RecyclerView busStopList, busList;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<CurrentBusInfo> currentBusInfos;
    ArrayList<BookmarkInfo> bookmarkInfo;
    BookmarkRecyclerViewAdapter badapter;
    CurrentBusRecyclerViewAdpater cadapter;
    ImageView drawer;
    int i;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        View fView = inflater.inflate(R.layout.recyclerview_bookmark, container, false);

        activity = (MainActivity)getActivity();
        busStopList = view.findViewById(R.id.bookmark);
        layoutManager = new LinearLayoutManager(activity);
        busStopList.setHasFixedSize(true);
        busStopList.setLayoutManager(layoutManager);
        drawer = fView.findViewById(R.id.drawer_button);
        busList = fView.findViewById(R.id.);
        busList.setHasFixedSize(true);
        busStopList.setLayoutManager(layoutManager);

        currentBusInfos = new ArrayList<>();
        bookmarkInfo = new ArrayList<>();

        busStopList.setAdapter(badapter);
        busList.setAdapter(cadapter);

        //하드코딩 - 테스트용
        currentBusInfos.add(new CurrentBusInfo(Color.BLUE, 1, 601, "우리집", "곧도착", "나아중에 도착"));
        bookmarkInfo.add(new BookmarkInfo("우리집앞", currentBusInfos));

        badapter = new BookmarkRecyclerViewAdapter(bookmarkInfo);
        cadapter = new CurrentBusRecyclerViewAdpater(currentBusInfos);


        drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawer.getImageAlpha()== R.drawable.ic_drawer_down)
                    drawer.setImageResource(R.drawable.ic_drawer_up);
                else
                    drawer.setImageResource(R.drawable.ic_drawer_down);
            }
        });

        return view;
    }
}
