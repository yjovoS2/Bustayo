package com.bhsd.bustayo.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bhsd.bustayo.R;
import com.bhsd.bustayo.dto.LostGoodsInfo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
    class LGViewHolder extends RecyclerView.ViewHolder {
        ImageView lgImage;
        TextView lgTitle, lgNum, lgPlace, lgDate;

        LGViewHolder(@NonNull View itemView) {
            super(itemView);

            lgImage = itemView.findViewById(R.id.lost_image);
            lgTitle = itemView.findViewById(R.id.lost_title);
            lgNum = itemView.findViewById(R.id.lost_number);    // 분실물 고유번호
            lgPlace = itemView.findViewById(R.id.lost_bus);
            lgDate = itemView.findViewById(R.id.lost_date);

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

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_lost_item, parent, false);

        return new LGViewHolder(v);
    }

    @Override
    // 뷰홀더가 필요한 위치에 할당될 때 호출되는 메서드 - position 에 해당하는 데이터를 뷰 홀더에 바인딩 ↓
    public void onBindViewHolder(@NonNull LostGoodsAdapter.LGViewHolder holder, int position) {
        String imageURL = lostGoodsInfos.get(position).getLgImage();
        holder.lgDate.setText(lostGoodsInfos.get(position).getLgDate());
        holder.lgPlace.setText(lostGoodsInfos.get(position).getLgPlace());
        holder.lgNum.setText(lostGoodsInfos.get(position).getLgNum());
        if(imageURL.equals("")) {
            holder.lgImage.setImageResource(R.drawable.ic_no_image);
        } else {
            new DownloadFilesTask(holder.lgImage).execute(imageURL);
        }

        //holder.lgImage.setImageResource(R.drawable.ic_no_image);

        holder.lgTitle.setText(lostGoodsInfos.get(position).getLgTitle());
    }

    @Override
    // 전체 아이템 개수 리턴 ↓
    public int getItemCount() {
        return lostGoodsInfos.size();
    }

    private class DownloadFilesTask extends AsyncTask<String,Void, Bitmap> {
        ImageView image;
        DownloadFilesTask(ImageView image) {
            this.image = image;
        }
        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bmp = null;
            try {
                String img_url = strings[0]; //url of the image
                URL url = new URL(img_url);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(Bitmap result) {
            // doInBackground 에서 받아온 total 값 사용 장소
            image.setImageBitmap(result);
        }
    }
}


