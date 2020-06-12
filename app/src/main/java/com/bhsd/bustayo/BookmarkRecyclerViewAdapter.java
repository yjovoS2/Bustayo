package com.bhsd.bustayo;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BookmarkRecyclerViewAdapter extends RecyclerView.Adapter<BookmarkRecyclerViewAdapter.BookmarkViewHolder> {

    ArrayList<BookmarkInfo> bookmarkInfos;
    private OnListItemSelected listener = null;

    public interface OnListItemSelected{
        void onItemSelected(View v, int position);
    }

    public BookmarkRecyclerViewAdapter(ArrayList<BookmarkInfo> bookmarkInfos) {
        this.bookmarkInfos = bookmarkInfos;
        //포함하는 리사이클러뷰 정보 가지고와서 대입
    }

    public class BookmarkViewHolder extends RecyclerView.ViewHolder{
        public TextView busStopName;
        public ImageView drawer;
        public RecyclerView currnetBusInfo;

        public BookmarkViewHolder(@NonNull View itemView) {
            super(itemView);
            busStopName = itemView.findViewById(R.id.bustop_name);
            drawer = itemView.findViewById(R.id.drawer_button);
            currnetBusInfo = itemView.findViewById(R.id.bus_bookmark);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        if(listener!= null){
                            listener.onItemSelected(v,position);
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
    public void onBindViewHolder(@NonNull BookmarkRecyclerViewAdapter.BookmarkViewHolder holder, int position) {
        CurrentBusRecyclerViewAdpater adapter = new CurrentBusRecyclerViewAdpater(bookmarkInfos.get(position).getCurrentBusInfo());
        holder.busStopName.setText(bookmarkInfos.get(position).getBusStopName());
        holder.drawer.setImageResource(R.drawable.ic_drawer_down);
        holder.currnetBusInfo.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return bookmarkInfos.size();
    }
}
