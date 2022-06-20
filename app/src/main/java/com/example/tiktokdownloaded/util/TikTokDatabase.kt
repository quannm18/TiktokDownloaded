package com.example.tiktokdownloaded.util

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tiktokdownloaded.dao.TikTokDAO
import com.example.tiktokdownloaded.model.TikTokEntity

@Database(entities = [TikTokEntity::class], version = 1, exportSchema = false)
abstract class TikTokDatabase : RoomDatabase() {
    abstract fun tikTokDAO(): TikTokDAO

    companion object {
        @Volatile
        private var INSTANCE : TikTokDatabase ? =null

        fun getDatabase(context: Context): TikTokDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TikTokDatabase::class.java,
                    "tiktok_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}