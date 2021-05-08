package com.example.androidbootcampturkey.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.androidbootcampturkey.model.UserName

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(userName: UserName)

    @Query("SELECT * FROM users")
    fun readAllUser(): LiveData<List<UserName>>

    @Query("DELETE FROM users")
    suspend fun deleteUser()

}