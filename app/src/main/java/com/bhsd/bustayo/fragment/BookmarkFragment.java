package com.bhsd.bustayo.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

    MainActivity activity;
    ArrayList<BookmarkInfo> bookmarkInfos;
    FloatingActionButton refresh_btn;
    public static BookmarkRecyclerViewAdapter bmAdapter;
    private ApplicationDB DBHelper;
    private ArrayList<String> bookmarkBusStation;
    private ArrayList<String[]> bookmark;
    private ArrayList<HashMap<String, String>> busList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);

        activity = (MainActivity) getActivity();
        bookmarkInfos = new ArrayList<>();
        activity.fab = refresh_btn = view.findViewById(R.id.refresh_btn);
        final RecyclerView bmRecyclerView = view.findViewById(R.id.bookmark);
        bmRecyclerView.setHasFixedSize(false);
        bmRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        bmAdapter = new BookmarkRecyclerViewAdapter(bookmarkInfos, activity, true);

        //DB 연결 및 어댑터 생성
        DBHelper = new ApplicationDB(activity);
        bookmarkBusStation = new ArrayList<>();
        bookmark = new ArrayList<>();
        busList = new ArrayList<>();

//                1. 데이터베이스에서 값을 불러옴

        bookmarkBusStation.clear();
        bookmarkInfos.clear();
        bookmark.clear();

        SQLiteDatabase dbSQL = DBHelper.getReadableDatabase();
        Cursor cursor = dbSQL.rawQuery("SELECT DISTINCT arsId FROM bookmarkTB;", null);
        while (cursor.moveToNext())
            bookmarkBusStation.add(cursor.getString(0));

        cursor = dbSQL.rawQuery("SELECT * FROM bookmarkTB;", null);
        while (cursor.moveToNext())
            bookmark.add(new String[]{cursor.getString(0), cursor.getString(1)});

        cursor.close();
        dbSQL.close();

//                2. api를 이용하여 해당하는 버스의 실시간 정보를 가지고옴
        if (bookmarkBusStation != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (String bookmarkstation : bookmarkBusStation) {
                        busList = APIManager.getAPIArray(APIManager.GET_STATION_BY_UID_ITEM, new String[]{bookmarkstation}, new String[]{"adirection", "arsId", "stNm", "busRouteId", "rtNm", "congestion", "routeType", "arrmsg1", "arrmsg2"});
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
                                    crbuslist.add(new CurrentBusInfo(map.get("busRouteId"), map.get("routeType"), map.get("congestion"), busStationName, map.get("adirection"), map.get("arrmsg1"), map.get("arrmsg2"), true, map.get("arsId")));
                                }
                            }
                        }
                        bookmarkInfos.add(new BookmarkInfo(stNm, arsId, crbuslist));
                    }
                }
            }).start();

            bmRecyclerView.setAdapter(bmAdapter);
            bmAdapter.notifyDataSetChanged();
        } else
            Toast.makeText(activity, "현재 즐겨찾기한 버스가 없음", Toast.LENGTH_SHORT).show();

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
                bookmarkBusStation.clear();
                bookmarkInfos.clear();
                bookmark.clear();
                busList.clear();

                SQLiteDatabase dbSQL = DBHelper.getReadableDatabase();
                Cursor cursor = dbSQL.rawQuery("SELECT DISTINCT arsId FROM bookmarkTB;", null);
                while (cursor.moveToNext())
                    bookmarkBusStation.add(cursor.getString(0));

                cursor = dbSQL.rawQuery("SELECT * FROM bookmarkTB;", null);
                while (cursor.moveToNext())
                    bookmark.add(new String[]{cursor.getString(0), cursor.getString(1)});

                cursor.close();
                dbSQL.close();


//              2. api를 이용하여 해당하는 버스의 실시간 정보를 가지고옴
                if (bookmarkBusStation != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (final String bookmarkstation : bookmarkBusStation) {
                                busList = APIManager.getAPIArray(APIManager.GET_STATION_BY_UID_ITEM, new String[]{bookmarkstation}, new String[]{"adirection", "arsId", "stNm", "busRouteId", "rtNm", "rerideNum1", "routeType", "arrmsg1", "arrmsg2"});

                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
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
                                                    crbuslist.add(new CurrentBusInfo(map.get("busRouteId"), map.get("routeType"), map.get("rerideNum1"), busStationName, map.get("adirection"), map.get("arrmsg1"), map.get("arrmsg2"), true, map.get("arsId")));
                                                }
                                            }
                                        }
                                        bookmarkInfos.add(new BookmarkInfo(stNm, arsId, crbuslist));
                                    }
                                });
                            }
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    bmAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }).start();
                } else
                    Toast.makeText(activity, "현재 즐겨찾기한 버스가 없음", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}