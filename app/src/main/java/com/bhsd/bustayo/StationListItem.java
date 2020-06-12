package com.bhsd.bustayo;

public class StationListItem {
    private String stationName, stationId;
    private int previous, next;

    public StationListItem(String stationName, String stationId, int previous, int next) {
        this.stationName = stationName;
        this.stationId = stationId;
        this.previous = previous;
        this.next = next;
    }

    void setStationName(String stationName) {
        this.stationName = stationName;
    }

    String getStationName() {
        return stationName;
    }

    void setStationId(String stationId) {
        this.stationId = stationId;
    }

    String getStationId() {
        return stationId;
    }

    void setPreviousColor(int colorID) {
        previous = colorID;
    }

    int getPreviousColor() {
        return previous;
    }

    void setNextColor(int colorID) {
        next = colorID;
    }

    int getNextColor() {
        return next;
    }
}