package com.example.mikebamb.presenter.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.mikebamb.data.EquipmentsRepository
import com.example.mikebamb.data.local.EquipmentEntity
import com.example.mikebamb.domain.EquipmentUseCase
import com.example.mikebamb.presenter.adapter.EquipmentAdapter
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
    var mAdapter = EquipmentAdapter()
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
            listFromDB = equipmentUseCase.localGetSubSubCategoryList(subSubCategory) as ArrayList<EquipmentEntity>
            recyclerViewItems.postValue(listFromDB)
        }
    }
}