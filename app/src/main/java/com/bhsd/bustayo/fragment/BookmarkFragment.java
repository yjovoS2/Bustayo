package com.bhsd.bustayo.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bhsd.bustayo.MainActivity;
import com.bhsd.bustayo.R;
import com.bhsd.bustayo.activity.StationActivity;
import com.bhsd.bustayo.adapter.BookmarkRecyclerViewAdapter;
import com.bhsd.bustayo.application.APIManager;
import com.bhsd.bustayo.database.ApplicationDB;
import com.bhsd.bustayo.dto.BookmarkInfo;
import com.bhsd.bustayo.dto.CurrentBusInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

public class BookmarkFragment extends Fragment {
    private final static String LOG_NAME = "BookmarkFragment";

    MainActivity activity;
    private ArrayList<BookmarkInfo> bookmarkInfos;          // 어댑터 연결된 아이템!!!!
    private FloatingActionButton refresh_btn;
    public static BookmarkRecyclerViewAdapter bmAdapter;
    private RecyclerView bmRecyclerView;
    private ApplicationDB DBHelper;
    private ArrayList<String> bookmarkBusStation;
    private ArrayList<String[]> bookmark;
    private ArrayList<HashMap<String, String>> busList;

    // 자동새로고침을 위한 것들
    private Handler handler = new Handler();
    private Runnable runnable;
    private int delay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);

        init(view);
        // DB에서 아이템 받아오고
        getDBItems();
        // api에서 값 받아오고
        getAPIItems();

        //recyclerView 클릭이벤트
        bmAdapter.setOnListItemSelected(new BookmarkRecyclerViewAdapter.OnListItemSelected() {
            @Override
            public void onItemSelected(View v, int position, RecyclerView selected) {
                Log.e("ljh", "onItemSelected");
                Intent intent = new Intent(activity, StationActivity.class);
                intent.putExtra("stationNm", bookmarkInfos.get(position).getBusStopName());
                intent.putExtra("arsId", bookmarkInfos.get(position).getArsId());
                activity.startActivity(intent);
            }
        });

        //floatingActionButton 클릭이벤트
        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDBItems();

                getAPIItems();
            }
        });
        return view;
    }

    private void init(View view) {
        activity = (MainActivity) getActivity();
        activity.fab = refresh_btn = view.findViewById(R.id.refresh_btn);
        bmRecyclerView = view.findViewById(R.id.bookmark);
        busList = new ArrayList<>();
        bookmarkInfos = new ArrayList<>();


        bmRecyclerView.setHasFixedSize(false);
        bmRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        bmAdapter = new BookmarkRecyclerViewAdapter(bookmarkInfos, activity, true);
        bmRecyclerView.setAdapter(bmAdapter);

        //DB 연결
        DBHelper = new ApplicationDB(activity);

        // SharedPreferences에 저장된 refresh 값 가져오기!!
        delay = activity.getSharedPreferences("setting", 0).getInt("refresh", 0);
    }

    private void getDBItems() {
        //bookmarkBusStation.clear();
        //bookmarkInfos.clear();
        //bookmark.clear();

        bookmarkBusStation = new ArrayList<>();
        bookmark = new ArrayList<>();

        SQLiteDatabase dbSQL = DBHelper.getWritableDatabase();
        Cursor cursor = dbSQL.rawQuery("SELECT DISTINCT arsId FROM bookmarkTB;", null);
        while (cursor.moveToNext())
            bookmarkBusStation.add(cursor.getString(0));

        cursor = dbSQL.rawQuery("SELECT * FROM bookmarkTB;", null);
        while (cursor.moveToNext())
            bookmark.add(new String[]{cursor.getString(0), cursor.getString(1)});

        cursor.close();
        dbSQL.close();
    }

    private void getAPIItems() {
        if (bookmarkBusStation.size() != 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int i = 0;
                    for (String bookmarkstation : bookmarkBusStation) {
                        busList = APIManager.getAPIArray(APIManager.GET_STATION_BY_UID_ITEM, new String[]{bookmarkstation},
                                new String[]{"adirection", "arsId", "stNm", "busRouteId", "rtNm", "congestion", "routeType", "arrmsg1", "arrmsg2"});
                        setData(i++, bookmarkstation);
                    }
                }
            }).start();

            bmAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(activity, "현재 즐겨찾기한 버스가 없음", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData(int position, String bookmarkstation) {
        String busStationName = "";
        String arsId = "";
        String stNm = "";
        ArrayList<CurrentBusInfo> crbuslist = new ArrayList<>();
        for (String[] bookmarkBus : bookmark) {
            for (HashMap<String, String> map : busList) {
                if (map.get("busRouteId").equals(bookmarkBus[0]) && bookmarkBus[1].equals(bookmarkstation)) {
                    busStationName = map.get("rtNm");
                    arsId = map.get("arsId");
                    stNm = map.get("stNm");
                    crbuslist.add(new CurrentBusInfo(map.get("busRouteId"), map.get("routeType"), map.get("congestion"),
                            busStationName, map.get("adirection"), map.get("arrmsg1"), map.get("arrmsg2"), true, map.get("arsId")));
                }
            }
        }

        if(bookmarkInfos.size() != bookmarkBusStation.size()) {
            bookmarkInfos.add(new BookmarkInfo(stNm, arsId, crbuslist));
        } else {
            bookmarkInfos.set(position, new BookmarkInfo(stNm, arsId, crbuslist));
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);
                Log.e("yj", LOG_NAME + "  " + (delay / 1000) + " second later");
                getDBItems();
                getAPIItems();

                bmAdapter.notifyDataSetChanged();
            }
        }, delay);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
        bookmarkInfos.clear();
        Log.e("yj", LOG_NAME + "refresh remove");
    }
}