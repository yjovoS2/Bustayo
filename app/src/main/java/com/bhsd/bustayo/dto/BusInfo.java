package com.bhsd.bustayo.dto;


import com.bhsd.bustayo.application.APIManager;

import java.util.HashMap;
//
public class BusInfo {
    private HashMap<String,String> bus;

    public BusInfo(String busId) {
        setBusInfoList(busId);
    }

    /* busInfoList */
    private void setBusInfoList(String busId) {
        String search_tag = "&busRouteId=";
        String[][] tagNm = {{ "busRouteId", "busRouteNm", "edStationNm", "routeType", "stStationNm", "term" }, { "beginTm", "lastTm" }};
        String[] search_url ={ "busRouteInfo/getRouteInfo?serviceKey=", "busRouteInfo/getStaionByRoute?serviceKey=" };

        bus = APIManager.getAPIMap(APIManager.GET_ROUTE_INFO, new String[]{busId}, tagNm[0]);
        bus.putAll(APIManager.getAPIMap(APIManager.GET_STATION_BY_ROUTE, new String[]{busId}, tagNm[1]));
    }
    public String getBusInfoItem(String key) {
        return bus.get(key);
    }
}