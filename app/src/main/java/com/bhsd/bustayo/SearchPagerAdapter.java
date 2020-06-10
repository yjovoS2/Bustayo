package com.bhsd.bustayo;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

//뷰페이저 어댑터
public class SearchPagerAdapter extends FragmentPagerAdapter {

    private Context _context;
    private ArrayList<Fragment> tabFragments; //사용할 프래그먼트 리스트
    private ArrayList<Integer> tabTitles; //탭 타이틀로 설정할 ID 리스트

    public SearchPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        _context = context;
        tabFragments = new ArrayList<>();
        tabTitles = new ArrayList<>();
    }

    //프래그먼트, 탭 타이틀 추가
    public void addFragment(Fragment fragment, int titleID){
        tabFragments.add(fragment);
        tabTitles.add(titleID);
    }

    //특정 탭 선택 시 해당하는 프래그먼트 리턴
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return tabFragments.get(position);
    }

    //탭 개수
    @Override
    public int getCount() {
        return 2;
    }

    //탭 타이틀 설정
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return _context.getString(tabTitles.get(position));
    }
}
