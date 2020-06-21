package com.bhsd.bustayo;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import static com.bhsd.bustayo.MainActivity.context_main;

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
    public void onBindViewHolder(@NonNull CurrentBusRecyclerViewAdapter.CurrentBusViewHolder holder, final int position) {
        holder.busColor.setColorFilter(currentBusInfos.get(position).getBusColor()); //버스 색
        holder.busNum.setText(currentBusInfos.get(position).getBusNum()+"");
        holder.busDestination.setText(currentBusInfos.get(position).getBusDestination());
        holder.currentbus1.setText(currentBusInfos.get(position).getCurrentLocation1());
        holder.currentbus2.setText(currentBusInfos.get(position).getCurrentLocation2());

        int BusCongestion = currentBusInfos.get(position).getBusCongestion();
        switch (BusCongestion){
            case 0 : holder.busCongested.setColorFilter(Color.parseColor("#23DA23"));break;
            case 10 : holder.busCongested.setColorFilter(Color.parseColor("#FFE600"));break;
            case 20 : holder.busCongested.setColorFilter(Color.parseColor("#DF1616"));break;
            default:;
        }

        if(isInclude)
            holder.bookmark.setVisibility(View.GONE);
        else
            holder.bookmark.setVisibility(View.VISIBLE);


        if(currentBusInfos.get(position).isBookmark())
            holder.bookmark.setColorFilter(Color.YELLOW);

        holder.alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //몇 정류장전에서 알람을 받을지 설정
                SetAlarmDialog alarmsetting = new SetAlarmDialog();
                alarmsetting.Dialog(context_main);
            }
        });

        holder.bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentBusInfos.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,currentBusInfos.size());
                notifyDataSetChanged();
            }
        });
    }

    public CurrentBusInfo getItem(int position){ return currentBusInfos.get(position); }

    @Override
    public int getItemCount() {
        return currentBusInfos.size();
    }
}
