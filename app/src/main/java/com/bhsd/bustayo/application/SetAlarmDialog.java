package com.bhsd.bustayo.application;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import com.bhsd.bustayo.MainActivity;

import java.util.ArrayList;

public class SetAlarmDialog {

    MainActivity activity;
    Context context;
    DialogListener dialogListener;

    public interface DialogListener{
        void setAlarm(int data);
    }

    public void Dialog(final Context context, final Activity activity, final DialogListener dialogListener){
        final String[] Difficulty = {"3 정류장 전 알림", "2 정류장 전 알림", "1 정류장 전 알림"};
        final Integer[] sec = {3,2,1};
        final ArrayList<Integer> selectedItem = new ArrayList<Integer>();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("알림 설정");
        selectedItem.add(3);
        this.activity = (MainActivity)activity;
        this.context = context;
        this.dialogListener = dialogListener;

        builder.setSingleChoiceItems(Difficulty, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedItem.clear();
                selectedItem.add(sec[which]);
            }
        });

        builder.setPositiveButton("시작하기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //알림 설정해주기
                Toast.makeText(context, selectedItem.get(0) + "정류장 전으로 설정되었습니다.", Toast.LENGTH_SHORT).show();
                dialogListener.setAlarm(selectedItem.get(0));
            }
        });

        builder.setNegativeButton("돌아가기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //창 닫기
                dialogListener.setAlarm(0);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
