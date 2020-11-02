package com.bhsd.bustayo.dto;


public class CurrentBusInfo {

    private String busRouteId, busNum, busDestination, currentLocation1, currentLocation2,busColor, busCongestion, arsId;
    boolean bookmark = false;

    public CurrentBusInfo(String busRouteId, String busColor, String busCongestion, String busNum, String busDestination, String currentLocation1, String currentLocation2, boolean bookmark, String arsId) {
        this.busRouteId = busRouteId;
        this.busColor = busColor;
        this.busCongestion = busCongestion;
        this.busNum = busNum;
        this.busDestination = busDestination;
        this.currentLocation1 = currentLocation1;
        this.currentLocation2 = currentLocation2;
        this.bookmark = bookmark;
        this.arsId = arsId;
    }

    public String getArsId() {
        return arsId;
    }

    public void setArsId(String arsId) {
        this.arsId = arsId;
    }

    public String getBusRouteId() {
        return busRouteId;
    }

    public void setBusRouteId(String busRouteId) {
        this.busRouteId = busRouteId;
    }

    public String getBusColor() {
        return busColor;
    }

    public void setBusColor(String busColor) {
        this.busColor = busColor;
    }

    public String getBusCongestion() {
        return busCongestion;
    }

    public void setBusCongestion(String busCongestion) {
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