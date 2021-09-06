package com.gohan.qrmyship.main_app.domain

import com.gohan.qrmyship.main_app.data.local.EquipmentEntity

object myConstants {
    const val NEW_SHIP_ACCOUNT = "New_Ship_Account"
    var COLLECTION_NAME = ""
    const val VESSEL_ID = "ShipId"
    const val VESSEL_EMAIL = "ShipEmail"
    const val VESSEL_PASSWORD = "VesselPassword"
    const val SHARED_PREF = "SharedPref"
    const val EMAIL = "email"
    const val PASSWORD = "password"
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
        ""
    )
}
