package com.bhsd.bustayo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class SetAlarmDialog {

    public void Dialog(final Context context){
        final String[] Difficulty = {"3 정류장 전 알림", "2 정류장 전 알림", "1 정류장 전 알림"};
        final Integer[] sec = {3,2,1};
        final ArrayList<Integer> selectedItem = new ArrayList<Integer>();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("알림 설정");
        selectedItem.add(3);

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
                Toast.makeText(context, selectedItem + "정류장 전으로 설정되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("돌아가기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //창 닫기
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
