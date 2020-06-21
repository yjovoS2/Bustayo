package com.bhsd.bustayo.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bhsd.bustayo.R;

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
    private EditText complaintTitle, complaintBusNum, complaintContent;

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
        complaintTitle   = findViewById(R.id.complaintTitle);
        complaintBusNum  = findViewById(R.id.complaintBusNum);
        complaintContent = findViewById(R.id.complaintContent);

        //날짜, 시간 설정 시 사용
        calendar         = Calendar.getInstance();
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

        complaintAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
    }
}
