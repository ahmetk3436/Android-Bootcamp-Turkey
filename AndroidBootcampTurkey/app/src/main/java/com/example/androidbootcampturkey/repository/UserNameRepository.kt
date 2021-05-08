package com.example.androidbootcampturkey.repository

import androidx.lifecycle.LiveData
import com.example.androidbootcampturkey.dao.UserDao
import com.example.androidbootcampturkey.model.UserName

class UserNameRepository(private val userDao: UserDao) {
    val readAllUser: LiveData<List<UserName>> = userDao.readAllUser()

    suspend fun addUser(userName: UserName) {
        userDao.addUser(userName)
    }

    suspend fun deleteUser() {
        userDao.deleteUser()
    }

}