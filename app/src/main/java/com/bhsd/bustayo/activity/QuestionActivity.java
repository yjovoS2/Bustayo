package com.bhsd.bustayo.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bhsd.bustayo.R;
import com.bhsd.bustayo.application.APIManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class QuestionActivity extends AppCompatActivity {

    EditText et_name, et_email, et_content;
    Spinner sp_email;

    String input_name, input_email, input_content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        init();
    }

    void init() {
        et_name = findViewById(R.id.question_user_name);
        et_email = findViewById(R.id.question_user_email);
        et_content = findViewById(R.id.question_content);
        sp_email = findViewById(R.id.question_user_email_spinner);

        setSpinner();

        findViewById(R.id.question_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        send();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "문의가 정상적으로 접수되었습니다.", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });
                    }
                }.start();
            }
        });

        findViewById(R.id.searchGoBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    void setSpinner() {
        String[] email ={"naver.com", "daum.net", "empal.com", "nate.com", "dreamwiz.com", "hanmail.net"};

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, email);
        sp_email.setAdapter(arrayAdapter);
    }

    void send() {
        String title = "", sendContent = "";
        input_email = et_email.getText().toString() + "@" + sp_email.getSelectedItem();
        input_name = et_name.getText().toString();
        input_content = et_content.getText().toString();

        title = "[버스타요] " + input_name + "(" + input_email + ")님의 문의가 접수되었습니다.";
        sendContent = "[문의자 정보]\n이름 : " + input_name + "\n이메일 : " + input_email + "\n\n[문의 내용]\n" + input_content;

        try {
            URL url = new URL(APIManager.SEND_MAIL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");

            StringBuilder sb = new StringBuilder();
            sb.append("mail=").append(input_email).append("&");
            sb.append("title=").append(title).append("&");
            sb.append("content=").append(sendContent);

            String sb_str = sb.toString();

            Log.e("yj", sb_str);


            URLEncoder.encode(sb_str,"UTF-8");

            PrintWriter pw = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
            pw.write(sb_str);
            pw.flush();

            BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder buff = new StringBuilder();
            String line;

            while((line = bf.readLine()) != null) {
                buff.append(line);
            }

        } catch (Exception e) {
            Log.e("yj", ""+e);
        }
    }
}
