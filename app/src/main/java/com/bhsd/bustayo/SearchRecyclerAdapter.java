package com.bhsd.bustayo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int TYPE_ITEM = 0;
    private final int TYPE_FOOTER = 1;
    private ArrayList<SearchRecyclerItem> data;

    //아이템 뷰를 저장하는 뷰홀더 클래스
    class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView busNum, busArea, busType;
        CheckBox busMark;

        ItemViewHolder(View view){
            super(view);

            busNum = view.findViewById(R.id.searchBusNumR);
            busArea = view.findViewById(R.id.searchBusAreaR);
            busType = view.findViewById(R.id.searchBusTypeR);
            busMark = view.findViewById(R.id.searchBusMarkR);
            //onclick 연결 필요
        }
    }

    //아이템 뷰를 저장하는 뷰홀더 클래스
    class FooterViewHolder extends RecyclerView.ViewHolder{
        TextView searchHistory;

        FooterViewHolder(View view) {
            super(view);
            searchHistory = view.findViewById(R.id.searchHistory);
        }
    }

    //생성자에서 데이터 리스트 객체 전달받음
    SearchRecyclerAdapter(ArrayList<SearchRecyclerItem> list){
        data = list;
    }

    //아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        View view;

        if(viewType == TYPE_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_search_bus, parent, false);
            holder = new SearchRecyclerAdapter.ItemViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_search_footer, parent, false);
            holder = new SearchRecyclerAdapter.FooterViewHolder(view);
        }

        return holder;
    }


    //position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_ITEM){
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            SearchRecyclerItem item = data.get(position);
            itemViewHolder.busNum.setText(item.getBusNum());
            itemViewHolder.busArea.setText(item.getBusArea());
            itemViewHolder.busType.setText(item.getBusType());
            itemViewHolder.busMark.setChecked(item.isBusMark());
        } else {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
        }
    }

    //전체 데이터 개수 리턴
    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == data.size())
            return TYPE_FOOTER;

        return TYPE_ITEM;
    }
}
