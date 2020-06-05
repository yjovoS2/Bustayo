package com.bhsd.bustayo;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    Fragment nMapFragment;

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        nMapFragment = new NMapFragment();

        bottomNavigation = findViewById(R.id.bottom_navigation);

        //BottomNavigationView 메뉴 클릭
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.bottomNavi_bookmark:
                        //fragment 화면 전환 (즐겨찾기)
                        fragmentManager.beginTransaction().replace(R.id.fragment, nMapFragment).commit();
                        return true;
                    case R.id.bottomNavi_map:
                        //fragment 화면 전환 (지도)
                        fragmentManager.beginTransaction().replace(R.id.fragment, nMapFragment).commit();
                        return true;
                    case R.id.bottomNavi_alarm_off:
                        //하차알람 화면 연결 (하차알람)
                        fragmentManager.beginTransaction().replace(R.id.fragment, nMapFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }
}
