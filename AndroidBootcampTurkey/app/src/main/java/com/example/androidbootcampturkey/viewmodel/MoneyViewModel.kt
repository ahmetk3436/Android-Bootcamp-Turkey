package com.example.androidbootcampturkey.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.androidbootcampturkey.database.MoneyDatabase
import com.example.androidbootcampturkey.model.Model
import com.example.androidbootcampturkey.repository.MoneyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoneyViewModel(application: Application) : AndroidViewModel(application) {
    //money
    val readAllData: LiveData<List<Model>>
    private val repository: MoneyRepository

    init {
        val moneyDao = MoneyDatabase.getDatabase(application).moneyDao()
        repository = MoneyRepository(moneyDao)
        readAllData = repository.readAllData
    }

    fun addMoney(money: Model) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMoney(money)
        }
    }

    fun deleteMoney2() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteMoney2()
        }
    }
}
