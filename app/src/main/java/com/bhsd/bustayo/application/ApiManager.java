package com.bhsd.bustayo.application;

import androidx.collection.SimpleArrayMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ApiManager {
    private static final String key = Common.CJB_KEY;
    private static final String url = Common.BUS_URL;

    // api 결과를 simple array map으로 return
    public static SimpleArrayMap<String, String> getApiMap(String search_url, String search_tag, String search_content, String[] tags) {
        String queryUrl = url + search_url + key + search_tag + search_content;
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

    // api 결과를 simple array map으로 저장한 뒤 array list에 넣어서 return
    public static ArrayList<SimpleArrayMap<String,String>> getApiArray(String search_url, String search_tag, String search_content, String[] tags) {
        String queryUrl = url + search_url + key + search_tag + search_content;
        ArrayList<SimpleArrayMap<String,String>> return_value = new ArrayList<>();

        try {
            URL url = new URL(queryUrl);
            InputStream input_stream = url.openStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new InputStreamReader(input_stream, StandardCharsets.UTF_8));

            String tag;

            parser.next();

            SimpleArrayMap<String,String> item = new SimpleArrayMap<>();
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
                    if(tag.equals("itemList")) {
                        return_value.add(item);
                        item = new SimpleArrayMap<>();
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
}

