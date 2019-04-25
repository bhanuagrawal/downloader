package com.example.downloader;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import androidx.work.WorkInfo;
import androidx.work.WorkManager;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private DownloadViewModel downloadViewModel;
    private Button cancelButton;
    private TextView logs;
    private long lStartTime;
    private long lEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button)findViewById(R.id.button2);
        cancelButton = (Button) findViewById(R.id.button4);
        logs = (TextView) findViewById(R.id.logs);

        downloadViewModel = ViewModelProviders.of(MainActivity.this).get(DownloadViewModel.class);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lStartTime = System.nanoTime();
                downloadViewModel.startBulkDownload().observe(MainActivity.this, new Observer<List<WorkInfo>>() {
                    @Override
                    public void onChanged(@Nullable List<WorkInfo> listOfWorkInfos) {

                        if (listOfWorkInfos == null || listOfWorkInfos.isEmpty()) {
                            return;
                        }


                        for(WorkInfo workInfo: listOfWorkInfos){

                            if(workInfo.getTags().toArray()[0].toString().equals("androidx.work.impl.workers.CombineContinuationsWorker")){
                                if (workInfo.getState().isFinished() ) {
                                    if(workInfo.getState() == WorkInfo.State.SUCCEEDED){
                                        lEndTime = System.nanoTime();
                                        long timetaken = lEndTime - lStartTime;
                                        downloadViewModel.getLog().postValue("all files downloaded and saved in " + timetaken/1000000000f+ " seconds");
                                    }
                                    else{
                                        downloadViewModel.getLog().postValue("some error occured ");

                                    }
                                }
                                else{
                                    downloadViewModel.getLog().postValue("downloading and saving ...");
                                }
                            }

                        }
                    }
                });
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadViewModel.getLog().postValue("");
                downloadViewModel.cancelBulkDownload();
            }
        });


        downloadViewModel.getLog().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                logs.setText(s);
            }
        });


    }
}
