package com.example.androidbootcampturkey.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserName(
    @ColumnInfo(name = "usernames")
    val user_name: String,
    @ColumnInfo(name = "gender_names")
    val gender_name: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}