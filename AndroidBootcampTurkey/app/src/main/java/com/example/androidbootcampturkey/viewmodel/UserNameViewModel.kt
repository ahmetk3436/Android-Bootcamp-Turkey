package com.example.androidbootcampturkey.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.androidbootcampturkey.database.UserNameDatabase
import com.example.androidbootcampturkey.model.UserName
import com.example.androidbootcampturkey.repository.UserNameRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserNameViewModel(application: Application) : AndroidViewModel(application) {
    val readAllUser: LiveData<List<UserName>>
    private val repository: UserNameRepository

    init {
        val userDao = UserNameDatabase.getDatabase(application).userDao()
        repository = UserNameRepository(userDao)
        readAllUser = repository.readAllUser
    }

    fun addUser(userName: UserName) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.addUser(userName)
        }
    }

    fun deleteUser() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteUser()
        }
    }
}