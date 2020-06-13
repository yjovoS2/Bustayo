package com.bhsd.bustayo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

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
    public void onBindViewHolder(@NonNull BookmarkRecyclerViewAdapter.BookmarkViewHolder holder, int position) {
        CurrentBusRecyclerViewAdapter adapter = new CurrentBusRecyclerViewAdapter(bookmarkInfos.get(position).getCurrentBusInfo());
        holder.currnetBusInfo.setHasFixedSize(true);
        holder.currnetBusInfo.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        holder.currnetBusInfo.setAdapter(adapter);
        holder.busStopName.setText(bookmarkInfos.get(position).getBusStopName());
        holder.drawer.setImageResource(R.drawable.ic_drawer_down);

    }

    @Override
    public int getItemCount() {
        return bookmarkInfos.size();
    }
}
