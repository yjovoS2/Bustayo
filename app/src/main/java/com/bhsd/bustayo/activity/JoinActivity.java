package com.bhsd.bustayo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.bhsd.bustayo.R;

import java.util.ArrayList;
import java.util.Arrays;

public class JoinActivity extends AppCompatActivity {

    Button btncomplete, checkNewId;
    EditText insertID,insertPasswd,insertName, checkPasswd, birthday, phoneNumber, checkemail;
    RadioGroup checkgender;
    String id, passwd, name, birth, gender, phoneNum, email, check;
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
        checkNewId = findViewById(R.id.checkNewId);

        insertEmail();

        checkNewId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(JoinActivity.this, "중복체크.", Toast.LENGTH_SHORT).show();
            }
        });

        btncomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = insertID.getText().toString();
                passwd = insertPasswd.getText().toString();
                name = insertName.getText().toString();
                check = checkPasswd.getText().toString();

                if(id == null || passwd == null || name == null ||check ==null) {
                    if (passwd.equals(check)) {
                        if (optioninfo.getVisibility() == View.VISIBLE) {
                            birth = birthday.getText().toString();
                            phoneNum = phoneNumber.getText().toString();
                            int check = checkgender.getCheckedRadioButtonId();
                            if (check == R.id.female)
                                gender = "여성";
                            else
                                gender = "남성";
                            email = checkemail.getText().toString() + "@" + emailform.getSelectedItem().toString();
                        } else {

                        }
                    } else
                        Toast.makeText(JoinActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(JoinActivity.this, "필수정보가 모두 입력되지 않았습니다..", Toast.LENGTH_SHORT).show();
            }
        });


        drawerinfo.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
              if(optioninfo.getVisibility()==View.VISIBLE)
                 optioninfo.setVisibility(View.GONE);
               else
                  optioninfo.setVisibility(View.VISIBLE);
        }
    });

    }

    public void insertEmail(){
        String[] email ={"naver.com", "daum.net", "empal.com", "nate.com", "dreamwiz.com", "hanmail.net"};

        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, email);
        emailform.setAdapter(arrayAdapter);
    }
}
