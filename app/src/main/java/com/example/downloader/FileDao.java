package com.example.downloader;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface FileDao {
    @Insert
    void insert(FileEntity file);

    @Query("DELETE FROM FileEntity")
    void deleteAll();
}
