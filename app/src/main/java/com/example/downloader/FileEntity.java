package com.example.downloader;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class FileEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "fileid")
    @NonNull
    private int fileId;


    @ColumnInfo(name = "filename")
    private String fileName;


    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] data;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public FileEntity(String fileName, byte[] data) {
        this.fileName = fileName;
        this.data = data;
    }
}
