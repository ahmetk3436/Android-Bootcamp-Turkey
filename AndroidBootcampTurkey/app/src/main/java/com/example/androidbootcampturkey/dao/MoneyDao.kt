package com.example.androidbootcampturkey.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.androidbootcampturkey.model.Model

@Dao
interface MoneyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMoney(money: Model)

    @Query("SELECT * FROM moneys")
    fun readAllMoney(): LiveData<List<Model>>

    @Update
    suspend fun updateMoney(money: Model)

    @Query("DELETE FROM moneys WHERE id != :id")
    suspend fun deleteMoney(id: Int)

    @Query("DELETE FROM moneys")
    suspend fun deleteMoney2()
}