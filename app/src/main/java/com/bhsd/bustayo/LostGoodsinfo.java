package com.bhsd.bustayo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;

public class LostGoodsinfo extends AppCompatActivity {

//    ArrayList<String> lostcategorize;
//    Spinner categories;
    WebView lostGoods;
    WebSettings mWebSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_goodsinfo);

//        categories = findViewById(R.id.lostGroup);

//        insertCategorize();

        lostGoods = findViewById(R.id.webView);

        lostGoods.setWebViewClient(new WebViewClient());
        mWebSetting = lostGoods.getSettings();//세부 세팅 등록
        mWebSetting.setJavaScriptEnabled(true); // 웹페이지 자바스클비트 허용 여부
        mWebSetting.setSupportMultipleWindows(false); // 새창 띄우기 허용 여부
        mWebSetting.setJavaScriptCanOpenWindowsAutomatically(false); // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
        mWebSetting.setLoadWithOverviewMode(true); // 메타태그 허용 여부
        mWebSetting.setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
        mWebSetting.setSupportZoom(false); // 화면 줌 허용 여부
        mWebSetting.setBuiltInZoomControls(false); // 화면 확대 축소 허용 여부
        mWebSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈 맞추기
        mWebSetting.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 허용 여부
        mWebSetting.setDomStorageEnabled(true); // 로컬저장소 허용 여부

        lostGoods.loadUrl("https://www.lost112.go.kr/lost/lostList.do");
    }

//    public void insertCategorize() {
//        String[] email = {"전체", "가방", "귀금속", "도서용품", "서류", "쇼핑백", "의류", "전자기기", "지갑", "휴대폰", "카드", "기타"};
//        lostcategorize = new ArrayList<String>(Arrays.asList(email));
//
//        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, email);
//        categories.setAdapter(arrayAdapter);
//    }
}
