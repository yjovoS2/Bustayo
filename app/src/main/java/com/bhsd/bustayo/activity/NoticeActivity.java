package com.bhsd.bustayo.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bhsd.bustayo.R;
import com.bhsd.bustayo.adapter.NoticeAdapter;
import com.bhsd.bustayo.application.APIManager;
import com.bhsd.bustayo.dto.NoticeItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class NoticeActivity extends AppCompatActivity {

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

        findViewById(R.id.searchGoBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        new Thread() {
            @Override
            public void run() {
                getItem();
            }
        }.start();
        setAdapter();
    }

    void setAdapter() {
        adapter = new NoticeAdapter(items);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    void getItem() {
        items = new ArrayList<>();

//        items.add(new NoticeItem("2020.11 버전 업데이트", "2020-11-07", "2020.11 버전이 업데이트 되었습니다.\n지금 바로 업데이트 해주세요!!\n\n작성자:이예진"));
//        items.add(new NoticeItem("2020.10 버전 업데이트", "2020-10-07", "2020.10 버전이 업데이트 되었습니다.\n지금 바로 업데이트 해주세요!!\n\n작성자:이예진"));
//        items.add(new NoticeItem("2020.9.2 버전 업데이트", "2020-09-27", "2020.9.2 버전이 업데이트 되었습니다.\n지금 바로 업데이트 해주세요!!\n\n작성자:이예진"));
//        items.add(new NoticeItem("2020.9.1 버전 업데이트", "2020-09-17", "2020.9.1 버전이 업데이트 되었습니다.\n지금 바로 업데이트 해주세요!!\n\n작성자:이예진"));
//        items.add(new NoticeItem("2020.9 버전 업데이트", "2020-09-07", "2020.9 버전이 업데이트 되었습니다.\n지금 바로 업데이트 해주세요!!\n\n작성자:이예진"));
//        items.add(new NoticeItem("2020.8 버전 업데이트", "2020-08-07", "2020.8 버전이 업데이트 되었습니다.\n지금 바로 업데이트 해주세요!!\n\n작성자:이예진"));
//        items.add(new NoticeItem("2020.7 버전 업데이트", "2020-07-07", "2020.7 버전이 업데이트 되었습니다.\n지금 바로 업데이트 해주세요!!\n\n작성자:이예진"));

        String result = "";
        try {
            // Open the connection
            URL url = new URL(APIManager.SERVER_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();

            // Get the stream
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            // Set the result
            result = builder.toString();

            JSONObject base = new JSONObject(result);
            JSONArray jsonArray = (JSONArray) base.get("items");
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                NoticeItem item = new NoticeItem(obj.getString("title"), obj.getString("date"), obj.getString("content"));
                items.add(item);
            }
        } catch (Exception e) {
            Log.e("yj", ""+e);
        }

    }
}
