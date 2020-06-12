package com.bhsd.bustayo;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class SearchActivity extends AppCompatActivity {

    EditText searchText;    //검색창
    ImageView searchGoBack; //뒤로가기
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
        searchGoBack = findViewById(R.id.searchGoBack);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        //프래그먼트 연결 & 어댑터 설정
        searchPagerAdapter = new SearchPagerAdapter(this, getSupportFragmentManager());
        searchPagerAdapter.addFragment(new SearchBusFragment(), R.string.bus);
        searchPagerAdapter.addFragment(new SearchStationFragment(), R.string.station);

        //뷰페이저에 어댑터 연결
        viewPager.setAdapter(searchPagerAdapter);

        //뷰페이저, 탭 연동
        tabLayout.setupWithViewPager(viewPager);

        //탭 아이콘 설정
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_bus);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_sation_search);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //버스, 정류장 탭 클릭 시
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //버스 클릭
                if(tab.getPosition() == 0){
                    searchText.setHint(R.string.bus_search);
                    searchText.setInputType(InputType.TYPE_CLASS_PHONE);
                }
                // 정류장 클릭
                else if(tab.getPosition() == 1){
                    searchText.setHint(R.string.station_search);
                    searchText.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        //뒤로가기 클릭 (검색 -> 메인)
        searchGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

                overridePendingTransition(R.anim.not_mov, R.anim.right_mov);
            }
        });

        //test
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0){
                    Toast.makeText(getApplicationContext(), "끝", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
