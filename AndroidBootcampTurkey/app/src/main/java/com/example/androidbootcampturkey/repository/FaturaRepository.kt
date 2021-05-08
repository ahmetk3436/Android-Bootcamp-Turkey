package com.example.androidbootcampturkey.repository

import androidx.lifecycle.LiveData
import com.example.androidbootcampturkey.dao.FaturaDao
import com.example.androidbootcampturkey.model.FaturaData

class FaturaRepository(private val faturaDao: FaturaDao) {
    val readAllFatura: LiveData<List<FaturaData>> = faturaDao.readAllFatura()

    suspend fun addFatura(fatura: FaturaData) {
        faturaDao.addFatura(fatura)
    }

    suspend fun deleteFatura(id: Int) {
        faturaDao.deleteFatura(id)
    }
}