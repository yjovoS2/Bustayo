package com.bhsd.bustayo.dto;

///////////////////////////////////////////////////
//검색 화면에서 리사이클러뷰의 한 행에 사용되는 아이템 클래스
public class SearchRecyclerItem {
    private String busNum, busArea, busType;
    private boolean busMark;

    public SearchRecyclerItem(String busNum, String busArea, String busType, boolean busMark) {
        this.busNum = busNum;
        this.busArea = busArea;
        this.busType = busType;
        this.busMark = busMark;
    }

    public String getBusNum() {
        return busNum;
    }

    public void setBusNum(String busNum) {
        this.busNum = busNum;
    }

    public String getBusArea() {
        return busArea;
    }

    public void setBusArea(String busArea) {
        this.busArea = busArea;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public boolean isBusMark() {
        return busMark;
    }

    public void setBusMark(boolean busMark) {
        this.busMark = busMark;
    }
}
