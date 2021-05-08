package com.example.androidbootcampturkey.repository

import androidx.lifecycle.LiveData
import com.example.androidbootcampturkey.dao.MoneyDao
import com.example.androidbootcampturkey.model.Model

class MoneyRepository(private val moneyDao: MoneyDao) {

    val readAllData: LiveData<List<Model>> = moneyDao.readAllMoney()

    suspend fun addMoney(Model: Model) {
        moneyDao.addMoney(Model)
    }

    suspend fun deleteMoney2() {
        moneyDao.deleteMoney2()
    }
}