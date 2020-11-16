package com.bhsd.bustayo.application;

import android.util.Log;

import com.firebase.jobdispatcher.JobService;

public class MyJobService extends JobService {

    @Override
    public boolean onStartJob(com.firebase.jobdispatcher.JobParameters job) {
        Log.d("longjob", "Performing long running task in scheduled job");
        //시간이 많이 걸리는거 여기 코딩
        return false;
    }

    @Override
    public boolean onStopJob(com.firebase.jobdispatcher.JobParameters job) {
        return false;
    }
}
