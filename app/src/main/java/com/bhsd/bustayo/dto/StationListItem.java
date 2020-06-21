package com.bhsd.bustayo.dto;

public class StationListItem {
    private String stationName, stationId, arsId, routeId;
    private int previous, next;

    public StationListItem(String stationName, String stationId, String arsId, String routeId, int previous, int next) {
        this.stationName = stationName;
        this.stationId = stationId;
        this.arsId = arsId;
        this.routeId = routeId;
        this.previous = previous;
        this.next = next;
    }

    void setStationName(String stationName) {
        this.stationName = stationName;
    }
    public String getStationName() {
        return stationName;
    }

    void setArsId(String arsId) {
        this.arsId = arsId;
    }
    public String getArsId() {
        return arsId;
    }

    void setStationId(String stationId) {
        this.stationId = stationId;
    }
    public String getStationId() {
        return stationId;
    }

    void setRouteId(String routeId) {
        this.routeId = routeId;
    }
    public String getRouteId() {
        return routeId;
    }

    void setPreviousColor(int colorID) {
        previous = colorID;
    }
    public int getPreviousColor() {
        return previous;
    }

    void setNextColor(int colorID) {
        next = colorID;
    }
    public int getNextColor() {
        return next;
    }
}