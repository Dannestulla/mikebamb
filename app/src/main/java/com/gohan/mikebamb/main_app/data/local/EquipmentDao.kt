package com.gohan.mikebamb.main_app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface EquipmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun localAddNewItem(newEquip: EquipmentEntity)

    @Query("DELETE FROM equipment WHERE part_number =:partNumber")
    suspend fun localDeleteEquipment(partNumber: String)

    @Query("SELECT * FROM equipment WHERE part_number=:partNumber")
    suspend fun localGetEquipmentByPartNumber(partNumber: String): EquipmentEntity

    @Query("SELECT EXISTS(SELECT * FROM equipment WHERE part_number=:partNumber)")
    suspend fun localDoesEquipExists(partNumber: String): Boolean

    @Query("SELECT * FROM equipment")
    fun localGetAllEquipments() : List<EquipmentEntity>

    @Query("SELECT * FROM equipment WHERE qr_code=:qrCode")
    fun localGetEquipmentByQRCode(qrCode: String) : EquipmentEntity

    @Query("SELECT qr_code FROM equipment")
    fun localPrintAllQrCodes() : List<String>

    @Query("SELECT category1 FROM equipment")
    fun localGetCategory1() : List<String>

    @Query("SELECT category2 FROM equipment WHERE category1=:subCategory")
    fun localGetCategory2(subCategory : String) : List<String>

    @Query("SELECT category3 FROM equipment WHERE category2=:subSubCategory")
    suspend fun localGetCaregory3(subSubCategory : String) : List<String>

    @Query("SELECT * FROM equipment WHERE category3=:category3")
    suspend fun localGetCaregory3items(category3: String): List<EquipmentEntity>

    @Query("DELETE FROM equipment")
    suspend fun localDeleteAllEquipment()

}