package com.bhsd.bustayo;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class SearchActivity extends MainActivity {

    EditText searchText; //검색창
    TabLayout tabLayout;
    ViewPager viewPager;

    //뷰페이저에 연결할 어댑터
    SearchPagerAdapter searchPagerAdapter;

    //초기화 작업
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchText = findViewById(R.id.searchText);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        //탭 아이콘 설정
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_bus);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_sation_search);

        //프래그먼트 연결 & 어댑터 설정
        searchPagerAdapter = new SearchPagerAdapter(this, getSupportFragmentManager());
        searchPagerAdapter.addFragment(new SearchBusFragment(), R.string.bus);
        searchPagerAdapter.addFragment(new SearchStationFragment(), R.string.station);

        //뷰페이저에 어댑터 연결
        viewPager.setAdapter(searchPagerAdapter);

        //뷰페이저, 탭 연동
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){
                    searchText.setHint(R.string.bus_search);
                }
                else {
                    searchText.setHint(R.string.station_search);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        findViewById(R.id.searchGoBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

                overridePendingTransition(R.anim.not_mov, R.anim.right_mov);
            }
        });
    }
}
