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

    //날짜 관련
    private Calendar calendar;

    //사용자 입력 정보
    private EditText complaintName, complaintPhone;
    private EditText complaintDate, complaintTime;
    private EditText complaintBusNum, complaintContent;

    private ImageView goBack;    //뒤로가기 버튼
    private Button complaintAdd; //접수 버튼

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

        //날짜, 시간 설정 시 사용
        calendar         = Calendar.getInstance();

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
        //   - 입력된 정보를 SMS를 통해 다산콜센터에 접수
        //   - SMS 길이제한으로 인해 나눠서 전송
        ////////////////////////////////////////
        complaintAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data1 = "";
                String data2 = "";

                data1 += "이　　름 : " + complaintName.getText() + "\n"; // 8 + a 이름 최대 10자
                data1 += "전화번호 : " + complaintPhone.getText() + "\n"; // 8 + 11 = 19
                data1 += "날　　짜 : " + complaintDate.getText() + "\n"; // 8 + 10 = 18
                data1 += "시　　간 : " + complaintTime.getText() + "\n"; // 8 + 7 = 15   =

                data2 += "버스번호 : " + complaintBusNum.getText() + "\n"; // 8 + a
                data2 += complaintContent.getText() + "\n"; //최대 50

                //사용자 데이터 유효성 검사
                //디비 연결하고 등록
/*
                try {
                    Pattern ps = Pattern.compile("^[ㄱ-ㅎ가-힣]+$");
                    if (!ps.matcher(source).matches()) {
                        return "";
                    }
                    return null;


                } catch(Exception e){

                }
 */

                send(data1, data2);
            }
        });
    }

    //////////////////////////////
    // SMS 전송 권한 확인
    //////////////////////////////
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /*
     * 입력된 정보를 SMS를 이용하여 다산콜센터로 전송
     * SMS는 최대 70자 까지 전송할 수 있으므로 나눠서 전송
     */
    private void send(final String data1, final String data2){
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
}
