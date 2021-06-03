package com.example.mikebamb.presenter.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mikebamb.data.EquipmentsRepository
import com.example.mikebamb.data.local.EquipmentEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EquipmentListViewModel @Inject constructor(
    val repository: EquipmentsRepository
) : ViewModel() {
    var recyclerViewItems = MutableLiveData<List<EquipmentEntity>>()

    fun addNewItem(newItem: EquipmentEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.addNewItem(newItem)
        }
    }

    fun getListFromDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            val listFromDB = repository.getListFromDatabase()
            recyclerViewItems.postValue(listFromDB)}
    }
}