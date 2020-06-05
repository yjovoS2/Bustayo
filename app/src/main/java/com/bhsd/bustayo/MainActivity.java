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
                        fragmentManager.beginTransaction().replace(R.id.fragment, nMapFragment).commit();
                        return true;
                    case R.id.bottomNavi_map:
                        fragmentManager.beginTransaction().replace(R.id.fragment, nMapFragment).commit();
                        return true;
                    case R.id.bottomNavi_alarm_off:
                        fragmentManager.beginTransaction().replace(R.id.fragment, nMapFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }
}
