package com.bhsd.bustayo;

import androidx.collection.SimpleArrayMap;

class BusInfo {  // bus의 정보를 담는 객체~!~
    private SimpleArrayMap<String,String> bus;

    private String key = "a9hQklCDHMmI23KG3suYrx0VtU7OOMgN%2B1SbLmIclORV%2FD%2F5QTRxFtmrjHzv4IEh8GiXMgiryKrlu7KKyAstKg%3D%3D";
    private String url = "http://ws.bus.go.kr/api/rest/";
    private ApiManager apiManager = new ApiManager(key, url);

    BusInfo(String busId) {
        setBusInfoList(busId);
    }

    private void setBusInfoList(String busId) {
        String search_tag = "&busRouteId=";
        String[][] tagNm = {{ "busRouteId", "busRouteNm", "edStationNm", "routeType", "stStationNm", "term" }, { "beginTm", "lastTm" }};
        String[] search_url ={ "busRouteInfo/getRouteInfo?serviceKey=", "busRouteInfo/getStaionByRoute?serviceKey=" };

        bus = apiManager.getApiMap(search_url[0], search_tag, busId, tagNm[0]);
        bus.putAll(apiManager.getApiMap(search_url[1], search_tag, busId, tagNm[1]));
    }
    String getBusInfoItem(String key) {
        return bus.get(key);
    }
}
