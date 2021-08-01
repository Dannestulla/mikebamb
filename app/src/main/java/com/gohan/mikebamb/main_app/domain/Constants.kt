package com.gohan.mikebamb.main_app.domain

import com.gohan.mikebamb.main_app.data.local.EquipmentEntity

class EquipmentConstants {
    companion object myConstants {
        val NEW_SHIP_ACCOUNT = "New_Ship_Account"
        var COLLECTION_NAME = ""
        val SHIP_ID = "ShipId"
        val SHIP_EMAIL = "ShipEmail"
        val SHIP_PASSWORD = "VesselPassword"
        var ADMIN_LIST = arrayListOf("dannestulla@gmail.com", "rochaparedes@gmail.com")
        var SHARED_PREF = "SharedPref"
        var EMAIL = "email"
        var PASSWORD = "password"
        var USER = false
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