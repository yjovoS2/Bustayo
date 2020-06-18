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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    //툴바 관련
    TextView search, header;
    DrawerLayout drawerLayout;
    NavigationView drawer;
    ImageView drawerHandle;

    //프래그먼트 관련
    Fragment nMapFragment;
    BookmarkFragment bmFragment;
    Fragment offAlarmFragment;

    //바텀네비게이션 관련
    BottomNavigationView bottomNavigation;

    //MainActivity의 context
    public static Context context_main;

    //플로팅 액션 버튼
    FloatingActionButton fab;
    //로그인상태인지 확인하는 변수 - 테스트용 나중엔 디비에서 가지고오자
    boolean islogin;

    //초기화 작업
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context_main = this;

        search = findViewById(R.id.search);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawer = findViewById(R.id.drawer);
        header = drawer.getHeaderView(0).findViewById(R.id.drawerHeaderName);
        drawerHandle = findViewById(R.id.drawerHandle);

        nMapFragment = new NMapFragment();
        bmFragment = new BookmarkFragment();
        offAlarmFragment = new GetOffAlarmFragment();

        bottomNavigation = findViewById(R.id.bottomNavigation);
        //로그인 테스트용
        islogin = false;
    }

    //리스너 연결
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
                //애니메이션 설정 (왼쪽으로 슬라이딩)
                overridePendingTransition(R.anim.left_mov, R.anim.not_mov);
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
                    //불편신고
                    case R.id.drawerComplaint:
                        Intent intent = new Intent(getApplicationContext(), ComplaintActivity.class);
                        startActivity(intent);

                        //애니메이션 설정 (왼쪽으로 슬라이딩)
                        //overridePendingTransition(R.anim.left_mov, R.anim.not_mov);

                        return true;
                    //분실물 현황
                    case R.id.drawerLostList:
                        Toast.makeText(MainActivity.this, "drawerLostList", Toast.LENGTH_SHORT).show();
                        return true;
                    //지역설정 (2차)
                    case R.id.drawerSetLocation:
                        Toast.makeText(getApplicationContext(), "지역설정", Toast.LENGTH_SHORT).show();
                        return true;
                    //공지사항 (2차)
                    case R.id.drawerNotice:
                        Toast.makeText(getApplicationContext(), "공지사항", Toast.LENGTH_SHORT).show();
                        return true;
                    //설정 (2차)
                    case R.id.drawerSetting:
                        Toast.makeText(getApplicationContext(), "설정", Toast.LENGTH_SHORT).show();
                        return true;
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
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, bmFragment).addToBackStack(null).commit();
                        //FloatingActionButton은 여기서만필요
                        return true;
                    //주변 정류장
                    case R.id.bottomNavMap:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, nMapFragment).addToBackStack(null).commit();
                        fab.hide();
                        return true;
                    //하차알림
                    case R.id.bottomNavAlarmOff:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, offAlarmFragment).addToBackStack(null).commit();
                        fab.hide();
                        return true;
                }
                return false;
            }
        });
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
