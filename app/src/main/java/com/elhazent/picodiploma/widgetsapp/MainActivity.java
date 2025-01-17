package com.elhazent.picodiploma.widgetsapp;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnStart;
    Button btnStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String hello = "Hello";

        btnStart = findViewById(R.id.btn_start);
        btnStop = findViewById(R.id.btn_stop);

        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_start:
                startJob();
                break;
            case R.id.btn_stop:
                cancelJob();
                break;
        }
    }

    private static int jobId = 100;
    private static int SCHEDULE_OF_PERIOD = 86000;

    private void startJob(){
        ComponentName serviceComponent = new ComponentName(this, UpdatingWidgetService.class);
        JobInfo.Builder builder = new JobInfo.Builder(jobId, serviceComponent);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            builder.setMinimumLatency(SCHEDULE_OF_PERIOD);
        } else {
            builder.setPeriodic(SCHEDULE_OF_PERIOD);
        }

        JobScheduler jobScheduler = (JobScheduler)getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());

        Toast.makeText(this, "Job Service Started", Toast.LENGTH_SHORT).show();
    }

    private void cancelJob(){
        JobScheduler tm = (JobScheduler)getSystemService(Context.JOB_SCHEDULER_SERVICE);
        tm.cancel(jobId);
        Toast.makeText(this, "Job Service Canceled", Toast.LENGTH_SHORT).show();
    }
}
