package com.example.androidbootcampturkey.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.androidbootcampturkey.database.FaturaDatabase
import com.example.androidbootcampturkey.model.FaturaData
import com.example.androidbootcampturkey.repository.FaturaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FaturaViewModel(application: Application) : AndroidViewModel(application) {
    val readAllFatura: LiveData<List<FaturaData>>
    private val repository: FaturaRepository

    init {
        val faturaDao = FaturaDatabase.getDatabase(application).faturaDao()
        repository = FaturaRepository(faturaDao)
        readAllFatura = repository.readAllFatura
    }

    fun addFatura(fatura: FaturaData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addFatura(fatura)
        }
    }

    fun deleteFatura(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFatura(id)
        }
    }
}