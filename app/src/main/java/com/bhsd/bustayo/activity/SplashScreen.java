package com.bhsd.bustayo.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bhsd.bustayo.MainActivity;
import com.bhsd.bustayo.R;

public class SplashScreen extends Activity {
    Thread splashThread;

    public void onAttachedToWindow(){
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        StartAnimations();

        init();
    }
    void init() {
        SharedPreferences refreshSec = getSharedPreferences("setting", 0);

        if(refreshSec.getInt("refresh", 0) == 0) {  // 새로고침 간격이 설정되어있지 않으면
            SharedPreferences.Editor editor = refreshSec.edit();
            editor.putInt("refresh", 30000);
            editor.apply();
        }
    }
    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.splash_mov);
        anim.reset();
        ConstraintLayout con=(ConstraintLayout) findViewById(R.id.con_lay);
        con.clearAnimation();
        con.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.splashimg);
        iv.clearAnimation();
        iv.startAnimation(anim);

        splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 2000) {
                        sleep(100);
                        waited += 100;
                    }
                    //Intent intent = new Intent(Splashscreen.this, MainActivity.class);
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    SplashScreen.this.finish();
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    SplashScreen.this.finish();
                }

            }
        };
        splashThread.start();

    }
}
