package com.bhsd.bustayo.adapter;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bhsd.bustayo.R;
import com.bhsd.bustayo.activity.SearchActivity;
import com.bhsd.bustayo.activity.StationActivity;
import com.bhsd.bustayo.activity.StationListActivity;
import com.bhsd.bustayo.database.TestDB;
import com.bhsd.bustayo.dto.SearchRecyclerItem;
import com.bhsd.bustayo.fragment.SearchBusFragment;

import java.util.ArrayList;

////////////////////////////////////////////////
// 검색 화면에서 버스, 정류장에 대한 리사이클러뷰 어댑터
//   - 버스, 정류장 각각의 히스토리와 검색 리스트 출력
////////////////////////////////////////////////
public class SearchRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /////////////////////////////////////////////////////////////////
    // TYPE_ITEM     일반 아이템
    // TYPE_FOOTER   히스토리 삭제 버튼
    // data          전달받은 데이터 리스트
    // type          버스인지 정류장인지 여부 (true : 버스, false : 정류장)
    /////////////////////////////////////////////////////////////////
    private final int TYPE_ITEM   = 0;
    private final int TYPE_FOOTER = 1;

    private ArrayList<SearchRecyclerItem> data;
    private boolean                       type;
    private Context                       context;
    private TestDB                        DBHelper;

    ///////////////////////////////
    // 일반 아이템에 대한 뷰홀더 클래스
    ///////////////////////////////
    class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView historyTitle, historySub1, historySub2;

        ItemViewHolder(View view){
            super(view);

            //뷰홀더에 사용되는 뷰 인플레이션
            historyTitle = view.findViewById(R.id.historyTitle);
            historySub1  = view.findViewById(R.id.historySub1);
            historySub2  = view.findViewById(R.id.historySub2);
        }
    }

    ///////////////////////////////////////////////////
    // 검색 히스토리 화면에서 삭제버튼을 위한 푸터 뷰홀더 클래스
    ///////////////////////////////////////////////////
    class FooterViewHolder extends RecyclerView.ViewHolder{
        TextView searchDelete;

        FooterViewHolder(View view) {
            super(view);

            //푸터에 사용되는 뷰 인플레이션 및 텍스트 설정
            searchDelete = view.findViewById(R.id.searchDelete);

            if(type)
                searchDelete.setText(R.string.bus_history);
            else
                searchDelete.setText(R.string.station_history);
        }
    }

    ///////////////////////////////
    // 전달받은 데이터 리스트, 타입 저장
    ///////////////////////////////
    public SearchRecyclerAdapter(ArrayList<SearchRecyclerItem> data, boolean type, Context context){
        this.data    = data;
        this.type    = type;
        this.context = context;

        DBHelper     = new TestDB(context);
    }

    ///////////////////////////////////////////
    // 아이템 항목이 일반 아이템인지 푸터인지 확인
    //   - 일반 아이템인 경우 아이템 뷰홀더 객체 생성
    //   - 푸터인 경우 푸터 뷰홀더 객체 생성
    ///////////////////////////////////////////
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        View view;

        if(viewType == TYPE_ITEM) {
            view   = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_search_item, parent, false);
            holder = new SearchRecyclerAdapter.ItemViewHolder(view);
        } else {
            view   = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_search_footer, parent, false);
            holder = new SearchRecyclerAdapter.FooterViewHolder(view);
        }

        return holder;
    }

    ////////////////////////////////////////////////////
    // 현재 아이템이 일반 아이템인지 푸터인지 확인 후 데이터 설정
    ////////////////////////////////////////////////////
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_ITEM){
            final ItemViewHolder     itemViewHolder = (ItemViewHolder) holder;
            final SearchRecyclerItem item           = data.get(position);

            itemViewHolder.historyTitle.setText(item.getHistoryTitle());
            itemViewHolder.historySub1.setText(item.getHistorySub1());
            itemViewHolder.historySub2.setText(item.getHistorySub2());

            ///////////////////////////////////////////////////////////////////
            // type true  : 버스 아이템 클릭 시 버스 노선 화면으로 이동
            // type false : 정류장 아이템 클릭 시 정류장 화면으로 이동
            // 화면이 이동되면서 선택한 아이템이 히스토리 테이블에 등록
            //   - 버스 히스토리 테이블   : 버스ID, 버스노선번호, 버스 타입, 기점, 종점
            //   - 정류장 히스토리 테이블 : 정류장ID, 정류장명, 정류장 고유번호
            ///////////////////////////////////////////////////////////////////
            if(type){
                itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        char routeType;

                        switch(item.getHistorySub1()) {
                            case "공용버스": routeType = '0'; break;
                            case "공항버스": routeType = '1'; break;
                            case "마을버스": routeType = '2'; break;
                            case "간선버스": routeType = '3'; break;
                            case "지선버스": routeType = '4'; break;
                            case "순환버스": routeType = '5'; break;
                            case "광역버스": routeType = '6'; break;
                            case "인천버스": routeType = '7'; break;
                            case "경기버스": routeType = '8'; break;
                            case "폐지버스": routeType = '9'; break;
                            default: routeType = '?'; break;
                        }

                        SQLiteDatabase dbSQL = DBHelper.getWritableDatabase();
                        dbSQL.execSQL("INSERT OR REPLACE INTO busHistoryTB VALUES(?, ?, ?, ?, ?, CURRENT_TIMESTAMP)",
                                new Object[] {item.getHistoryId(), item.getHistoryTitle(), routeType, item.getHistorySub2().split("→")[0], item.getHistorySub2().split("→")[1]});
                        dbSQL.close();

                        Intent intent = new Intent(context, StationListActivity.class);
                        intent.putExtra("busRouteNm", item.getHistoryTitle());

                        context.startActivity(intent);
                    }
                });
            } else {
                itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SQLiteDatabase dbSQL = DBHelper.getWritableDatabase();
                        dbSQL.execSQL("INSERT OR REPLACE INTO stationHistoryTB VALUES(?, ?, ?, ?, CURRENT_TIMESTAMP)",
                                new Object[] {item.getHistoryId(), item.getHistoryTitle(), item.getHistorySub1(), item.getHistorySub2().replace(" 방면", "")});
                        dbSQL.close();

                        Intent intent = new Intent(context, StationActivity.class);
                        intent.putExtra("arsId", item.getHistorySub1());
                        intent.putExtra("stationNm", item.getHistoryTitle());

                        context.startActivity(intent);
                    }
                });
            }
        }
        //////////////////////////////////////////
        // 푸터 부분
        //   - 버스, 정류장 검색 기록 삭제 버튼 클릭 시
        //     해당하는 모든 검색 기록 삭제
        //////////////////////////////////////////
        else {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;

            footerViewHolder.searchDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    if(type){
                        dialog.setMessage("버스 검색 기록을 삭제하시겠습니까?");
                        dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {@Override public void onClick(DialogInterface dialog, int which) {}});
                        dialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SQLiteDatabase dbSQL = DBHelper.getWritableDatabase();
                                dbSQL.execSQL("DELETE FROM busHistoryTB");
                                dbSQL.close();

                                //프래그먼트 화면 갱신
                                ((SearchActivity)context).refresh("bus");
                            }
                        });
                    } else {
                        dialog.setMessage("정류장 검색 기록을 삭제하시겠습니까?");
                        dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {@Override public void onClick(DialogInterface dialog, int which) {}});
                        dialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SQLiteDatabase dbSQL = DBHelper.getWritableDatabase();
                                dbSQL.execSQL("DELETE FROM stationHistoryTB");
                                dbSQL.close();

                                //프래그먼트 화면 갱신
                                ((SearchActivity)context).refresh("station");
                            }
                        });
                    }
                    dialog.show();
                }
            });
        }
    }

    ///////////////////////////////////////////////////
    // 전체 데이터 개수 리턴
    //   - 검색 히스토리 상태일 경우 푸터를 추가하여 + 1
    //   - API를 호출하여 받아온 데이터일 경우 푸터가 필요없음
    ///////////////////////////////////////////////////
    @Override
    public int getItemCount() {
        return (SearchActivity.searchText.getText().length() == 0 && data.size() != 0) ? data.size() + 1 : data.size();
    }

    ////////////////////////////////////////////////////
    // 현재 아이템 위치가 일반 아이템인지 푸터인지 확인하는 메서드
    ////////////////////////////////////////////////////
    @Override
    public int getItemViewType(int position) {
        return (position == data.size()) ? TYPE_FOOTER : TYPE_ITEM;
    }
}
