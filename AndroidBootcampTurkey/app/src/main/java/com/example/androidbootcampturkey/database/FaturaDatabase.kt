package com.example.androidbootcampturkey.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.androidbootcampturkey.dao.FaturaDao
import com.example.androidbootcampturkey.model.FaturaData

@Database(entities = [FaturaData::class], version = 1, exportSchema = false)
abstract class FaturaDatabase : RoomDatabase() {
    abstract fun faturaDao(): FaturaDao

    companion object {
        @Volatile
        private var INSTANCE: FaturaDatabase? = null

        fun getDatabase(context: Context): FaturaDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this)
            {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FaturaDatabase::class.java,
                    "fatura_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}