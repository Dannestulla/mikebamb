package com.example.mikebamb.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "equipment")
data class EquipmentEntity(
    @ColumnInfo(name = "part_number") @PrimaryKey var partNumber: String,
    @ColumnInfo(name = "name") var equipNameEntity: String,
    @ColumnInfo(name = "model") var modelEntity: String,
    @ColumnInfo(name = "manufacturer") var manufacturerEntity: String,
    @ColumnInfo(name = "guide_links") var manualLinksEntity: String,
    @ColumnInfo(name = "fluig") var fluigEntity: String,
    @ColumnInfo(name = "install_date") var installdateEntity: String,
    @ColumnInfo(name = "hours") var hoursEntity: String,
    @ColumnInfo(name = "qr_code") var qrCodeEntity: String,
    @ColumnInfo(name = "comments") var commentsEntity: String
)
