package com.bhsd.bustayo;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookmarkFragment extends Fragment {

    MainActivity activity;
    ArrayList<BookmarkInfo> bookmarkInfos;
    RecyclerView currentBus;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);

        activity = (MainActivity)getActivity();
        bookmarkInfos = new ArrayList<BookmarkInfo>();

        insertData();
        RecyclerView bmRecyclerView = view.findViewById(R.id.bookmark);

        final BookmarkRecyclerViewAdapter bmAdapter = new BookmarkRecyclerViewAdapter(bookmarkInfos,activity.context_main);
        bmRecyclerView.setHasFixedSize(true);
        bmRecyclerView.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));
        bmRecyclerView.setAdapter(bmAdapter);

        bmAdapter.setOnListItemSelected(new BookmarkRecyclerViewAdapter.OnListItemSelected() {
            @Override
            public void onItemSelected(View v, int position, RecyclerView selected) {
                if(selected.getVisibility()!=View.VISIBLE)
                    selected.setVisibility(View.VISIBLE);
                else
                    selected.setVisibility(View.GONE);
            }
        });

        return view;
    }

    //test용
    public void insertData(){
        ArrayList<CurrentBusInfo> currentBusInfos = new ArrayList<>();

        currentBusInfos.add(new CurrentBusInfo(Color.BLUE,0,601,"우리집","곧 도착", "이따 도착"));
        currentBusInfos.add(new CurrentBusInfo(Color.GREEN,1,6716,"등촌역", "곧도착","막차"));

        bookmarkInfos.add(new BookmarkInfo("등촌역", currentBusInfos));

        ArrayList<CurrentBusInfo> currentBusInfos1 = new ArrayList<>();

        currentBusInfos1.add(new CurrentBusInfo(Color.GREEN,1,6716,"등촌역", "곧도착","막차"));

        bookmarkInfos.add(new BookmarkInfo("목동역", currentBusInfos1));
    }

}
