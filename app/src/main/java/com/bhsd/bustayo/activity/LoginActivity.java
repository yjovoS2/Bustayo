package com.bhsd.bustayo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bhsd.bustayo.R;

public class LoginActivity extends AppCompatActivity {

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
//                //결과를 처리하는 부분
//                Response.Listener listener = new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jResponse = new JSONObject(response);
//                            boolean iD = jResponse.getBoolean("checkID");
//                            boolean passWD = jResponse.getBoolean("checkPW");
//                            //return값이 true이면 기존에 없는 아이디 false면 기존에 있는 아이디
//                            if(iD&&passWD){
//                                //로그인 이후동작
//                            }else {
//                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
//                                AlertDialog dialog = builder.setMessage("아이디나 비밀번호가 잘못되었습니다.").setNegativeButton("확인", null).create();
//                                dialog.show();
//                            }
//                        }catch (Exception e){
//                            Log.d("mytest",e.toString());
//                        }
//                    }
//                };
//
//                ValidateRequest vRequest = new ValidateRequest(userID, userPasswd,listener);
//                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
//                queue.add(vRequest);
                //서버 연동

                //test용

                if(userID.equals("test")&&userPasswd.equals("1111")){
                    login.putExtra("userID",userID);
                    login.putExtra("isLogin",true);
                    setResult(RESULT_OK,login);
                    finish();
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }


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
}
