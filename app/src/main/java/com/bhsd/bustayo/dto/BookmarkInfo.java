package com.bhsd.bustayo.dto;

import java.util.ArrayList;

public class BookmarkInfo {

    private String busStopName;
    private boolean isshow;
    private ArrayList<CurrentBusInfo> currentBusInfo;

    public BookmarkInfo(String busStopName, ArrayList<CurrentBusInfo> currentBusInfo) {
        this.busStopName = busStopName;
        this.isshow = false;
        this.currentBusInfo = currentBusInfo;
    }

    public String getBusStopName() {
        return busStopName;
    }

    public void setBusStopName(String busStopName) {
        this.busStopName = busStopName;
    }

    public boolean isIsshow() {
        return isshow;
    }

    public void setIsshow(boolean isshow) {
        this.isshow = isshow;
    }

    public ArrayList<CurrentBusInfo> getCurrentBusInfo() {
        return currentBusInfo;
    }

    public void setCurrentBusInfo(ArrayList<CurrentBusInfo> currentBusInfo) {
        this.currentBusInfo = currentBusInfo;
    }
}
