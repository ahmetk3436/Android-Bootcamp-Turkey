package com.example.androidbootcampturkey.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "moneys")
data class Model(
    @SerializedName("USD")
    @ColumnInfo(name = "dolar")
    val USD: Double,
    @SerializedName("TRY")
    @ColumnInfo(name = "tl")
    val TRY: Double,
    @SerializedName("EUR")
    @ColumnInfo(name = "euro")
    val EUR: Double,
    @SerializedName("GBP")
    @ColumnInfo(name = "sterlin")
    val GBP: Double
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}