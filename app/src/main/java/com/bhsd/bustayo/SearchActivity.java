package com.bhsd.bustayo;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class SearchActivity extends AppCompatActivity {

    EditText searchText;    //검색창
    ImageView searchGoBack; //뒤로가기 버튼
    TabLayout tabLayout;    //버스, 정류장 탭
    ViewPager viewPager;    //버스, 정류장 목록 출력

    SearchPagerAdapter searchPagerAdapter;

    //초기화 작업
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //필요한 뷰 인플레이션
        searchText = findViewById(R.id.searchText);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        searchGoBack = findViewById(R.id.searchGoBack);

        //어댑터 설정 & 프래그먼트 연결
        searchPagerAdapter = new SearchPagerAdapter(this, getSupportFragmentManager());
        searchPagerAdapter.addFragment(new SearchBusFragment(), R.string.bus);
        searchPagerAdapter.addFragment(new SearchStationFragment(), R.string.station);

        viewPager.setAdapter(searchPagerAdapter); //뷰페이저에 어댑터 연결
        tabLayout.setupWithViewPager(viewPager);  //뷰페이저, 탭 연동

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

        //뒤로가기 버튼 클릭 시
        searchGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.not_mov, R.anim.right_mov); //애니메이션 설정 (오른쪽으로 슬라이딩)
            }
        });

        //검색창에 변화가 있는 경우
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //버스 API 연결해서 버스 목록 출력
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

                searchPagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0){
                    //버스 히스토리 목록 출력
                    Toast.makeText(getApplicationContext(), "끝", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        });
    }
}
