package com.example.mikebamb.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "equipment")
data class EquipmentEntity(
    @ColumnInfo(name = "part_number") @PrimaryKey val partNumber: String,
    @ColumnInfo(name = "name") val equip_name: String,
    @ColumnInfo(name = "number") val numberEntity: String,
    @ColumnInfo(name = "model") val model: String,
    @ColumnInfo(name = "manufacturer") val manufacturer: String,
    @ColumnInfo(name = "guide_links") val manual_links: String,
    @ColumnInfo(name = "fluig") val fluig: String,
    @ColumnInfo(name = "install_date") val installdate: String,
    @ColumnInfo(name = "hours") val hoursEntity: String,
    @ColumnInfo(name = "qr_code") val qrCodeEntity: String,
    @ColumnInfo(name = "comments") val comments: String
)
