package com.example.mikebamb.data

import com.example.mikebamb.data.local.EquipmentEntity
import com.example.mikebamb.data.remote.EquipmentRemote
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
    observation4Model,
    observation5Model,
    timestamp.toString()
)

/*fun EquipmentRemote.toEquipmentEntity() = EquipmentEntity(
    partNumberRemote,
    guideLinksRemote,
    commentsRemote,
    observations4Remote,
    commentsEntityRemote,
    observations1Remote,
    qrCodeRemote,
    manufacturerRemote,
    fluigRemote,
    installDateRemote,
    modelRemote,
    timestampRemote,
    observations2Remote,
    category1Remote,
    category2Remote,
    observations5Remote,
    hoursRemote,

 )*/

