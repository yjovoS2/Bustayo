package com.bhsd.bustayo.application;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class APIManager {
    private static final String CJB_KEY     = "mwvEaAXw6zA%2BTnM5a7W%2F24dpurVReU2rdiVACF8v3oQrY%2Becbbv018iPqZnXId0oqx4cSkUBbQxU1vFjPn62kw%3D%3D";
    private static final String LYJ_KEY     = "a9hQklCDHMmI23KG3suYrx0VtU7OOMgN%2B1SbLmIclORV%2FD%2F5QTRxFtmrjHzv4IEh8GiXMgiryKrlu7KKyAstKg%3D%3D";
    private static final String PCJ_KEY     = "m0Oa7AbIFizhCq03rya2SapvO0bHMctYwrKZls8BAUgoHdvVWVuU%2BVhh%2BEiYxtXXVYuJCOPP3DYvFywUzb%2F3Ow%3D%3D";
    private static final String SERVICE_KEY = LYJ_KEY;

    private static final String BUS_URL = "http://ws.bus.go.kr/api/rest/";
    private static final String LOST_GOODS_URL = "http://apis.data.go.kr/1320000/LostGoodsInfoInqireService/";

    private static final String BUS_POS_URL = BUS_URL + "buspos/";
    private static final String ROUTE_INFO = BUS_URL + "busRouteInfo/";
    private static final String STATION_INFO_URL = BUS_URL + "stationinfo/";

    /* 서버주소 */
    public static final String SERVER_URL = "http://118.67.134.138:3000/";

    /* 사용할 API url */
    public static final String GET_BUSPOS_BY_RT_ID = BUS_POS_URL + "getBusPosByRtid?serviceKey=" + SERVICE_KEY;
    public static final String GET_ROUTE_INFO = ROUTE_INFO + "getRouteInfo?serviceKey=" + SERVICE_KEY;
    public static final String GET_BUS_ROUTE_LIST = ROUTE_INFO + "getBusRouteList?serviceKey=" + SERVICE_KEY;
    public static final String GET_STATION_BY_ROUTE = ROUTE_INFO + "getStaionByRoute?serviceKey=" + SERVICE_KEY;
    public static final String GET_STATION_BY_POS = STATION_INFO_URL + "getStationByPos?serviceKey=" + SERVICE_KEY;
    public static final String GET_STATION_BY_NAME = STATION_INFO_URL + "getStationByName?serviceKey=" + SERVICE_KEY;
    public static final String GET_STATION_BY_UID_ITEM = STATION_INFO_URL + "getStationByUid?serviceKey=" + SERVICE_KEY;
    public static final String GET_LOST_GOODS = LOST_GOODS_URL + "getLostGoodsInfoAccTpNmCstdyPlace?serviceKey=" + SERVICE_KEY
            + "&LST_PLACE=버스&numOfRows=10";
    public static final String GET_LOST_GOODS_DETAIL = LOST_GOODS_URL + "getLostGoodsDetailInfo?serviceKey=" + SERVICE_KEY;

    /* 사용할 API search tag */
    private static final String[] GET_BUSPOS_BY_RT_ID_TAG = { "&busRouteId=" };
    private static final String[] GET_ROUTE_INFO_TAG = { "&busRouteId=" };
    private static final String[] GET_BUS_ROUTE_LIST_TAG = { "&strSrch=" };
    private static final String[] GET_STATION_BY_ROUTE_TAG = { "&busRouteId=" };
    private static final String[] GET_STATION_BY_POS_TAG = { "&tmX=", "&tmY=", "&radius=" };
    private static final String[] GET_STATION_BY_NAME_TAG = { "&stSrch=" };
    private static final String[] GET_STATION_BY_UID_ITEM_TAG = { "&arsId=" };
    private static final String[] GET_LOST_GOODS_TAG = { "&LST_PRDT_NM=", "&pageNo=" };
    private static final String[] GET_LOST_GOODS_DETAIL_TAG = { "&ATC_ID=" };

    // =================================================
    // api 결과를 HashMap으로 return
    // -------------------------------------------------
    //   String _url                    찾아올 URL
    //   String[] search_content        검색할 내용
    //   String[] tags                  받아올 태그
    // =================================================
    public static HashMap<String, String> getAPIMap(String _url, String[] search_content, String[] tags) {
        HashMap<String,String> return_value = new HashMap<>();
        String queryUrl = _url + setAPISearchItems(_url, search_content);

        try {
            URL url = new URL(queryUrl);
            InputStream input_stream = url.openStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new InputStreamReader(input_stream, StandardCharsets.UTF_8));

            String tag;

            parser.next();
            int eventType = parser.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    tag = parser.getName();
                    for (String s : tags) {
                        if (tag.equals(s)) {
                            parser.next();
                            return_value.put(s, parser.getText());
                        }
                    }
                }
                if(eventType == XmlPullParser.END_TAG) {
                    tag = parser.getName();
                    if (tag.equals("itemList") || tag.equals("item")) {
                        return return_value;
                    }
                }
                eventType = parser.next();
            }
            return return_value;
        } catch (Exception e) {
            //e.toString();
        }
        return null;
    }

    // =================================================
    // api 결과를 ArrayList<HashMap>으로 return
    // -------------------------------------------------
    //   String _url                    찾아올 URL
    //   String[] search_content        검색할 내용
    //   String[] tags                  받아올 태그
    // =================================================
    public static ArrayList<HashMap<String,String>> getAPIArray(String _url, String[] search_content, String[] tags) {
        ArrayList<HashMap<String,String>> return_value = new ArrayList<>();
        String queryUrl = _url + setAPISearchItems(_url, search_content);

        try {
            URL url = new URL(queryUrl);
            InputStream input_stream = url.openStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new InputStreamReader(input_stream, StandardCharsets.UTF_8));

            String tag;

            parser.next();

            HashMap<String,String> item = new HashMap<>();
            int eventType = parser.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    tag = parser.getName(); // get tag name
                    for (String s : tags) {
                        if (tag.equals(s)) {
                            parser.next();
                            item.put(s, parser.getText());
                        }
                    }
                }
                if(eventType == XmlPullParser.END_TAG) {
                    tag = parser.getName();
                    if(tag.equals("itemList") || tag.equals("item")) {
                        return_value.add(item);
                        item = new HashMap<>();
                    }
                }
                eventType = parser.next();
            }

            return return_value;
        } catch (Exception e) {
            //e.toString();
        }
        return null;
    }

    /* 각 url에 사용되는 search tag를 사용해 한 문장으로 return */
    private static String setAPISearchItems(String url, String[] items) {
        String[] search_tags;
        StringBuilder return_value = new StringBuilder();
        switch (url) {
            case GET_BUSPOS_BY_RT_ID:
                search_tags = GET_BUSPOS_BY_RT_ID_TAG;
                break;
            case GET_BUS_ROUTE_LIST:
                search_tags = GET_BUS_ROUTE_LIST_TAG;
                break;
            case GET_ROUTE_INFO:
                search_tags = GET_ROUTE_INFO_TAG;
                break;
            case GET_STATION_BY_POS:
                search_tags = GET_STATION_BY_POS_TAG;
                break;
            case GET_STATION_BY_UID_ITEM:
                search_tags = GET_STATION_BY_UID_ITEM_TAG;
                break;
            case GET_STATION_BY_NAME:
                search_tags = GET_STATION_BY_NAME_TAG;
                break;
            case GET_STATION_BY_ROUTE:
                search_tags = GET_STATION_BY_ROUTE_TAG;
                break;
            case GET_LOST_GOODS:
                search_tags = GET_LOST_GOODS_TAG;
                break;
            case GET_LOST_GOODS_DETAIL:
                search_tags = GET_LOST_GOODS_DETAIL_TAG;
                break;
            default:
                return null;
        }
        for(int i = 0; i < search_tags.length; i++) {
            return_value.append(search_tags[i]).append(items[i]);
        }
        return return_value.toString();
    }
}