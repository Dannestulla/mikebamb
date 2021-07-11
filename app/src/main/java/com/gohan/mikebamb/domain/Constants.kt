package com.gohan.mikebamb.domain

import com.gohan.mikebamb.data.local.EquipmentEntity

class EquipmentConstants {
    companion object myConstants {
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