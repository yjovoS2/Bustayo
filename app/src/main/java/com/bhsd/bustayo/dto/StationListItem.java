package com.bhsd.bustayo.dto;

import java.util.ArrayList;
import java.util.HashMap;

public class StationListItem {
    private String stationName, stationId, arsId, routeId;
    private int previous, next, busType;
    private ArrayList<HashMap<String,String>> bus;

    public StationListItem(String stationName, String stationId, String arsId, String routeId, int busType, int previous, int next, ArrayList<HashMap<String,String>> bus) {
        this.stationName = stationName;
        this.stationId = stationId;
        this.arsId = arsId;
        this.routeId = routeId;
        this.busType = busType;
        this.previous = previous;
        this.next = next;
        this.bus = bus;
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

    void setBusType(int busType) {
        this.busType = busType;
    }
    public int getBusType() {
        return busType;
    }

    public ArrayList<HashMap<String,String>> getBus() {
        return bus;
    }
}