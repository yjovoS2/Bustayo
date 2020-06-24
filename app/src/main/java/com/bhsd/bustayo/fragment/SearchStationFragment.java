package com.bhsd.bustayo.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bhsd.bustayo.R;
import com.bhsd.bustayo.activity.SearchActivity;
import com.bhsd.bustayo.adapter.SearchRecyclerAdapter;
import com.bhsd.bustayo.application.APIManager;
import com.bhsd.bustayo.application.Common;
import com.bhsd.bustayo.database.TestDB;
import com.bhsd.bustayo.dto.SearchRecyclerItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

////////////////////////////////////////////////////////////////////
// 검색 화면에 있는 정류장 프래그먼트
//   - 정류장 검색 히스토리를 출력하거나 API를 통해 받아온 정류장 목록을 출력함
////////////////////////////////////////////////////////////////////
public class SearchStationFragment extends Fragment {

    private RecyclerView                    searchStationRecycler; //정류장 리스트
    private SearchRecyclerAdapter           searchAdapter;         //정류장 리스트 출력 어댑터
    private ArrayList<SearchRecyclerItem>   data;                  //정류장 리스트 데이터
    private HashMap<String, String>         stationData;           //1개의 정류장에 대한 정보
    private int                             MAX_API = 10;

    private String                          searchStr;             //검색어
    private Timer                           searchTimer;           //API 과다 호출 방지

    private TestDB                          DBHelper;              //내부 디비 연결도구

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        //필요한 뷰 인플레이션
        View view             = inflater.inflate(R.layout.fragment_search_station, container, false);
        searchStationRecycler = view.findViewById(R.id.searchStationRecycler);

        //필요한 객체 생성
        DBHelper              = new TestDB(getContext());
        data                  = new ArrayList<>();

        /*
         * 어댑터 생성
         * 리스트 출력 방식 설정(리니어 레이아웃)
         * 리사이클러뷰에 어댑터 연결
         */
        searchAdapter = new SearchRecyclerAdapter(data, false, getContext());
        searchStationRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        searchStationRecycler.setAdapter(searchAdapter);

        ////////////////////////////////////////////////////////////////
        // 검색 전 : 내부 디비에 있는 정류장 검색 히스토리를 출력
        // 검색 후 : API 호출을 통해 검색어에 해당하는 정류장 출력
        // 텍스트에 변화가 생길 때 마다 API 호출 시 많은 트래픽이 발생되므로
        // 사용자가 조회할 정류장명을 모두 입력했을 때 API가 호출되도록 타이머 설정
        ////////////////////////////////////////////////////////////////
        SearchActivity.searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(searchTimer != null)
                    searchTimer.cancel();
            }

            @Override
            public void afterTextChanged(final Editable s) {
                if(s.length() > 0) {
                    searchTimer = new Timer();
                    searchTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            searchStr = s.toString();
                            new SearchStationXmlparse().execute();
                        }
                    }, 1000);
                } else
                    useInterDB();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        /*
         * 데이터베이스 연결
         * 정류장 검색 히스토리 조회 (최근 조회 항목이 먼저 출력)
         * 조회된 데이터 저장
         * 데이터베이스 연결 해제
         */
        if(SearchActivity.searchText.length() == 0){
            useInterDB();
        }
    }

    //////////////////////////////////////
    // 네트워크 통신을 하는 워커 스레드 클래스
    //   - API 호출을 통해 받아온 데이터를 저장
    //////////////////////////////////////
    private class SearchStationXmlparse extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            data.clear();

            ArrayList<HashMap<String, String>> list = APIManager.getAPIArray(APIManager.GET_STATION_BY_NAME, new String[]{searchStr}, new String[]{"arsId", "stId", "stNm"});

            for (HashMap<String,String> map : list) {
                /*
                 * 서울 지역이 아닌 경우 0을 반환하므로 체크
                 * API 호출을 통해 받아온 정류장명이 검색창에 있는 텍스트와 처음부터 일치하는지 확인
                 * API를 통해 받아온 데이터 개수가 5개 이하면 다음 정류장을 보여줌 (API 제한 문제)
                 */
                if(!map.get("arsId").equals("0") && map.get("stNm").startsWith(searchStr)){
                   if(list.size() < MAX_API) {
                       HashMap<String, String> nextStation = APIManager.getAPIMap(APIManager.GET_STATION_BY_UID_ITEM, new String[]{map.get("arsId")}, new String[]{"nxtStn"});
                       data.add(new SearchRecyclerItem(map.get("stId"), map.get("stNm"), map.get("arsId"), nextStation.get("nxtStn") + " 방면"));
                   }
                   else
                       data.add(new SearchRecyclerItem(map.get("stId"), map.get("stNm"), map.get("arsId"), map.get("stId")));
                }
            }

            updateAdapter();

            return null;
        }
    }

    /////////////////////////////////////////////////
    // 데이터베이스 연결
    // 정류장 검색 히스토리 조회 (최근 조회 항목이 먼저 출력)
    // 조회된 데이터 저장
    // 데이터베이스 연결 해제
    /////////////////////////////////////////////////
    private void useInterDB(){
        data.clear();

        SQLiteDatabase dbSQL       = DBHelper.getReadableDatabase();
        Cursor         cursor      = dbSQL.rawQuery("SELECT * FROM stationHistoryTB ORDER BY timestamp desc;", null);

        while(cursor.moveToNext())
            data.add(new SearchRecyclerItem(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)+ " 방면"));

        cursor.close();
        dbSQL.close();

        updateAdapter();
    }

    //////////////////////////////////////////////////////
    // 리사이클러뷰 데이터에 변화가 생길 경우 적용 시키기 위해 호출
    //////////////////////////////////////////////////////
    private void updateAdapter(){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                searchAdapter.notifyDataSetChanged();
            }
        });
    }
}
