package com.example.downloader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.common.io.ByteStreams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;


public class DownloadWorker extends Worker {


    public static final String KEY_FILE_NANE = "sdfasdf";
    public static final String DOWNLOAD_URL = "http://www.mso.anu.edu.au/~ralph/OPTED/v003/wb1913_";
    public static final String FILE_DATA_KEY = "hjg";


    public DownloadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String fileName = getInputData().getString(KEY_FILE_NANE);

        try {
            URL url = new URL( DOWNLOAD_URL + fileName);
            Cache.getFileData().put(fileName, ByteStreams.toByteArray(url.openStream()));
            Data outputData = new Data.Builder()
                    .putString(Constants.KEY_LOG, fileName  + " downloaded")
                    .build();
            return Result.success(outputData);
        } catch (MalformedURLException e) {
            return Result.failure();
        } catch (IOException e) {
            return Result.failure();
        } catch (Exception e) {
            return Result.failure();
        }
    }
}
