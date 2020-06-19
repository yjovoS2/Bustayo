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
    ///////////////////////////////
    //TYPE_ITEM : 일반 아이템
    //TYPE_FOOTER : 히스토리 삭제 버튼
    //data : 전달받은 데이터 리스트
    private final int TYPE_ITEM = 0;
    private final int TYPE_FOOTER = 1;
    private ArrayList<SearchRecyclerItem> data;

    class ItemViewHolder extends RecyclerView.ViewHolder{
        //일반 아이템 뷰홀더
        TextView busNum, busArea, busType;
        CheckBox busMark;

        ItemViewHolder(View view){
            super(view);
            //ViewHolder에 사용되는 뷰 인플레이션
            busNum = view.findViewById(R.id.searchBusNumR);
            busArea = view.findViewById(R.id.searchBusAreaR);
            busType = view.findViewById(R.id.searchBusTypeR);
            busMark = view.findViewById(R.id.searchBusMarkR);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder{
        //히스토리 삭제 버튼 뷰홀더
        TextView searchHistory;

        FooterViewHolder(View view) {
            super(view);
            searchHistory = view.findViewById(R.id.searchHistory);
        }
    }

    SearchRecyclerAdapter(ArrayList<SearchRecyclerItem> list){
        //전달받은 데이터 리스트 저장
        data = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ///////////////////////////////////////
        //아이템 항목이 일반 아이템인지 푸터인지 확인
        //일반 아이템인 경우 아이템 뷰홀더 객체 생성
        //푸터인 경우 푸터 뷰홀더 객체 생성
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


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //////////////////////////////////////
        //현재 아이템이 일반 아이템인지 푸터인지 확인
        //확인 후 각 데이터 연결
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

    @Override
    public int getItemCount() {
        //전체 데이터 개수 리턴 (푸터 때문에 +1)
        return data.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        //현재 아이템 위치가 일반 아이템인지 푸터인지 확인하는 메서드
        if(position == data.size())
            return TYPE_FOOTER;

        return TYPE_ITEM;
    }
}
