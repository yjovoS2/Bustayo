package com.bhsd.bustayo;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class BusInfo {  // bus의 정보를 담는 객체~!~
    private String[] b_tagNm = { "busRouteId", "busRouteNm", "edStationNm", "busType", "stStationNm", "term" };
    private String[] s_tagNm = { "beginTm", "lastTm" };

    private String b_search = "&strSrch=";
    private String s_search = "&busRouteId=";

    private HashMap<String,String> bus;
    private String url = "http://ws.bus.go.kr/api/rest/";
    private String b_url = url + "busRouteInfo/getBusRouteList?serviceKey=";
    private String s_url = url + "busRouteInfo/getStaionByRoute?ServiceKey=";

    BusInfo(String busNm) {
        bus = new HashMap<>();
        setBus(b_url, b_search, busNm, b_tagNm);
        setBus(s_url, s_search, bus.get("busRouteId"), s_tagNm);
    }
    private void setBus(String urll, String search_tag, String search_content, String[] tags) {
        String key = "a9hQklCDHMmI23KG3suYrx0VtU7OOMgN%2B1SbLmIclORV%2FD%2F5QTRxFtmrjHzv4IEh8GiXMgiryKrlu7KKyAstKg%3D%3D";
        String queryUrl = urll + key + search_tag + search_content;

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
                    tag = parser.getName(); // get tag name
                    for (String s : tags) {
                        if (tag.equals(s)) {
                            Log.d("tag__", s);
                            parser.next();
                            bus.put(s, parser.getText());
                        }
                    }
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            //e.toString();
        }
    }
    void setBusInfoList(String busNm) {
        setBus(b_url, b_search, busNm, b_tagNm);
        setBus(s_url, s_search, bus.get("busRouteId"), s_tagNm);
    }
    String getBusInfoItem(String key) {
        return bus.get(key);
    }
}
