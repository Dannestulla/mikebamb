package com.gohan.mikebamb.main_app.domain

import com.gohan.mikebamb.main_app.data.local.EquipmentEntity

class EquipmentConstants {
    companion object myConstants {
        val SHIP_ID = "ShipId"
        var ADMIN_LIST = arrayListOf("dannestulla@gmail.com", "rochaparedes@gmail.com")
        var SHARED_PREF = "SharedPref"
        var EMAIL = "email"
        var PASSWORD = "password"
        var USER = true
        var EMPTY_EQUIPMENT_ENTITY = EquipmentEntity(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "")
    }
}