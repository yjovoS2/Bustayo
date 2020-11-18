package com.bhsd.bustayo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bhsd.bustayo.R;
import com.bhsd.bustayo.application.APIManager;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    boolean loginState;
    EditText edID, edPasswd;
    Button loginButton, loginNaver, loginKakao, loginGoogle, loginGuest;
    String userID, userPasswd;
    Intent login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = getIntent();

        edID = findViewById(R.id.editID);
        edPasswd = findViewById(R.id.editPASSWD);
        loginButton = findViewById(R.id.loginButton);
        loginNaver = findViewById(R.id.loginNaver);
        loginKakao = findViewById(R.id.loginKakao);
        loginGoogle = findViewById(R.id.loginGoogle);
        loginGuest = findViewById(R.id.loginGuest);

        findViewById(R.id.searchGoBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userID = edID.getText().toString();
                userPasswd = edPasswd.getText().toString();

                new Thread() {
                    @Override
                    public void run() {
                        synchronized ((Object) loginState) {
                            getDB();
                        }

                        synchronized ((Object) loginState) {
                            if(!loginState) {
                                login.putExtra("userID",userID);

                                /* 성공한 로그인 정보를 SharedPreferences에 저장 */
                                SharedPreferences loginInfo = getSharedPreferences("setting", 0);
                                SharedPreferences.Editor editor = loginInfo.edit();
                                editor.putString("id", userID);
                                editor.putString("password", userPasswd);
                                editor.apply();

                                setResult(RESULT_OK, login);
                                finish();
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        interrupt();
                    }
                }.start();



            }
        });

        loginNaver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //네이버 api연동
                Toast.makeText(LoginActivity.this, "네이버 연동 필요", Toast.LENGTH_SHORT).show();
            }
        });

        loginKakao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //카카오 api연동
                Toast.makeText(LoginActivity.this, "카카오 연동 필요", Toast.LENGTH_SHORT).show();
            }
        });

        loginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //구글 api연동
                Toast.makeText(LoginActivity.this, "구글 연동 필요", Toast.LENGTH_SHORT).show();
            }
        });

        loginGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
            }
        });
    }

    void getDB() {
        String result_msg = "";
        try {
            // Open the connection
            URL url = new URL(APIManager.GET_USER_ID + userID + "&pw=" + userPasswd);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();

            // Get the stream
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            // Set the result
            result_msg = builder.toString();

            JSONObject base = new JSONObject(result_msg);
            JSONObject result = (JSONObject) base.get("result");
            loginState = result.getInt("rowCount") == 0;

        } catch (Exception e) {
            Log.e("yj", ""+e);
        }
    }
}
