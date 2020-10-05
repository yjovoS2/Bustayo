package com.bhsd.bustayo.dto;


public class CurrentBusInfo {

    private int busColor, busCongestion;
    private String busNum, busDestination, currentLocation1, currentLocation2;
    boolean bookmark = false;

    public CurrentBusInfo(int busColor, int busCongestion, String busNum, String busDestination, String currentLocation1, String currentLocation2, boolean bookmark) {
        this.busColor = busColor;
        this.busCongestion = busCongestion;
        this.busNum = busNum;
        this.busDestination = busDestination;
        this.currentLocation1 = currentLocation1;
        this.currentLocation2 = currentLocation2;
        this.bookmark = bookmark;
    }

    public int getBusColor() {
        return busColor;
    }

    public void setBusColor(int busColor) {
        this.busColor = busColor;
    }

    public int getBusCongestion() {
        return busCongestion;
    }

    public void setBusCongestion(int busCongestion) {
        this.busCongestion = busCongestion;
    }

    public String getBusNum() {
        return busNum;
    }

    public void setBusNum(String busNum) {
        this.busNum = busNum;
    }

    public String getBusDestination() {
        return busDestination;
    }

    public void setBusDestination(String busDestination) {
        this.busDestination = busDestination;
    }

    public String getCurrentLocation1() {
        return currentLocation1;
    }

    public void setCurrentLocation1(String currentLocation1) {
        this.currentLocation1 = currentLocation1;
    }

    public String getCurrentLocation2() {
        return currentLocation2;
    }

    public void setCurrentLocation2(String currentLocation2) {
        this.currentLocation2 = currentLocation2;
    }

    public boolean isBookmark() {
        return bookmark;
    }

    public void setBookmark(boolean bookmark) {
        this.bookmark = bookmark;
    }
}