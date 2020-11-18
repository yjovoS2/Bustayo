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
                items = new ArrayList<>();
                synchronized (items) {
                    getItem();
                }
                synchronized (items) {
                    setAdapter();
                }
            }
        }.start();
    }

    void setAdapter() {
        adapter = new NoticeAdapter(items);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    void getItem() {
        String result_msg = "";
        try {
            // Open the connection
            URL url = new URL(APIManager.GET_NOTICE_LIST);
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
            result_msg = builder.toString();

            JSONObject base = new JSONObject(result_msg);
            JSONObject result = (JSONObject) base.get("result");
            JSONArray array = (JSONArray) result.get("rows");
            for(int i = 0; i < array.length(); i++) {
                JSONObject obj = (JSONObject) array.get(i);
                NoticeItem item = new NoticeItem(obj.getString("title"), obj.getString("date").split("T")[0], obj.getString("content"));
                items.add(item);
            }
        } catch (Exception e) {
            Log.e("yj", ""+e);
        }

    }
}
