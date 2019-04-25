package com.example.downloader;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

public class DownloadViewModel extends AndroidViewModel {

    ArrayList<String> filesToDownload;
    MutableLiveData<String> log;

    public ArrayList<String> getFilesToDownload() {
        return filesToDownload;
    }



    public MutableLiveData<String> getLog() {
        if(log == null){
            log = new MutableLiveData<>();
        }
        return log;
    }

    public void setLog(MutableLiveData<String> log) {
        this.log = log;
    }

    public DownloadViewModel(@NonNull Application application) {
        super(application);
        filesToDownload = new ArrayList<>();
        generateFileNames(filesToDownload);
    }

    private void generateFileNames(ArrayList<String> files) {
        for (Character ch = 'a'; ch <= 'z'; ch++){
            files.add(ch+ ".html");
        }
    }

    public LiveData<List<WorkInfo>> startBulkDownload() {

        List<WorkContinuation> downloadAndSaveRequests = new ArrayList<>();
        for(String fileName: filesToDownload) {
            downloadAndSaveRequests.add(WorkManager.getInstance().beginWith(getDownloadWork(fileName)).then(getSaveWork(fileName)));
        }

        WorkContinuation workContinuation = WorkContinuation.combine(downloadAndSaveRequests);
        workContinuation.enqueue();
        return workContinuation.getWorkInfosLiveData();
    }

    private OneTimeWorkRequest getSaveWork(String fileName) {
        OneTimeWorkRequest saveWork =
                new OneTimeWorkRequest.Builder(SaveDataWorker.class)
                        .setInputData(createSaveDataWorkerData(fileName))
                        .build();


        return saveWork;

    }

    private Data createSaveDataWorkerData(String fileName) {
        Data.Builder builder = new Data.Builder();
        builder.putString(SaveDataWorker.KEY_FILE_NAME, fileName);
        return builder.build();
    }

    private OneTimeWorkRequest getDownloadWork(String fileName) {
        OneTimeWorkRequest downloadWork =
                new OneTimeWorkRequest.Builder(DownloadWorker.class)
                        .setInputData(createDownloadWorkerData(fileName))
                        .build();


        return downloadWork;

    }

    private Data createDownloadWorkerData(String fileName) {
        Data.Builder builder = new Data.Builder();
        builder.putString(DownloadWorker.KEY_FILE_NANE, fileName);
        return builder.build();
    }

    public void cancelBulkDownload() {
        WorkManager.getInstance().cancelAllWork();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                AppDatabase.getAppDatabase(getApplication().getApplicationContext()).fileDao().deleteAll();
            }
        });
    }
}
