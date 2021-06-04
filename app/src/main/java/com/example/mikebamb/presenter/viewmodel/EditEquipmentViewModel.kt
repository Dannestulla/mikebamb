package com.example.mikebamb.presenter.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mikebamb.data.EquipmentsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditEquipmentViewModel @Inject constructor(
    val repository: EquipmentsRepository
) : ViewModel() {

}
