package com.bhsd.bustayo.dto;

import java.util.ArrayList;

public class BookmarkInfo {

    private String busStopName, arsId;
    private ArrayList<CurrentBusInfo> currentBusInfo;

    public BookmarkInfo(String busStopName, String arsId, ArrayList<CurrentBusInfo> currentBusInfo) {
        this.busStopName = busStopName;
        this.arsId = arsId;
        this.currentBusInfo = currentBusInfo;
    }

    public String getBusStopName() {
        return busStopName;
    }

    public void setBusStopName(String busStopName) {
        this.busStopName = busStopName;
    }

    public ArrayList<CurrentBusInfo> getCurrentBusInfo() {
        return currentBusInfo;
    }

    public void setCurrentBusInfo(ArrayList<CurrentBusInfo> currentBusInfo) {
        this.currentBusInfo = currentBusInfo;
    }

    public String getArsId() {
        return arsId;
    }

    public void setArsId(String arsId) {
        this.arsId = arsId;
    }
}
