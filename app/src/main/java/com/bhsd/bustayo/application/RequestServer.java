package com.bhsd.bustayo.application;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RequestServer {

    private ArrayList<HashMap<String, String>> stationInfo;
    Activity activity;
    String station1, station2, station3;

    public RequestServer(Activity activity) {
        this.activity = activity;
        stationInfo = new ArrayList<>();
    }

    public void requestGetInAlarm(String busRouteId, String arsId, final int alarm, String token) {
        final String url = "http://10.0.2.2:3000/post";

        final String routeId = busRouteId;
        final String arsid = arsId;
        final String target_token = token;


        //1. 해당 버스의 노선도를 불러와 ars와 맞는 정류장을 찾는다.
        Thread bus = new Thread() {
            @Override
            public void run() {
                stationInfo = APIManager.getAPIArray(APIManager.GET_STATION_BY_ROUTE, new String[]{routeId}, new String[]{"station", "arsId", "stationNm", "section"});
                int i;
                for (i = 0; i < stationInfo.size(); i++) {
                    if (arsid.equals(stationInfo.get(i).get("arsId"))) {
                        station1 = stationInfo.get(i - 1).get("station");
                        station2 = stationInfo.get(i - 2).get("station");
                        station3 = stationInfo.get(i - 3).get("station");
                        break;
                    }
                }

                JSONObject jsonObject = new JSONObject();
                FirebaseApp.initializeApp(activity);
                try {
                    //입력해둔 edittext의 id와 pw값을 받아와 put해줍니다 : 데이터를 json형식으로 바꿔 넣어주었습니다.
                    switch (alarm) {
                        case 3:
                            jsonObject.put("station3", station3);
                        case 2:
                            jsonObject.put("station2", station2);
                        case 1:
                            jsonObject.put("station1", station1);
                        default:
                            jsonObject.put("busNUM", routeId);
                    }
                    jsonObject.put("target_token", target_token);
                    jsonObject.put("alarm", alarm);
                    jsonObject.put("ord", i);

                    //이제 전송
                    final RequestQueue requestQueue = Volley.newRequestQueue(activity);
                    final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {

                        //데이터 전달을 끝내고 이제 그 응답을 받
                        @Override
                        public void onResponse(JSONObject response) {

                        }
                        //서버로 데이터 전달 및 응답 받기에 실패한 경우 아래 코드가 실행됩니다.
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });
                    jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(jsonObjectRequest);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        bus.start();
        bus.interrupt();
    }

//    public void requestGetOffAlarm(String busRouteId,String mybus, String arsId, final int alarm, String token) {
//        String url = "http://10.0.2.2:3000/put";
//
//        final String routeId = busRouteId;
//        final String arsid = arsId;
//        final String target_token = token;
//        final String myBus = mybus;
//
//
//        //1. 해당 버스의 노선도를 불러와 ars와 맞는 정류장을 찾는다.
//        new Thread() {
//            @Override
//            public void run() {
//                stationInfo = APIManager.getAPIArray(APIManager.GET_STATION_BY_ROUTE, new String[]{routeId}, new String[]{"station", "arsId", "stationNm", "section"});
//                int i;
//                for (i = 0; i < stationInfo.size(); i++) {
//                    if (arsid.equals(stationInfo.get(i).get("arsId"))) {
//                        station1 = stationInfo.get(i - 1).get("station");
//                        station2 = stationInfo.get(i - 2).get("station");
//                        station3 = stationInfo.get(i - 3).get("station");
//                        break;
//                    }
//                }
//
//                JSONObject jsonObject = new JSONObject();
//                FirebaseApp.initializeApp(activity);
//                try {
//                    //입력해둔 edittext의 id와 pw값을 받아와 put해줍니다 : 데이터를 json형식으로 바꿔 넣어주었습니다.
//                    switch (alarm) {
//                        case 3:
//                            jsonObject.put("station3", station3);
//                        case 2:
//                            jsonObject.put("station2", station2);
//                        case 1:
//                            jsonObject.put("station1", station1);
//                        default:
//                            jsonObject.put("busNUM", routeId);
//                    }
//                    jsonObject.put("target_token", target_token);
//                    jsonObject.put("alarm", alarm);
//                    jsonObject.put("ord", i);
//
//
//                    //이제 전송해볼까요?
//                    final RequestQueue requestQueue = Volley.newRequestQueue(activity);
//                    final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
//
//                        //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
//                        @Override
//                        public void onResponse(JSONObject response) {
//
//                        }
//                        //서버로 데이터 전달 및 응답 받기에 실패한 경우 아래 코드가 실행됩니다.
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            error.printStackTrace();
//                        }
//                    });
//                    jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//                    requestQueue.add(jsonObjectRequest);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }
}
