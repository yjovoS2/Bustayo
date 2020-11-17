package com.bhsd.bustayo.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bhsd.bustayo.R;
import com.bhsd.bustayo.application.APIManager;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JoinActivity extends AppCompatActivity {

    Button btncomplete;
    EditText insertID,insertPasswd,insertName, checkPasswd, birthday, phoneNumber, checkemail;
    TextView checkID, checkPW;
    RadioGroup checkgender;
    String id, passwd, name, birth, gender, phoneNum, email, _result;
    boolean id_check = false;
    boolean pw_check = false;
    ImageView drawerinfo;
    ConstraintLayout optioninfo;
    Spinner emailform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        findViewById(R.id.searchGoBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btncomplete = findViewById(R.id.Completionjoin);
        insertID = findViewById(R.id.insertID);
        insertPasswd = findViewById(R.id.insertPW);
        insertName = findViewById(R.id.insertName);
        checkPasswd = findViewById(R.id.checkingPW);
        optioninfo = findViewById(R.id.optioninfo);
        birthday = findViewById(R.id.insertBirthday);
        phoneNumber = findViewById(R.id.insertPhoneNum);
        checkgender = findViewById(R.id.userGender);
        checkemail = findViewById(R.id.insertEmail);
        emailform = findViewById(R.id.emailForm);
        drawerinfo = findViewById(R.id.drawerinfo);
        checkID = findViewById(R.id.checkID);
        checkPW = findViewById(R.id.checkPW);

        insertEmail();

        /* 아이디 값이 변경되면 중복 확인 결과 초기화 */
        insertID.addTextChangedListener(new InputText() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                id_check = false;
                checkID.setVisibility(View.GONE);
            }
        });

        /* 아이디 중복 확인 */
        insertID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    new Thread() {
                        @Override
                        public void run() {
                            id = insertID.getText().toString();
                            synchronized ((Object) id_check) {
                                getDB();
                            }

                            String msg = "";
                            final int color;
                            // id_check 사용! db에서 값 가져오면 비교해서 없으면 true, 있으면 false

                            synchronized ((Object) id_check) {
                                if (!id_check) {
                                    color = getColor(R.color.bus_red);
                                    msg = "중복된 아이디입니다.";
                                } else {
                                    color = getColor(R.color.bus_green);
                                    msg = "사용 가능한 아이디입니다.";
                                }
                            }

                            final String finalMsg = msg;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    checkID.setVisibility(View.VISIBLE);
                                    checkID.setTextColor(color);
                                    checkID.setText(finalMsg);
                                }
                            });

                            interrupt();    // 할일 끝났으니 종료
                        }
                    }.start();
                }
            }
        });

        /* 비밀번호, 비밀번호 확인 값이 변경되면 비밀번호 확인 결과 초기화 */
        insertPasswd.addTextChangedListener(new InputText() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pw_check = false;
                checkPW.setVisibility(View.GONE);
            }
        });
        checkPasswd.addTextChangedListener(new InputText() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pw_check = false;
                checkPW.setVisibility(View.GONE);
            }
        });

        /* 비밀번호 확인 */
        checkPasswd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    int color;
                    String check = checkPasswd.getText().toString(), msg = "";
                    passwd = insertPasswd.getText().toString();

                    checkPW.setVisibility(View.VISIBLE);
                    if(!check.equals(passwd)) {
                        color = getColor(R.color.bus_red);
                        msg = "비밀번호가 일치하지 않습니다.";
                        pw_check = false;
                    } else {
                        color = getColor(R.color.bus_green);
                        msg = "비밀번호가 일치합니다.";
                        pw_check = true;
                    }
                    checkPW.setTextColor(color);
                    checkPW.setText(msg);
                }
            }
        });

        btncomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = insertID.getText().toString();
                passwd = insertPasswd.getText().toString();
                name = insertName.getText().toString();

                if (id.equals("")) {            // 아이디 입력 안함
                    Toast.makeText(JoinActivity.this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if(!id_check) {           // 아이디 중복확인 안됨
                    Toast.makeText(JoinActivity.this, "아이디 중복확인을 해주세요.", Toast.LENGTH_SHORT).show();
                } else if(passwd.equals("")) {  // 비밀번호 입력 안함
                    Toast.makeText(JoinActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if(!pw_check) {          // 비밀번호 확인 안됨
                    Toast.makeText(JoinActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                } else if(name.equals("")) {    // 이름 입력 안함
                    Toast.makeText(JoinActivity.this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    birth = birthday.getText().toString();
                    phoneNum = phoneNumber.getText().toString();
                    int check = checkgender.getCheckedRadioButtonId();
                    if (check == R.id.female)       // 여성 선택
                        gender = "w";
                    else if (check == R.id.male)    // 남성 선택
                        gender = "m";
                    else                            // 아무것도 선택 안함
                        gender = "";
                    String temp = checkemail.getText().toString();
                    email = temp.equals("")? "" : temp + "@" + emailform.getSelectedItem().toString();
                    // 이메일 아이디부분에 아무것도 작성하지 않았다면? "" 아니면 이메일주소

                    new Thread() {
                        @Override
                        public void run() {
                            insertDB();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(_result.equals("1")) {
                                        Toast.makeText(JoinActivity.this, "가입이 완료되었습니다.", Toast.LENGTH_LONG).show();
                                        finish();
                                    } else {
                                        Toast.makeText(JoinActivity.this, "오류가 발생했습니다.", Toast.LENGTH_LONG).show();
                                    }

                                }
                            });

                            interrupt();    // 할일 끝났으니 종료
                        }
                    }.start();
                }
            }
        });


        drawerinfo.setOnClickListener(new View.OnClickListener() {  // ∨ 클릭
            @Override
            public void onClick(View v) {
                  if(optioninfo.getVisibility()==View.VISIBLE) {
                      optioninfo.setVisibility(View.GONE);
                  } else {
                      optioninfo.setVisibility(View.VISIBLE);
                  }
            }
        });

    }

    void getDB() {
        String result_msg = "";
        try {
            // Open the connection
            URL url = new URL(APIManager.GET_USER_ID + id);
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

            id_check = result.getInt("rowCount") == 0;

            Log.e("yj", ""+id_check);
        } catch (Exception e) {
            Log.e("yj", ""+e);
        }
    }

    void insertDB() {
        String result_msg = "";
        try {
            String i_pw = "&pw=" + passwd;
            String i_name = "&name=" + name;
            String i_birthdate = birth.equals("")? "" : "&birthdate=" + birth;
            String i_tel = phoneNum.equals("")? "" : "&tel=" + phoneNum;
            String i_gender = gender.equals("")? "" : "&gender=" + gender;
            String i_email = email.equals("")? "" : "&email=" + email;
            URL url = new URL(APIManager.JOIN_USER + id +
                    i_pw + i_name + i_birthdate + i_tel + i_gender + i_email);
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
            _result = base.getString("result");

        } catch (Exception e) {
            Log.e("yj", ""+e);
        }
    }

    public void insertEmail(){
        String[] email ={"naver.com", "daum.net", "empal.com", "nate.com", "dreamwiz.com", "hanmail.net"};

        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, email);
        emailform.setAdapter(arrayAdapter);
    }
}

/* TextWatcher에서 안쓰는 부분 제외시키고 InputText 만들기! */
abstract class InputText implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // 입력 전
    }
    @Override
    public void afterTextChanged(Editable s) {
        // 입력 후
    }
}
