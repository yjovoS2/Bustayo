package com.bhsd.bustayo.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

/////////////////////////////////////
// 검색 화면에서 뷰페이저에 연결되는 어댑터
// context    탭 타이틀 설정 시 사용
// fragments  탭 상에 등록할 프래그먼트
// titles     탭 타이틀로 설정할 텍스트
/////////////////////////////////////
public class SearchPagerAdapter extends FragmentPagerAdapter {

    private Context             context;
    private ArrayList<Fragment> fragments;
    private ArrayList<Integer>  titles;

    public SearchPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
        fragments    = new ArrayList<>();
        titles       = new ArrayList<>();
    }

    //////////////////////////
    // 프래그먼트, 탭 타이틀 등록
    //////////////////////////
    public void addFragment(Fragment fragment, int titleID){
        fragments.add(fragment);
        titles.add(titleID);
    }

    ////////////////////////////////////////////
    // 버스, 정류장 탭 클릭 시 해당하는 프래그먼트 리턴
    ////////////////////////////////////////////
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    //////////////
    // 탭 개수 반환
    //////////////
    @Override
    public int getCount() {
        return 2;
    }

    ///////////////
    // 탭 타이틀 설정
    ///////////////
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return context.getString(titles.get(position));
    }
}
