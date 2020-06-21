package com.bhsd.bustayo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bhsd.bustayo.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/////////////////////////////////////////////////////////////////
// 햄버거 메뉴 -> 불편신고 접수 리스트
//   - 불편신고 리스트 화면으로 내가 접수했던 불편신고 목록을 확인할 수 있음
//   - 불편신고 등록 화면으로 이동할 수 있음
/////////////////////////////////////////////////////////////////
public class ComplaintActivity extends AppCompatActivity {

    private RecyclerView         complaintList; //불편신고 접수 리스트
    private FloatingActionButton complaintAdd;  //불편신고 등록 화면으로 연결
    private ImageView            goBack;        //뒤로가기 버튼

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

        //TODO :: 내부 디비 연결해서 접수 목록 뿌려주기
    }

    /////////////
    // 이벤트 처리
    /////////////
    @Override
    protected void onResume() {
        super.onResume();

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
}
