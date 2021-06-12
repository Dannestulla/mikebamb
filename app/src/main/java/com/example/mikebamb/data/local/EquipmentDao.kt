package com.example.mikebamb.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface EquipmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewItemLocal(newEquip: EquipmentEntity)

    @Query("DELETE FROM equipment WHERE part_number =:partNumber")
    suspend fun deleteEquipment(partNumber: String)

    @Query("SELECT * FROM equipment WHERE part_number=:partNumber")
    suspend fun getEquipmentByPartNumber(partNumber: String): EquipmentEntity

    @Query("SELECT EXISTS(SELECT * FROM equipment WHERE part_number=:partNumber)")
    suspend fun doesEquipExists(partNumber: String): Boolean

    @Query("SELECT * FROM equipment")
    fun getEquipmentsFromDatabase() : List<EquipmentEntity>

    @Query("SELECT * FROM equipment WHERE qr_code=:qrCode")
    fun getEquipmentByQRcode(qrCode: String) : EquipmentEntity

    @Query("SELECT qr_code FROM equipment")
    fun printAllQrCodes() : List<String>

    @Query("SELECT category1 FROM equipment")
    fun getMainCategory() : List<String>

    @Query("SELECT category2 FROM equipment WHERE category1=:subCategory")
    fun getSubCategory(subCategory : String) : List<String>

    @Query("SELECT category2 FROM equipment WHERE category2=:subSubCategory")
    fun getSubSubCategory(subSubCategory : String) : List<String>

    @Query("SELECT timestamp FROM equipment ORDER BY timestamp DESC")
    fun getTimestamp() : List<String>

}