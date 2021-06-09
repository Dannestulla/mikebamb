package com.example.mikebamb.presenter.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.mikebamb.data.EquipmentsRepository
import com.example.mikebamb.data.local.EquipmentEntity
import com.example.mikebamb.presenter.adapter.EquipmentAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EquipmentListViewModel @Inject constructor(
    val app : Application,
    val repository: EquipmentsRepository
) : AndroidViewModel(app) {
    var listFromDB = ArrayList<EquipmentEntity>()
    var recyclerViewItems = MutableLiveData<List<EquipmentEntity>>()
    var mAdapter = EquipmentAdapter()

    fun getListFromDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            listFromDB = repository.getListFromDatabase() as ArrayList<EquipmentEntity>
            recyclerViewItems.postValue(listFromDB)
            mAdapter = EquipmentAdapter()
        }
    }
}