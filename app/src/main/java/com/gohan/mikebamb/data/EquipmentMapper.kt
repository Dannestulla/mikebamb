package com.gohan.mikebamb.data

import com.gohan.mikebamb.data.local.EquipmentEntity
import com.gohan.mikebamb.data.remote.EquipmentRemote
import com.gohan.mikebamb.domain.EquipmentModel

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

