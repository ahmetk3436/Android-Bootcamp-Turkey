package com.example.androidbootcampturkey.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fatura_bilgileri")
data class FaturaData(
    @ColumnInfo(name = "fatura_tipi")
    val fatura_tipi: String?,
    @ColumnInfo(name = "fatura_aciklamasi")
    var fatura_aciklamasi: String?,
    @ColumnInfo(name = "para_miktari")
    var para_miktari: String?,
    @ColumnInfo(name = "para_birimi")
    var para_birimi: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}