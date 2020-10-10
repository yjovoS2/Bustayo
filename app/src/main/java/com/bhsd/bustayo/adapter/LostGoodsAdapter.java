package com.bhsd.bustayo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bhsd.bustayo.R;
import com.bhsd.bustayo.dto.LostGoodsInfo;

import java.util.ArrayList;

public class LostGoodsAdapter extends RecyclerView.Adapter<LostGoodsAdapter.LGViewHolder>{

    ArrayList<LostGoodsInfo> lostGoodsInfos;
    private LostGoodsAdapter.OnListItemSelected listener = null;

    public LostGoodsAdapter(ArrayList<LostGoodsInfo> lostGoodsInfos) {
        this.lostGoodsInfos = lostGoodsInfos;
    }
    // ↓ 리스트 아이템 선택 이벤트가 일어난경우 콜백 메서드를 커스텀 하기 위한 인터페이스
    public interface OnListItemSelected{
        void onItemSelected(View v, int position);
    }

    public void setOnListItemSelected(LostGoodsAdapter.OnListItemSelected listener){
        this.listener = listener;
    }
    // ↓ 뷰 홀더 클래스를 정의한 부분
    public class LGViewHolder extends RecyclerView.ViewHolder {
        ImageView lgImage, space;
        public TextView lgName, lgNum, lgPlace, lgDate;

        public LGViewHolder(@NonNull View itemView) {
            super(itemView);

            lgImage = itemView.findViewById(R.id.bus_color);
            lgName = itemView.findViewById(R.id.bus_num);
            lgNum = itemView.findViewById(R.id.bus_destination);
            lgPlace = itemView.findViewById(R.id.bus_first);
            lgDate = itemView.findViewById(R.id.bus_second);
            space = itemView.findViewById(R.id.bus_bookmark);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (listener != null) {
                            // ↓ 콜백 리스너 호출
                            listener.onItemSelected(v, position);
                        }
                    }
                }
            });
        }
    }


    @NonNull
    @Override
    // 뷰홀더 객체 생성하는 메서드 - 아이템뷰를 위한 뷰 홀더 객체 생성하여 리턴 ↓
    public LostGoodsAdapter.LGViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_bus_item, parent, false);

        return new LGViewHolder(v);
    }

    @Override
    // 뷰홀더가 필요한 위치에 할당될 때 호출되는 메서드 - position 에 해당하는 데이터를 뷰 홀더에 바인딩 ↓
    public void onBindViewHolder(@NonNull LostGoodsAdapter.LGViewHolder holder, int position) {
        holder.space.setVisibility(View.GONE);
        holder.lgDate.setText(lostGoodsInfos.get(position).getLgDate());
        holder.lgDate.setTextSize(12);
        holder.lgPlace.setText(lostGoodsInfos.get(position).getLgPlace());
        holder.lgPlace.setTextSize(12);
        holder.lgNum.setText(lostGoodsInfos.get(position).getLgNum());
        holder.lgNum.setTextSize(12);
        holder.lgImage.setImageResource(R.drawable.ic_bus);
        holder.lgName.setText(lostGoodsInfos.get(position).getLgName());
    }

    @Override
    // 전체 아이템 개수 리턴 ↓
    public int getItemCount() {
        return lostGoodsInfos.size();
    }
}
