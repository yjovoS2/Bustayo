package com.bhsd.bustayo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bhsd.bustayo.activity.MainActivity;
import com.bhsd.bustayo.R;
import com.bhsd.bustayo.activity.StationActivity;
import com.bhsd.bustayo.application.APIManager;
import com.bhsd.bustayo.dto.StationListItem;

import java.util.ArrayList;
import java.util.HashMap;

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

    public void addItem(StationListItem item) {
        list.add(item);
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private View itemView;
        private TextView stationName;
        private ImageView previous;
        private ImageView next;
        private String stationId, arsId, routeId;

        ItemViewHolder(View itemView, final Context context) {
            super(itemView);

            this.itemView = itemView;
            this.context = context;

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
                            if(arsId.equals("0") || arsId.equals(" ")) {
                                Toast.makeText(context, "정류장 정보를 받아올 수 없습니다.", Toast.LENGTH_LONG).show();
                                return;
                            }
                            Intent intent = new Intent(context, StationActivity.class);
                            intent.putExtra("stationNm", stationName.getText().toString());
                            intent.putExtra("arsId", arsId);
                            context.startActivity(intent);
                        }
                    }
                }
            });
        }

        void onBind(StationListItem item) {
            stationName.setText(item.getStationName());
            arsId = item.getArsId();
            stationId = item.getStationId();
            routeId = item.getRouteId();
            previous.setBackgroundColor(item.getPreviousColor());
            next.setBackgroundColor(item.getNextColor());

            final Handler handler = new Handler();
            new Thread() {
                @Override
                public void run() {
                    Log.d("lyj", "\nstationId : " + stationId);

                    final ArrayList<HashMap<String, String>> bus = APIManager.getAPIArray(APIManager.GET_BUSPOS_BY_RT_ID, new String[]{routeId}, new String[]{"lastStnId", "congetion"});
                    for(int i = 0; i < bus.size(); i++) {
                        if(bus.get(i).get("lastStnId").equals(stationId)) {
                            final int index = i;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("lyj", "\n[ congetion ]\n" + bus.get(index).get("congetion"));
                                    addBusIcon(bus.get(index).get("congetion"), 4);
                                }
                            });
                        }
                    }
                }
            }.start();
        }
        void addBusIcon(String congetion, int type) {
            ImageView busIcon = new ImageView(itemView.getContext());
            ConstraintLayout constraint = itemView.findViewById(R.id.station_list_item);

            ConstraintLayout.LayoutParams constParams = new ConstraintLayout.LayoutParams(85, 85);
            constParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            constParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
            constParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
            constParams.setMarginStart(50);

            Drawable bus = context.getDrawable(R.drawable.ic_bus_).mutate();
            Drawable man = context.getDrawable(R.drawable.ic_man).mutate();
            LayerDrawable drawable = new LayerDrawable(new Drawable[]{man, bus});

            drawable.getDrawable(0).setTint(getCongestionColor(Integer.parseInt(congetion)));
            drawable.getDrawable(1).setTint(getTypeColor(type));

            busIcon.setImageDrawable(drawable);

            constraint.addView(busIcon, constParams);
        }

        int getCongestionColor(int congestion) {
            int color_id;
            switch(congestion) {
                case 3: // 여유
                    color_id = R.color.busy_empty;
                case 4: // 보통
                    color_id = R.color.busy_half;
                case 5: //
                case 6:
                    color_id = R.color.busy_full;
                case 0: // 데이터 없음
                default:
                    color_id = R.color.import_error;
            }
            return context.getColor(color_id);
        }
        int getTypeColor(int type) {
            int color_id;
            switch (type) {
                case 1:
                    color_id = R.color.bus_skyblue;
                case 2:
                case 4:
                    color_id = R.color.bus_green;
                case 3:
                    color_id = R.color.bus_blue;
                case 5:
                    color_id = R.color.bus_yellow;
                case 6:
                case 0:
                    color_id = R.color.bus_red;
                case 7:
                case 8:
                case 9:
                default:
                    color_id = R.color.import_error;
            }
            return context.getColor(color_id);
        }

    }
}
