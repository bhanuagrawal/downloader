package com.example.downloader;

import java.util.HashMap;
import java.util.Map;

public class Cache {

    private static Map<String, byte[]> fileData;

    public static Map<String, byte[]> getFileData() {
        if(fileData == null){
            fileData = new HashMap<>();
        }
        return fileData;
    }
}
