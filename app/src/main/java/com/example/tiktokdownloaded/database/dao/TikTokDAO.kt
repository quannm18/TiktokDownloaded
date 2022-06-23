package com.example.tiktokdownloaded.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tiktokdownloaded.model.TikTokEntity

@Dao
interface TikTokDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addTikTokToDB(tikTokEntity: TikTokEntity);

    @Query("SELECT * FROM tiktok_table ORDER BY id DESC")
    fun readData():LiveData<List<TikTokEntity>>


}