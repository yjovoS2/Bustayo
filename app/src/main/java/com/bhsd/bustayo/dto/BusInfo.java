package com.bhsd.bustayo.dto;

import androidx.collection.SimpleArrayMap;

import com.bhsd.bustayo.application.ApiManager;

public class BusInfo {  // bus의 정보를 담는 객체~!~
    private SimpleArrayMap<String,String> bus;

    public BusInfo(String busId) {
        setBusInfoList(busId);
    }

    private void setBusInfoList(String busId) {
        String search_tag = "&busRouteId=";
        String[][] tagNm = {{ "busRouteId", "busRouteNm", "edStationNm", "routeType", "stStationNm", "term" }, { "beginTm", "lastTm" }};
        String[] search_url ={ "busRouteInfo/getRouteInfo?serviceKey=", "busRouteInfo/getStaionByRoute?serviceKey=" };

        bus = ApiManager.getApiMap(search_url[0], search_tag, busId, tagNm[0]);
        bus.putAll(ApiManager.getApiMap(search_url[1], search_tag, busId, tagNm[1]));
    }
    public String getBusInfoItem(String key) {
        return bus.get(key);
    }
}
