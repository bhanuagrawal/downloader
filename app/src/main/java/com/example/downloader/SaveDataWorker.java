package com.example.downloader;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class SaveDataWorker extends Worker {
    public static final String KEY_FILE_DATA = "asdfasdf";
    public static final String KEY_FILE_NAME = "asdSDasdASD";

    public SaveDataWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            /*String fileData = getInputData().getString(KEY_FILE_DATA);*/

            String fileName = getInputData().getString(KEY_FILE_NAME);
            byte[] fileData = Cache.getFileData().get(fileName);
            AppDatabase.getAppDatabase(getApplicationContext()).fileDao().insert(new FileEntity(fileName, fileData));
            Cache.getFileData().remove(fileName);
            Data outputData = new Data.Builder()
                    .putString(Constants.KEY_LOG, fileName +  " saved")
                    .build();
            return Result.success(outputData);
        } catch (Exception exception) {
            return Result.failure();
        }
    }
}
