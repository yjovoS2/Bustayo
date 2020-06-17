package com.bhsd.bustayo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StationListAdapter extends RecyclerView.Adapter<StationListAdapter.ItemViewHolder> {

    private ArrayList<StationListItem> list = new ArrayList<>();
    private static OnListItemSelected listener = null;

    public interface OnListItemSelected{
        void onItemSelected(View v, int position);
    }

    public void setOnListItemSelected(OnListItemSelected listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.station_list, parent, false);
        return new ItemViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    void addItem(StationListItem item) {
        list.add(item);
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView stationName;
        private ImageView previous;
        private ImageView next;
        private String stationId;

        ItemViewHolder(View itemView, final Context context) {
            super(itemView);

            stationName = itemView.findViewById(R.id.busstop_name);
            previous = itemView.findViewById(R.id.previous_busstop);
            next = itemView.findViewById(R.id.next_busstop);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        if(context == MainActivity.context_main)
                            //하차알림의 클릭과의 구분
                            listener.onItemSelected(v, position);
                        else
                        {
                            if(stationId.equals("0") || stationId.equals(" ")) {
                                Toast.makeText(context, "정류장 정보를 받아올 수 없습니다.", Toast.LENGTH_LONG).show();
                                return;
                            }
                            Intent intent = new Intent(context, StationActivity.class);
                            intent.putExtra("stationNm", stationName.getText().toString());
                            intent.putExtra("arsId", stationId);
                            context.startActivity(intent);
                        }
                    }
                }
            });
        }

        void onBind(StationListItem item) {
            stationName.setText(item.getStationName());
            stationId = item.getStationId();
            previous.setBackgroundColor(item.getPreviousColor());
            next.setBackgroundColor(item.getNextColor());
        }
    }
}
