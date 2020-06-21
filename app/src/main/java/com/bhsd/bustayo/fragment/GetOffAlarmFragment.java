package com.bhsd.bustayo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bhsd.bustayo.R;
import com.bhsd.bustayo.activity.MainActivity;
import com.bhsd.bustayo.application.SetAlarmDialog;
import com.bhsd.bustayo.adapter.StationListAdapter;
import com.bhsd.bustayo.dto.StationListItem;

import java.util.ArrayList;


public class GetOffAlarmFragment extends Fragment {

    MainActivity activity;
    ArrayList<StationListItem> stationListItems;
    AutoCompleteTextView busAuto;
    ArrayList<String> stationitem;
    ImageView mapping;
    RecyclerView busStation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);

        activity = (MainActivity)getActivity();
        stationListItems = new ArrayList<StationListItem>();
        busAuto = view.findViewById(R.id.search_bus_auto);
        mapping = view.findViewById(R.id.search_bus_button);
        busStation = view.findViewById(R.id.station_list);

        RecyclerView slRecyclerView = view.findViewById(R.id.station_list);

        final StationListAdapter slAdapter = new StationListAdapter();
        slRecyclerView.setHasFixedSize(true);
        slRecyclerView.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));
        slRecyclerView.setAdapter(slAdapter);

        //하드코딩
        stationitem = new ArrayList<>();
        stationitem.add("버스이름 - 어디방면");
        stationitem.add("버스이름 - 요기방면");
        stationitem.add("버스이름2 - 어디방면");
        stationitem.add("버스이름2 - 우리집");
        stationitem.add("버스이름3 - 우리집");
        stationitem.add("버스이름3 - 어디방면");
        ArrayAdapter adapter = new ArrayAdapter(activity, android.R.layout.simple_dropdown_item_1line,stationitem);
        busAuto.setAdapter(adapter);

        mapping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //버스 번호에 해당하는 값이 있는 경우 리스트를 출력하고 아니면 출력하지 않고 메시지를 띄운다
                    busStation.setVisibility(View.VISIBLE);

            }
        });

        slAdapter.setOnListItemSelected(new StationListAdapter.OnListItemSelected() {
            @Override
            public void onItemSelected(View v, int position) {

                //몇 정류장전에서 알람을 받을지 설정
                SetAlarmDialog alarmsetting = new SetAlarmDialog();
                alarmsetting.Dialog(activity);
            }
        });
/*
        //하드코딩
        slAdapter.addItem(new StationListItem("등촌","아이디",1,2));
        slAdapter.addItem(new StationListItem("등촌옆","아이디",2,3));
        slAdapter.addItem(new StationListItem("등촌옆옆","아이디",4,5));
        slAdapter.addItem(new StationListItem("등촌옆옆옆","아이디",3,4));
        slAdapter.addItem(new StationListItem("등촌옆옆옆옆","아이디",0,1));
        slAdapter.addItem(new StationListItem("등촌옆옆옆옆옆","아이디",5,6));
*/
        return view;
    }
}
