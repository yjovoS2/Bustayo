package com.bhsd.bustayo;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

class ValidateRequest extends StringRequest {

    //서버 연동을 위한 url 서버디비용으로 바꿔야함
    final static private String URL = "http://10.0.2.2/2016081016/ValidateTest.php";

    private Map<String, String> parameters;

    public ValidateRequest(String userID,String userPasswd, Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);

        //요청값을 HashMap형식으로 보냄
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("userPasswd", userPasswd);//여기를 서버디비에 들어갈 내용으로 바꿔

    }

    protected Map<String, String> getParams() throws AuthFailureError{
        return parameters;
    }
}
