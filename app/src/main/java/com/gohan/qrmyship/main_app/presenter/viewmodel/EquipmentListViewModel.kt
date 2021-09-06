package com.gohan.qrmyship.main_app.presenter.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.gohan.qrmyship.main_app.data.EquipmentsRepository
import com.gohan.qrmyship.main_app.data.local.EquipmentEntity
import com.gohan.qrmyship.main_app.domain.EquipmentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EquipmentListViewModel @Inject constructor(
    val app: Application,
    repository: EquipmentsRepository
) : AndroidViewModel(app) {
    val equipmentUseCase = EquipmentUseCase(repository)
    var recyclerViewItems = MutableLiveData<List<EquipmentEntity>>()
    var listFromDB = ArrayList<EquipmentEntity>()

    fun localGetAllEquipments() {
        CoroutineScope(Dispatchers.IO).launch {
            listFromDB = equipmentUseCase.localGetAllEquipments() as ArrayList<EquipmentEntity>
            recyclerViewItems.postValue(listFromDB)
        }
    }

    fun localGetASubSubCategory(subSubCategory : String) {
        CoroutineScope(Dispatchers.IO).launch {
            listFromDB = equipmentUseCase.localGetCaregory3items(subSubCategory) as ArrayList<EquipmentEntity>
            recyclerViewItems.postValue(listFromDB)
        }
    }
}