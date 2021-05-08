package com.example.androidbootcampturkey.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.androidbootcampturkey.model.FaturaData

@Dao
interface FaturaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFatura(money: FaturaData)

    @Query("SELECT * FROM fatura_bilgileri")
    fun readAllFatura(): LiveData<List<FaturaData>>

    @Query("DELETE FROM fatura_bilgileri WHERE ID = :id")
    suspend fun deleteFatura(id: Int)
}