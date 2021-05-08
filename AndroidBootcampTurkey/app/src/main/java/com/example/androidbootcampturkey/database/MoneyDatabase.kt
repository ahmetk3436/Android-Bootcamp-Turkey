package com.example.androidbootcampturkey.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.androidbootcampturkey.dao.MoneyDao
import com.example.androidbootcampturkey.model.Model

@Database(entities = [Model::class], version = 1, exportSchema = false)
abstract class MoneyDatabase : RoomDatabase() {
    abstract fun moneyDao(): MoneyDao

    companion object {
        @Volatile
        private var INSTANCE: MoneyDatabase? = null

        fun getDatabase(context: Context): MoneyDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this)
            {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MoneyDatabase::class.java,
                    "money_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}