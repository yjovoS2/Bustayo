package com.bhsd.bustayo.activity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bhsd.bustayo.R;
import com.bhsd.bustayo.adapter.SearchPagerAdapter;
import com.bhsd.bustayo.fragment.SearchBusFragment;
import com.bhsd.bustayo.fragment.SearchStationFragment;
import com.google.android.material.tabs.TabLayout;

/////////////////////////////////////////////////////////////////////////
// 메인 화면 -> 검색 화면
//   - 내가 검색했던 버스, 정류장 히스토리를 보여줌
//   - 상단에 있는 EditText에 값을 입력 시 API 호출을 통해 버스, 정류장 검색
/////////////////////////////////////////////////////////////////////////
public class SearchActivity extends AppCompatActivity {

    private ImageView                     searchGoBack;       //뒤로가기 버튼
    private TabLayout                     tabLayout;          //버스, 정류장 탭
    private ViewPager                     viewPager;          //버스, 정류장 목록 출력
    private SearchPagerAdapter            searchPagerAdapter; //뷰페이저 어댑터

    private SearchBusFragment             busFragment;        //버스 프래그먼트
    private SearchStationFragment         stationFragment;    //정류장 프래그먼트

    public static EditText                searchText;         //검색창

    /////////////
    // 초기화 작업
    /////////////
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //필요한 뷰 인플레이션
        searchText          = findViewById(R.id.searchText);
        tabLayout           = findViewById(R.id.tabLayout);
        viewPager           = findViewById(R.id.viewPager);
        searchGoBack        = findViewById(R.id.searchGoBack);

        //필요한 객체 생성
        searchPagerAdapter  = new SearchPagerAdapter(this, getSupportFragmentManager());
        busFragment         = new SearchBusFragment();
        stationFragment     = new SearchStationFragment();

        //뷰페이저에 프래그먼트 등록, 어댑터 연결
        searchPagerAdapter.addFragment(busFragment, R.string.bus);
        searchPagerAdapter.addFragment(stationFragment, R.string.station);
        viewPager.setAdapter(searchPagerAdapter);

        //뷰페이저-탭 연결, 탭 아이콘 설정
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_bus);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_sation_search);
    }

    /////////////
    // 이벤트 처리
    /////////////
    @Override
    protected void onResume() {
        super.onResume();

        ////////////////////////////////////////////////
        // 버스, 정류장 탭 클릭 시
        //   - 버스    : 키패드 입력 방식을 숫자 입력으로 변경
        //   - 정류장  : 키패드 입력 방식을 기본 입력으로 변경
        ////////////////////////////////////////////////
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //버스 클릭
                if(tab.getPosition() == 0){
                    searchText.setHint(R.string.bus_search);
                    searchText.setText("");
                    searchText.setInputType(InputType.TYPE_CLASS_PHONE);
                }
                // 정류장 클릭
                else if(tab.getPosition() == 1){
                    searchText.setHint(R.string.station_search);
                    searchText.setText("");
                    searchText.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        /////////////////////////////////////
        // 뒤로가기 버튼 클릭 시 메인 화면으로 이동
        //   - 애니메이션 설정 (오른쪽 슬라이딩)
        /////////////////////////////////////
        searchGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.not_mov, R.anim.right_mov);
            }
        });
    }
}
