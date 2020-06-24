package com.bhsd.bustayo.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bhsd.bustayo.R;
import com.bhsd.bustayo.database.TestDB;
import com.naver.maps.map.LocationTrackingMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

//////////////////////////////////////////////////////
// 불편신고 접수 리스트 -> 불편신고 접수
//   - 불편신고를 할 수 있는 화면
//   - 사용자의 SMS를 이용하여 다산콜센터로 접수
//   - 접수된 불편신고는 불편신고 접수 리스트에서 확인할 수 있음
//////////////////////////////////////////////////////
public class ComplaintAddActivity extends AppCompatActivity {

    private Calendar    calendar;      //날짜 관련 처리
    private ImageView   goBack;        //뒤로가기 버튼
    private Button      complaintAdd;  //접수 버튼
    private TestDB      DBHelper;      //DB 연결 도구

    //사용자 입력 정보
    private EditText    complaintBusNum, complaintContent;
    private EditText    complaintName, complaintPhone;
    private EditText    complaintDate, complaintTime;

    private String      data1 = ""; //불편신고 정보
    private String      data2 = ""; //불편신고 내용

    /////////////////
    // 초기화 작업
    /////////////////
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_complaint);

        //필요한 뷰 인플레이션
        goBack           = findViewById(R.id.goBack);
        complaintAdd     = findViewById(R.id.complaintAdd);
        complaintName    = findViewById(R.id.complaintName);
        complaintPhone   = findViewById(R.id.complaintPhone);
        complaintDate    = findViewById(R.id.complaintDate);
        complaintTime    = findViewById(R.id.complaintTime);
        complaintBusNum  = findViewById(R.id.complaintBusNum);
        complaintContent = findViewById(R.id.complaintContent);

        calendar         = Calendar.getInstance();
        DBHelper         = new TestDB(getApplicationContext());

        //마쉬멜로우 이상일 경우에는 권한 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //SMS 사용 권한, 번호 가져오기
            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            } else {
                requestPermissions(new String[] { Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE}, 1);
            }
        }

        //////////////////////////
        // 사용자 휴대폰 번호 가져오기
        //////////////////////////
        TelephonyManager telManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        String phoneNum = telManager.getLine1Number();
        if(phoneNum.startsWith("+82"))
            phoneNum.replace("+82", "0");

        complaintPhone.setText(phoneNum);
    }

    /////////////
    // 이벤트 처리
    /////////////
    @Override
    protected void onResume() {
        super.onResume();

        //////////////////////////////////////////////////////////////////////////
        // 날짜 항목에 포커스에 주어진 경우
        //   - DatePickerDialog 출력
        //   - 날짜를 선택하고 확인을 누르면 YYYY-mm-dd 포맷으로 해당 항목에 날짜 값이 들어감
        //   - 오늘 이후의 날짜는 선택할 수 없음
        //////////////////////////////////////////////////////////////////////////
        complaintDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    DatePickerDialog date = new DatePickerDialog(ComplaintAddActivity.this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                        @SuppressLint("DefaultLocale")
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            complaintDate.setText(String.format("%d-%02d-%02d", year, month + 1, dayOfMonth));
                            complaintTime.requestFocus();
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    date.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                    date.show();
                    complaintDate.clearFocus();
                }
            }
        });

        //////////////////////////////////////////////////////////////////////////
        // 시간 항목에 포커스에 주어진 경우
        //   - TimePickerDialog 출력
        //   - 시간을 선택하고 확인을 누르면 hh시 mm분 포맷으로 해당 항목에 시간 값이 들어감
        //////////////////////////////////////////////////////////////////////////
        complaintTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    TimePickerDialog time = new TimePickerDialog(ComplaintAddActivity.this,android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            complaintTime.setText(String.format("%02d시 %02d분", hourOfDay, minute));
                            complaintContent.requestFocus();
                        }
                    }, calendar.get(Calendar.HOUR),calendar.get(Calendar.MINUTE),false);
                    time.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    time.show();
                    complaintTime.clearFocus();
                }
            }
        });

        ////////////////////////////////////////
        // 뒤로가기 버튼으로 클릭 시 메인 화면으로 이동
        ////////////////////////////////////////
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ////////////////////////////////////////
        // 접수 버튼 클릭 시 불편신고 접수
        //   - 데이터 유효성 검사
        //   - 입력된 정보를 DB에 저장
        //   - 입력된 정보를 SMS를 통해 다산콜센터에 접수
        //   - SMS 길이제한으로 인해 나눠서 전송
        ////////////////////////////////////////
        complaintAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String name    = complaintName.getText().toString();
                    String busNum  = complaintBusNum.getText().toString();
                    String content = complaintContent.getText().toString();
                    String setDate = complaintDate.getText().toString();
                    String setTime = complaintTime.getText().toString();

                    //값이 비어있는지 확인
                    checkInputNull(new String[]{name, busNum, content, setDate, setTime});

                    String year    = setDate.substring(0, 4);
                    String month   = setDate.substring(5, 7);
                    String date    = setDate.substring(8, 10);
                    String hour    = setTime.substring(0, 2);
                    String minute  = setTime.substring(4, 6);

                    SQLiteDatabase dbSQL = DBHelper.getWritableDatabase();
                    dbSQL.execSQL("INSERT INTO complaintsTB VALUES(null, ?, ?, ?, ?, ?, ?, ?)",
                            new Object[] {busNum, content, year, month, date, hour, minute});
                    dbSQL.close();

                    //문자 메시지 전송
                    sendMessage();

                } catch(NullPointerException e){
                    Toast.makeText(ComplaintAddActivity.this, "모든 정보를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //////////////////////////////////////
    // 입력된 정보 중 비어있는 값이 있는지 확인
    //////////////////////////////////////
    private void checkInputNull(String[] strList) throws NullPointerException {
        for(String str : strList){
            if(str.length() == 0)
                throw new NullPointerException();
        }
    }

    /*
     * 입력된 정보를 SMS를 이용하여 다산콜센터로 전송
     * SMS는 최대 70자 까지 전송할 수 있으므로 나눠서 전송
     */
    private void sendMessage(){
        data1 += "이름:" + complaintName.getText() + "\n";
        data1 += "번호:" + complaintPhone.getText() + "\n";
        data1 += "날짜:" + complaintDate.getText() + "\n";
        data1 += "시간:" + complaintTime.getText().toString().replace("시 ", ":").replace("분", "") + "\n";
        data1 += "버스:" + complaintBusNum.getText();
        data2 += complaintContent.getText();

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("등록된 정보로 접수 하시겠습니까?");
        dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {@Override public void onClick(DialogInterface dialog, int which) {}});
        dialog.setPositiveButton("접수", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage("01042809120", null, data1, null, null);
                smsManager.sendTextMessage("01042809120", null, data2, null, null);

                Toast.makeText(getApplicationContext(), "불편신고가 접수 되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        dialog.show();
    }

    //////////////////////////////
    // SMS 전송 권한 확인
    //////////////////////////////
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
