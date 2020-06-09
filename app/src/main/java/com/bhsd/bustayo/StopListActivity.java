package com.bhsd.bustayo;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class StopListActivity extends AppCompatActivity {

    RecyclerView bus_list;
    StopListAdapter bus_adapter;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);

        bus_list = findViewById(R.id.bus_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        bus_list.setLayoutManager(linearLayoutManager);

        bus_adapter = new StopListAdapter();
        bus_list.setAdapter(bus_adapter);

        getData();

        bus_adapter.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    void getData() {
        List<String> bus_name = Arrays.asList( "정류장 1", "정류장 2" );
        List<Integer> previous_color = Arrays.asList( R.color.invisible, R.color.busy_full );
        List<Integer> next_color = Arrays.asList( R.color.busy_empty, R.color.invisible );

        for(int i = 0; i < bus_name.size(); i++) {
            String name = bus_name.get(i);
            int previous = getColor(previous_color.get(i)), next = getColor(next_color.get(i));
            // 첫번째 : 앞 도로 투명, 마지막 : 뒤에 도로 투명
            if(i == 0) {
                previous = getColor(R.color.invisible);
            }
            if (i == bus_name.size() - 1) {
                next = getColor(R.color.invisible);
            }
            StopListItem item = new StopListItem(name, previous, next);

            bus_adapter.addItem(item);
        }
    }
}
