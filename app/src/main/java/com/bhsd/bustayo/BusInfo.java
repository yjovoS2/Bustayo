package com.bhsd.bustayo;

import androidx.collection.SimpleArrayMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

class BusInfo {  // bus의 정보를 담는 객체~!~

    private SimpleArrayMap<String,String> bus;

    private String[] tagNm = { "busRouteId", "busRouteNm", "edStationNm", "busType", "stStationNm", "term", "beginTm", "lastTm" };
    private String search = "&busRouteId=";
    private String search_url ="busRouteInfo/getStaionByRoute?serviceKey=";

    BusInfo(String busId) {
        bus = setBus(search_url, search, busId, tagNm);
    }

    // api
    private SimpleArrayMap<String,String> setBus(String search_url, String search_tag, String search_content, String[] tags) {
        String key = "a9hQklCDHMmI23KG3suYrx0VtU7OOMgN%2B1SbLmIclORV%2FD%2F5QTRxFtmrjHzv4IEh8GiXMgiryKrlu7KKyAstKg%3D%3D";
        String url1 = "http://ws.bus.go.kr/api/rest/";
        String queryUrl = url1 + search_url + key + search_tag + search_content;
        SimpleArrayMap<String,String> return_value = new SimpleArrayMap<>();

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
                            parser.next();
                            return_value.put(s, parser.getText());
                        }
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
    void setBusInfoList(String busId) {
        bus = setBus(search_url, search, busId, tagNm);
    }
    String getBusInfoItem(String key) {
        return bus.get(key);
    }
}
