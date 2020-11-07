package com.bhsd.bustayo.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bhsd.bustayo.R;
import com.bhsd.bustayo.adapter.NoticeAdapter;
import com.bhsd.bustayo.dto.NoticeItem;

import java.util.ArrayList;

public class NoticeActivity extends AppCompatActivity {

    // JSONObject item;
    ArrayList<NoticeItem> items;

    RecyclerView recyclerView;
    NoticeAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        recyclerView = findViewById(R.id.notice_recycler);

        init();
    }

    void init() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        getItem();
        setAdapter();
    }

    void setAdapter() {
        adapter = new NoticeAdapter(items);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    void getItem() {
        items = new ArrayList<>();

        items.add(new NoticeItem("2020.11 버전 업데이트", "2020-11-07", "2020.11 버전이 업데이트 되었습니다.\n지금 바로 업데이트 해주세요!!\n\n작성자:이예진"));
        items.add(new NoticeItem("2020.10 버전 업데이트", "2020-10-07", "2020.10 버전이 업데이트 되었습니다.\n지금 바로 업데이트 해주세요!!\n\n작성자:이예진"));
        items.add(new NoticeItem("2020.9.2 버전 업데이트", "2020-09-27", "2020.9.2 버전이 업데이트 되었습니다.\n지금 바로 업데이트 해주세요!!\n\n작성자:이예진"));
        items.add(new NoticeItem("2020.9.1 버전 업데이트", "2020-09-17", "2020.9.1 버전이 업데이트 되었습니다.\n지금 바로 업데이트 해주세요!!\n\n작성자:이예진"));
        items.add(new NoticeItem("2020.9 버전 업데이트", "2020-09-07", "2020.9 버전이 업데이트 되었습니다.\n지금 바로 업데이트 해주세요!!\n\n작성자:이예진"));
        items.add(new NoticeItem("2020.8 버전 업데이트", "2020-08-07", "2020.8 버전이 업데이트 되었습니다.\n지금 바로 업데이트 해주세요!!\n\n작성자:이예진"));
        items.add(new NoticeItem("2020.7 버전 업데이트", "2020-07-07", "2020.7 버전이 업데이트 되었습니다.\n지금 바로 업데이트 해주세요!!\n\n작성자:이예진"));

    }
}
