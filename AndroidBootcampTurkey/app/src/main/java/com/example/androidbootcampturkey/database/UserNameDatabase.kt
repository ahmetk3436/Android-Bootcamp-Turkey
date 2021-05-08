package com.example.androidbootcampturkey.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.androidbootcampturkey.dao.UserDao
import com.example.androidbootcampturkey.model.UserName

@Database(entities = [UserName::class], version = 1, exportSchema = false)
abstract class UserNameDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserNameDatabase? = null

        fun getDatabase(context: Context): UserNameDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this)
            {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserNameDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}