package com.bhsd.bustayo.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bhsd.bustayo.MainActivity;
import com.bhsd.bustayo.R;
import com.bhsd.bustayo.application.SetAlarmDialog;
import com.bhsd.bustayo.database.ApplicationDB;
import com.bhsd.bustayo.dto.BookmarkInfo;
import com.bhsd.bustayo.dto.CurrentBusInfo;

import java.util.ArrayList;


public class CurrentBusRecyclerViewAdapter extends RecyclerView.Adapter<CurrentBusRecyclerViewAdapter.CurrentBusViewHolder> {

    ArrayList<CurrentBusInfo> currentBusInfos;
    BookmarkInfo item;
    //중첩된것인지 확인
    boolean isInclude = false;

    Activity activity;
    ApplicationDB DBHelper;
    SQLiteDatabase dbSQL;
    String arsId;
    private ArrayList<String> bookmarkBusStation;
    private ArrayList<String[]> bookmark;

    private OnListItemSelected listener = null;

    public interface OnListItemSelected {
        void onItemSelected(View v, int position);
    }

    public void setOnListItemSelected(OnListItemSelected listener) {
        this.listener = listener;
    }

    public CurrentBusRecyclerViewAdapter(ArrayList<CurrentBusInfo> currentBusInfos, boolean isInclude, Activity activity) {
        this.currentBusInfos = currentBusInfos;
        this.isInclude = isInclude;
        this.activity = activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setArsId(String arsId) {
        this.arsId = arsId;
    }

    public class CurrentBusViewHolder extends RecyclerView.ViewHolder {
        ImageView busColor, alarm, bookmark, busCongested;
        public TextView busNum, busDestination, currentbus1, currentbus2;

        public CurrentBusViewHolder(@NonNull View itemView) {
            super(itemView);
            busColor = itemView.findViewById(R.id.bus_color);
            busCongested = itemView.findViewById(R.id.busCongested);
            busNum = itemView.findViewById(R.id.bus_num);
            busDestination = itemView.findViewById(R.id.bus_destination);
            currentbus1 = itemView.findViewById(R.id.bus_first);
            currentbus2 = itemView.findViewById(R.id.bus_second);
            alarm = itemView.findViewById(R.id.bus_alarm);
            bookmark = itemView.findViewById(R.id.bus_bookmark);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (listener != null) {
                            listener.onItemSelected(v, position);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public CurrentBusRecyclerViewAdapter.CurrentBusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_bus_item, parent, false);

        return new CurrentBusViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CurrentBusRecyclerViewAdapter.CurrentBusViewHolder holder, final int position) {
        //디비에서 불러와서 포지션당
        bookmark = new ArrayList<>();
        bookmarkBusStation = new ArrayList<>();

        bookmarkBusStation.clear();
        bookmark.clear();

        DBHelper = new ApplicationDB(activity);
        SQLiteDatabase dbSQL = DBHelper.getReadableDatabase();
        Cursor cursor = dbSQL.rawQuery("SELECT DISTINCT arsId FROM bookmarkTB;", null);
        while (cursor.moveToNext())
            bookmarkBusStation.add(cursor.getString(0));

        cursor = dbSQL.rawQuery("SELECT * FROM bookmarkTB;", null);
        while (cursor.moveToNext())
            bookmark.add(new String[]{cursor.getString(0), cursor.getString(1)});

        cursor.close();
        dbSQL.close();

        for(String bookmarkstation : bookmarkBusStation){
            if(currentBusInfos.get(position).getArsId().equals(bookmarkstation)){
                for(String[] bookmarkbus : bookmark){
                    if (currentBusInfos.get(position).getBusRouteId().equals(bookmarkbus[0])&&currentBusInfos.get(position).getArsId().equals(bookmarkbus[1]))
                        currentBusInfos.get(position).setBookmark(true);
                }
            }
        }

        if (currentBusInfos.get(position).isBookmark())
            holder.bookmark.setImageResource(R.drawable.ic_bookmark);
        else
            holder.bookmark.setImageResource(R.drawable.ic_bookmark_empty);
        holder.bookmark.setColorFilter(Color.parseColor("#AAAAAA"));
        holder.busColor.setImageResource(R.drawable.ic_bus_);
        holder.busCongested.setImageResource(R.drawable.ic_man);
        holder.alarm.setImageResource(R.drawable.ic_alarm);
        holder.busNum.setText(currentBusInfos.get(position).getBusNum());
        holder.busDestination.setText(currentBusInfos.get(position).getBusDestination());

        // ---------------------------------------------------------------------
        holder.currentbus1.setText(getBusMsg(currentBusInfos.get(position).getCurrentLocation1()));
        holder.currentbus2.setText(getBusMsg(currentBusInfos.get(position).getCurrentLocation2()));
        // ---------------------------------------------------------------------

//  혼잡도
//        int BusCongestion = Integer.parseInt(currentBusInfos.get(position).getBusCongestion());
        String color = "#";
//
//        if(BusCongestion >= 0 &&BusCongestion <=10)
//            color = "#70BF70";
//        else if (BusCongestion <= 20)
//            color = "#F8E95F";
//        else
//            color = "#D16969";
//
//        holder.busCongested.setColorFilter(Color.parseColor(color));

        int buscolor = Integer.parseInt(currentBusInfos.get(position).getBusColor());
        switch (buscolor) {
            case 1: // 공항
                color = "#0000FF";
                break;
            case 2: // 마을
            case 4: // 지선
                color = "#70BF70";
                break;
            case 3: // 간선
                color = "#0071c2";
                break;
            case 5: // 순환
                color = "#F8E95F";
                break;
            case 6: // 광역
            case 0: // 공용
                color = "#D16969";
                break;
            case 7: // 인천
            case 8: // 경기
            case 9: // 폐지
            default: color = "#D16969"; //나중에 수정이 필요함
        }
        holder.busColor.setColorFilter(Color.parseColor(color));


        if (currentBusInfos.get(position).isBookmark()) {
            holder.bookmark.setImageResource(R.drawable.ic_bookmark);
            holder.bookmark.setColorFilter(Color.parseColor("#F8E95F"));
        }

        holder.bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentBusInfos.get(position).setBookmark(!(currentBusInfos.get(position).isBookmark()));


                SQLiteDatabase dbSQL;
                DBHelper = new ApplicationDB(activity);
                dbSQL = DBHelper.getWritableDatabase();
//                북마크가 false이면 데이터베이스에서 삭제 true면 삽입
                if (currentBusInfos.get(position).isBookmark())
                    dbSQL.execSQL("INSERT INTO bookmarkTB VALUES(" + currentBusInfos.get(position).getBusRouteId() + "," + arsId + ")");
                else
                    dbSQL.execSQL("DELETE FROM bookmarkTB WHERE busRouteId = " + currentBusInfos.get(position).getBusRouteId() + " AND arsId = " + currentBusInfos.get(position).getArsId());

                dbSQL.close();
                if (isInclude)
                    currentBusInfos.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //몇 정류장전에서 알람을 받을지 설정
                SetAlarmDialog alarmsetting = new SetAlarmDialog();
                alarmsetting.Dialog(v.getRootView().getContext());
            }
        });
    }

    /* 버스 도착시간 메시지 정리 */
    private String getBusMsg(String msg) {
        String[] split_text = msg.split("후");
        // "곧 도착", "운행종료", "출발대기"  →  0분0초 이외 상태들
        String time = split_text[0], count = "";

        if(split_text.length > 1) {
            String[] temp = time.split("분");
            count = split_text[1];
            time = temp[0] + "분";
        }

        return time + count;
    }

    public CurrentBusInfo getItem(int position) {
        return currentBusInfos.get(position);
    }

    @Override
    public int getItemCount() {
        return currentBusInfos.size();
    }
}