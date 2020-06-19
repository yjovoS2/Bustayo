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

public class AddComplaintActivity extends AppCompatActivity {

    Calendar calendar = Calendar.getInstance();
    Date today = new Date();

    EditText complaintName, complaintPhone;
    EditText complaintDate, complaintTime;
    EditText complaintTitle, complaintBusNum, complaintContent;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_add);

        complaintName = findViewById(R.id.complaintName);
        complaintPhone = findViewById(R.id.complaintPhone);
        complaintDate = findViewById(R.id.complaintDate);
        complaintTime = findViewById(R.id.complaintTime);
        complaintTitle = findViewById(R.id.complaintTitle);
        complaintBusNum = findViewById(R.id.complaintBusNum);
        complaintContent = findViewById(R.id.complaintContent);
        complaintDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    DatePickerDialog date = new DatePickerDialog(AddComplaintActivity.this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                        @SuppressLint("DefaultLocale")
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            complaintDate.setText(String.format("%d-%02d-%02d", year, month + 1, dayOfMonth));
                        }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    date.getDatePicker().setMaxDate(today.getTime());
                    date.show();

                    complaintDate.clearFocus();
                }
            }
        });

        complaintTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    TimePickerDialog Tp = new TimePickerDialog(AddComplaintActivity.this,android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            complaintTime.setText(String.format("%02d시 %02d분", hourOfDay, minute));
                        }
                    }, calendar.get(Calendar.HOUR),calendar.get(Calendar.MINUTE),false);
                    Tp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    Tp.show();
                    complaintTime.clearFocus();
                }
            }
        });


        final ImageView searchGoback = findViewById(R.id.searchGoBack);
        searchGoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Button complaintRegBtn = findViewById(R.id.complaintRegBtn);
        complaintRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
