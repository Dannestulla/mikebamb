package com.gohan.mikebamb.main_app.data.local

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
    @ColumnInfo(name = "comments") var commentsEntity: String,
    @ColumnInfo(name = "category1") var category1Entity: String,
    @ColumnInfo(name = "category2") var category2Entity: String,
    @ColumnInfo(name = "category3") var category3Entity: String,
    @ColumnInfo(name = "observations1") var observations1Entity: String,
    @ColumnInfo(name = "observations2") var observations2Entity: String,
    @ColumnInfo(name = "observations3") var observations3Entity: String,
    @ColumnInfo(name = "observations4") var observations4Entity: String,
    @ColumnInfo(name = "observations5") var observations5Entity: String,
    @ColumnInfo(name = "timestamp") var timestampEntity: String
)
