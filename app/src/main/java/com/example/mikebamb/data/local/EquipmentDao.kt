package com.example.mikebamb.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface EquipmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewItem(newEquip: EquipmentEntity)

    @Query("DELETE FROM equipment WHERE part_number =:partNumber")
    suspend fun deleteEquipment(partNumber: String)

    @Query("SELECT * FROM equipment WHERE part_number=:partNumber")
    suspend fun getEquipmentByPartNumber(partNumber: String): EquipmentEntity

    @Query("SELECT * FROM equipment")
    fun getListFromDB() : List<EquipmentEntity>

    @Query("SELECT * FROM equipment WHERE qr_code=:qrCode")
    fun getEquipmentByQRcode(qrCode: String) : EquipmentEntity

    @Query("SELECT qr_code FROM equipment")
    fun printAllQrCodes() : List<String>

}