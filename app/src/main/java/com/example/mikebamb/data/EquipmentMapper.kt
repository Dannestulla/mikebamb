package com.example.mikebamb.data

import com.example.mikebamb.data.local.EquipmentEntity
import com.example.mikebamb.domain.EquipmentModel

fun EquipmentModel.toEquipmentEntity() = EquipmentEntity(
    partNumberModel,
    nameModel,
    modelModel,
    manufacturerModel,
    linkToManualModel,
    fluigModel,
    installDateModel,
    hoursModel,
    qrCodeModel,
    commentsModel,
    category1Model,
    category2Model,
    category3Model,
    observation1Model,
    observation2Model,
    observation3Model,
)