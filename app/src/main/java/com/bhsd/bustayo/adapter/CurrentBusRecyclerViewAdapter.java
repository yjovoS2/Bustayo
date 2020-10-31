package com.bhsd.bustayo.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bhsd.bustayo.R;
import com.bhsd.bustayo.application.SetAlarmDialog;
import com.bhsd.bustayo.dto.CurrentBusInfo;

import java.util.ArrayList;


public class CurrentBusRecyclerViewAdapter extends RecyclerView.Adapter<CurrentBusRecyclerViewAdapter.CurrentBusViewHolder>{

    ArrayList<CurrentBusInfo> currentBusInfos;
    BookmarkRecyclerViewAdapter bAdapter;
    //중첩된것인지 확인
    boolean isInclude = false;

    private OnListItemSelected listener = null;

    public interface OnListItemSelected{
        void onItemSelected(View v, int position);
    }

    public void setOnListItemSelected(OnListItemSelected listener){
        this.listener = listener;
    }

    public CurrentBusRecyclerViewAdapter(ArrayList<CurrentBusInfo> currentBusInfos, boolean isInclude) {
        this.currentBusInfos = currentBusInfos;
        this.isInclude = isInclude;
    }

    public class CurrentBusViewHolder extends RecyclerView.ViewHolder{
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
        if(currentBusInfos.get(position).isBookmark())
            holder.bookmark.setImageResource(R.drawable.ic_bookmark);
        else
            holder.bookmark.setImageResource(R.drawable.ic_bookmark_empty);
        holder.bookmark.setColorFilter(Color.parseColor("#AAAAAA"));
        holder.busColor.setImageResource(R.drawable.ic_bus_);
        holder.busCongested.setImageResource(R.drawable.ic_man);
        holder.alarm.setImageResource(R.drawable.ic_alarm);
        holder.busColor.setColorFilter(currentBusInfos.get(position).getBusColor()); //버스 색
        holder.busNum.setText(currentBusInfos.get(position).getBusNum());
        holder.busDestination.setText(currentBusInfos.get(position).getBusDestination());
        /*
        holder.currentbus1.setText(currentBusInfos.get(position).getCurrentLocation1());
        holder.currentbus2.setText(currentBusInfos.get(position).getCurrentLocation2());
        */

        // ---------------------------------------------------------------------
        holder.currentbus1.setText(getBusMsg(currentBusInfos.get(position).getCurrentLocation1()));
        holder.currentbus2.setText(getBusMsg(currentBusInfos.get(position).getCurrentLocation2()));
        // ---------------------------------------------------------------------

        int BusCongestion = currentBusInfos.get(position).getBusCongestion();
        String color = "#";
        switch (BusCongestion){
            case 3 : color = "#70BF70"; break; // 여유
            case 4 : color = "#F8E95F";break; // 보통
            case 5 :    // 혼잡
            case 6 : color = "#D16969";break;
            case 0 :    // 데이터 없음
            default: color = "#AAAAAA";
        }
        holder.busCongested.setColorFilter(Color.parseColor(color));

        if(currentBusInfos.get(position).isBookmark()) {
            holder.bookmark.setImageResource(R.drawable.ic_bookmark);
            holder.bookmark.setColorFilter(Color.parseColor("#F8E95F"));
        }

        holder.bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentBusInfos.get(position).setBookmark(!(currentBusInfos.get(position).isBookmark()));
//                북마크가 false이면 데이터베이스에서 삭제 true면 삽입
                if (isInclude)
                    if (!(currentBusInfos.get(position).isBookmark())){}
                        currentBusInfos.remove(position);
                notifyDataSetChanged();

//                if (currentBusInfos.get(position).isBookmark())
//                    디비에 insert - 정류장테이블이 있으면
//              else
//                    디비에서 삭제
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

    public CurrentBusInfo getItem(int position){ return currentBusInfos.get(position); }

    @Override
    public int getItemCount() {
        return currentBusInfos.size();
    }
}
