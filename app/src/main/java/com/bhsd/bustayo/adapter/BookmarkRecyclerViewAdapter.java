package com.bhsd.bustayo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bhsd.bustayo.R;
import com.bhsd.bustayo.activity.StationListActivity;
import com.bhsd.bustayo.dto.BookmarkInfo;

import java.util.ArrayList;

import static com.bhsd.bustayo.activity.MainActivity.context_main;

public class BookmarkRecyclerViewAdapter extends RecyclerView.Adapter<BookmarkRecyclerViewAdapter.BookmarkViewHolder> {

    ArrayList<BookmarkInfo> bookmarkInfos;
    Context context;
    private OnListItemSelected listener = null;

    public interface OnListItemSelected{
        void onItemSelected(View v, int position, RecyclerView selected);
    }

    public void setOnListItemSelected(OnListItemSelected listener){
        this.listener = listener;
    }

    public BookmarkRecyclerViewAdapter(ArrayList<BookmarkInfo> bookmarkInfos, Context context) {
        this.bookmarkInfos = bookmarkInfos;
        this.context = context;
    }

    public class BookmarkViewHolder extends RecyclerView.ViewHolder{
        public TextView busStopName;
        public ImageView drawer, bookmark_button;
        public RecyclerView currnetBusInfo;

        public BookmarkViewHolder(@NonNull View itemView) {
            super(itemView);
            busStopName = itemView.findViewById(R.id.bustop_name);
            drawer = itemView.findViewById(R.id.drawer_button);
            currnetBusInfo = itemView.findViewById(R.id.bus_bookmark);
            bookmark_button = itemView.findViewById(R.id.bookmark_button);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        if(listener!= null){
                            listener.onItemSelected(v,position, currnetBusInfo);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public BookmarkRecyclerViewAdapter.BookmarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_bookmark, parent, false);

        return new BookmarkViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final BookmarkRecyclerViewAdapter.BookmarkViewHolder holder, final int position) {

        final CurrentBusRecyclerViewAdapter adapter = new CurrentBusRecyclerViewAdapter(bookmarkInfos.get(position).getCurrentBusInfo(), true);
        holder.currnetBusInfo.setHasFixedSize(true);
        holder.currnetBusInfo.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        holder.currnetBusInfo.setAdapter(adapter);
        holder.busStopName.setText(bookmarkInfos.get(position).getBusStopName());
        holder.drawer.setImageResource(R.drawable.ic_drawer_down);
        //holder.bookmark_button.setColorFilter(currentBusInfos.get(position).getBusColor()); 로 색 바꿔줘야하는데 내용 바꿔여함

        adapter.setOnListItemSelected(new CurrentBusRecyclerViewAdapter.OnListItemSelected() {
            @Override
            public void onItemSelected(View v, int pos) {
                //이부분에 데이터넘겨주는 부분
                Intent intent = new Intent(context_main, StationListActivity.class);
                String busNum = adapter.getItem(pos).getBusNum()+"";
                intent.putExtra("busRouteNm", busNum);
                context_main.startActivity(intent);
            }
        });

        holder.drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.currnetBusInfo.getVisibility()!=View.VISIBLE)
                    holder.currnetBusInfo.setVisibility(View.VISIBLE);
                else
                    holder.currnetBusInfo.setVisibility(View.GONE);
            }
        });

        holder.bookmark_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookmarkInfos.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,bookmarkInfos.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookmarkInfos.size();
    }
}
