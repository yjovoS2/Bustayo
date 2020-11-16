package com.bhsd.bustayo.application;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.bhsd.bustayo.MainActivity;
import com.bhsd.bustayo.R;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM";

    public MyFirebaseMessagingService(){
    }
//
//
//    @Override
//    public void onNewToken(@NonNull String s) {
//        super.onNewToken(s);
//
//        Log.e("토큰토큰", "onNewToken 호출 : " + s);
//    }
//
//    @Override
//    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
//
//        //받은 데이터 중, 내용만 가지고와 출려하는 메소드
//        Log.d("받은데이터", " data : " + remoteMessage.getData());
//    }

    //    푸시 알림 설정
    private String title = "";
    private String body = "";

    //start receive_message

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //여기서 FCM메시지 핸들링
        Log.d("어디서오니?", "From : " + remoteMessage.getFrom());

        //푸시알림 메시지 분기
        //putData를 사용했을 때 data가져오기
        if (remoteMessage.getData().size() > 0) {
            Log.d("메시지 데이타 페이로드", "Message data payload : " + remoteMessage.getData());
            title = remoteMessage.getData().get("title");

            Log.d("받은데이터", "title : " + title);

//            if (/*10초가 넘어가는 작업*/ true) {
//                scheduleJob();
//            } else {
//                //아니면
//                handleNow();
//            }
        }

        //Notification 사용했을 때 data 가져오기
        if (remoteMessage.getNotification() != null) {
            Log.d("notification", "Message Notification title : " + remoteMessage.getNotification().getTitle());
            Log.d("notification", "Message Notification body : " + remoteMessage.getNotification().getBody());
            Log.d("notification", "Message Notification data : " + remoteMessage.getData());
        }
        snedNotification();
        //수신된 결과로 FCM 자체 알림을 생성하려는 경우
    }
    //end receive_message

    //start on_new_token
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("토큰", "Refreshed token : " + s);

        sendRegistrationToServer(s);
    }
    //end on_new_token

    private  void scheduleJob(){
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myjob = dispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag("my-job-tag")
                .build();
        dispatcher.schedule(myjob);
    }

    private void handleNow(){
        Log.d("끄읕?", "Short live task is done");
    }

    private void sendRegistrationToServer(String token){
        //우리앱서버로 토큰을 보내야함
    }

    private void snedNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = "채널 아이디";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBouilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_HIGH);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //Since android Oreo notification channel is needed
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBouilder.build());
    }
}
