package com.bhsd.bustayo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CurrentBusRecyclerViewAdapter extends RecyclerView.Adapter<CurrentBusRecyclerViewAdapter.CurrentBusViewHolder> {

    ArrayList<CurrentBusInfo> currentBusInfos;

    private OnListItemSelected listener = null;

    public interface OnListItemSelected{
        void onItemSelected(View v, int position);
    }

    public CurrentBusRecyclerViewAdapter(ArrayList<CurrentBusInfo> currentBusInfos) {
        this.currentBusInfos = currentBusInfos;
    }

    public class CurrentBusViewHolder extends RecyclerView.ViewHolder{
        ImageView busColor;
        public TextView busNum, busDestination, currentbus1, currentbus2;

        public CurrentBusViewHolder(@NonNull View itemView) {
            super(itemView);
            busColor = itemView.findViewById(R.id.bus_state);
            busNum = itemView.findViewById(R.id.bus_num);
            busDestination = itemView.findViewById(R.id.bus_destination);
            currentbus1 = itemView.findViewById(R.id.bus_first);
            currentbus2 = itemView.findViewById(R.id.bus_second);

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
    public void onBindViewHolder(@NonNull CurrentBusRecyclerViewAdapter.CurrentBusViewHolder holder, int position) {
        holder.busColor.setColorFilter(currentBusInfos.get(position).getBusColor());   //이부분은 데이터를 받아와서 버스색이랑 혼잡도 색 다시 설정해줘야함
        holder.busNum.setText(currentBusInfos.get(position).getBusNum()+"");
        holder.busDestination.setText(currentBusInfos.get(position).getBusDestination());
        holder.currentbus1.setText(currentBusInfos.get(position).getCurrentLocation1());
        holder.currentbus2.setText(currentBusInfos.get(position).getCurrentLocation2());
    }

    @Override
    public int getItemCount() {
        return currentBusInfos.size();
    }
}
