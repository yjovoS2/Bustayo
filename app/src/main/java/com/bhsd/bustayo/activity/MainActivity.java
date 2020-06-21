package com.bhsd.bustayo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bhsd.bustayo.R;
import com.bhsd.bustayo.activity.ComplaintActivity;
import com.bhsd.bustayo.activity.LoginActivity;
import com.bhsd.bustayo.activity.LostGoodsActivity;
import com.bhsd.bustayo.activity.SearchActivity;
import com.bhsd.bustayo.fragment.BookmarkFragment;
import com.bhsd.bustayo.fragment.GetOffAlarmFragment;
import com.bhsd.bustayo.fragment.NMapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    //플로팅 액션 버튼
    public FloatingActionButton fab;
    //툴바 관련
    TextView search, header;
    DrawerLayout drawerLayout;
    NavigationView drawer;
    ImageView drawerHandle;

    //프래그먼트 관련
    FragmentManager fragmentManager;
    BookmarkFragment bmFragment;
    Fragment nMapFragment;
    Fragment offAlarmFragment;

    //바텀네비게이션 관련
    BottomNavigationView bottomNavigation;

    //MainActivity의 context
    public static Context context_main;

    //로그인상태인지 확인하는 변수 - 테스트용 나중엔 디비에서 가지고오자
    boolean islogin;

    //초기화 작업
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context_main = this;

        //필요한 뷰 인플레이션
        search = findViewById(R.id.search);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawer = findViewById(R.id.drawer);
        drawerHandle = findViewById(R.id.drawerHandle);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        //프래그먼트 생성
        bmFragment = new BookmarkFragment();
        nMapFragment = new NMapFragment();
        offAlarmFragment = new GetOffAlarmFragment();

        bottomNavigation = findViewById(R.id.bottomNavigation);
        //로그인 테스트용
        islogin = false;
        //프래그먼트 추가 및 초기화면 설정
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragment, bmFragment, "bookmark").commit();
        fragmentManager.beginTransaction().add(R.id.fragment, nMapFragment, "nMap").commit();
        fragmentManager.beginTransaction().add(R.id.fragment, offAlarmFragment, "offAlarm").commit();
        header = drawer.getHeaderView(0).findViewById(R.id.drawerHeaderName);
        showFragment(bmFragment);
    }

    //이벤트 처리
    @Override
    protected void onResume() {
        super.onResume();

        //햄버거 버튼
        drawerHandle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawerLayout.isDrawerOpen(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else
                    drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //검색 화면 실행
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);

                overridePendingTransition(R.anim.left_mov, R.anim.not_mov); //애니메이션 설정 (왼쪽으로 슬라이딩)
            }
        });

        /*
         * 햄버거 버튼 -> 로그인 영역
         * 로그인 화면 실행
         */
        drawer.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(islogin){//이미 로그인된경우
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("로그아웃");
                    builder.setMessage("로그아웃 하시겠습니까?");

                    builder.setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            islogin = false;
                            header.setText("로그인");
                        }
                    });

                    builder.setNegativeButton("돌아가기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //창 닫기
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else{//로그인 하지 않은 경우
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivityForResult(intent,0);
                }

            }
        });

        /*
         * 햄버거 버튼 -> 아이템 선택
         * 각각의 아이템에 해당하는 화면 실행
         */
        drawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    //불편신고 접수
                    case R.id.drawerComplaint: {
                        Intent intent = new Intent(getApplicationContext(), ComplaintActivity.class);
                        startActivity(intent);

                        return true;
                    }
                    //분실물 현황
                    case R.id.drawerLostList: {
                        Intent intent = new Intent(getApplicationContext(), LostGoodsActivity.class);
                        startActivity(intent);
                        return true;
                    }
                    //지역설정 (2차 개발)
                    case R.id.drawerSetLocation: Toast.makeText(getApplicationContext(), "지역설정", Toast.LENGTH_SHORT).show(); return true;
                    //공지사항 (2차 개발)
                    case R.id.drawerNotice: Toast.makeText(getApplicationContext(), "공지사항", Toast.LENGTH_SHORT).show(); return true;
                    //설정 (2차 개발)
                    case R.id.drawerSetting: Toast.makeText(getApplicationContext(), "설정", Toast.LENGTH_SHORT).show(); return true;
                }
                return false;
            }
        });

        //바텀네비게이션 메뉴 선택
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    //즐겨찾기
                    case R.id.bottomNavBookmark:
                        //FloatingActionButton은 여기서만필요
                        return showFragment(bmFragment);
                    //주변 정류장
                    case R.id.bottomNavMap:
                        fab.hide();
                        return showFragment(nMapFragment);
                    //하차알림
                    case R.id.bottomNavAlarmOff:
                        fab.hide();
                        return showFragment(offAlarmFragment);
                }
                return false;
            }
        });
    }

    //특정 프래그먼트만 보이도록 하는 메서드
    private boolean showFragment(Fragment fragment){
        //프래그먼트 기존 데이터 저장하기 위해 show(), hide() 메서드 사용
        fragmentManager.beginTransaction().hide(bmFragment).commit();
        fragmentManager.beginTransaction().hide(nMapFragment).commit();
        fragmentManager.beginTransaction().hide(offAlarmFragment).commit();
        fragmentManager.beginTransaction().show(fragment).commit();

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if(resultCode==RESULT_OK){
                String userName = data.getStringExtra("userID");
                islogin = data.getBooleanExtra("isLogin",false);
                header.setText(userName);}
        }
    }
}
