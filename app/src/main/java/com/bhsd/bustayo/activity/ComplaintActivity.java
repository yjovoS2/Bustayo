package com.bhsd.bustayo.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bhsd.bustayo.R;
import com.bhsd.bustayo.adapter.ComplaintRecyclerAdapter;
import com.bhsd.bustayo.database.ApplicationDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/////////////////////////////////////////////////////////////////
// 햄버거 메뉴 -> 불편신고 접수 리스트
//   - 불편신고 리스트 화면으로 내가 접수했던 불편신고 목록을 확인할 수 있음
//   - 불편신고 등록 화면으로 이동할 수 있음
/////////////////////////////////////////////////////////////////
public class ComplaintActivity extends AppCompatActivity {

    private RecyclerView               complaintList; //불편신고 접수 리스트
    private FloatingActionButton       complaintAdd;  //불편신고 등록 화면으로 연결
    private ImageView                  goBack;        //뒤로가기 버튼
    private ApplicationDB DBHelper;      //DB 연결 도구
    private ComplaintRecyclerAdapter   adapter;
    private ArrayList<String[]>        data;

    /////////////////
    // 초기화 작업
    /////////////////
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);

        //필요한 뷰 인플레이션
        complaintList = findViewById(R.id.complaintList);
        complaintAdd  = findViewById(R.id.complaintAdd);
        goBack        = findViewById(R.id.goBack);

        //DB 연결 및 어댑터 생성
        DBHelper      = new ApplicationDB(this);
        data          = new ArrayList<>();
        adapter       = new ComplaintRecyclerAdapter(data);

        //어댑터 연결
        complaintList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        complaintList.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                int complaintId = Integer.parseInt(data.get(position)[0]);

                SQLiteDatabase dbSQL = DBHelper.getWritableDatabase();
                dbSQL.execSQL("DELETE FROM complaintsTB WHERE complaintId = " + complaintId);
                dbSQL.close();

                data.remove(position);
                adapter.notifyItemRemoved(position);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(complaintList);
    }

    /////////////
    // 이벤트 처리
    /////////////
    @Override
    protected void onResume() {
        super.onResume();

        ////////////////////////////////////////////////
        // 불편신고 테이블에서 신고 내역 리스트를 가져오는 메서드
        ////////////////////////////////////////////////
        useDB();

        ////////////////////////////////////
        // 불편신고를 접수할 수 있는 화면으로 이동
        ////////////////////////////////////
        complaintAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ComplaintAddActivity.class);
                startActivity(intent);
            }
        });

        ////////////////////////////////////////
        // 뒤로가기 버튼으로 클릭 시 메인 화면으로 이동
        ////////////////////////////////////////
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    ////////////////////////////////////////////////
    // 불편신고 테이블에서 신고 내역 리스트를 가져오는 메서드
    ////////////////////////////////////////////////
    private void useDB(){
        data.clear();

        SQLiteDatabase dbSQL   = DBHelper.getReadableDatabase();
        Cursor cursor          = dbSQL.rawQuery("SELECT * FROM complaintsTB ORDER BY complaintId desc;", null);

        while(cursor.moveToNext())
            data.add(new String[]{cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3) + "년 " + cursor.getString(4) + "월 " + cursor.getString(5) + "일 접수내역", cursor.getString(6), cursor.getString(7)});

        cursor.close();
        dbSQL.close();

        adapter.notifyDataSetChanged();
    }
}
