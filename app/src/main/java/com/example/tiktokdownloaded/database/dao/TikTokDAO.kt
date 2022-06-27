package com.example.tiktokdownloaded.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tiktokdownloaded.model.TikTokEntity

@Dao
interface TikTokDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addTikTokToDB(tikTokEntity: TikTokEntity);

    @Query("SELECT * FROM tiktok_table ORDER BY id DESC")
    fun readData():LiveData<List<TikTokEntity>>

    @Update
    fun updateTikTok(tikTokEntity: TikTokEntity)

    @Delete
    fun deleteTikTok(tikTokEntity: TikTokEntity)

    @Query("DELETE FROM tiktok_table")
    fun deleteAllTikTok()
}