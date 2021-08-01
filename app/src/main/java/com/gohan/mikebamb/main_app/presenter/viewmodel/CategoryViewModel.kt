package com.gohan.mikebamb.main_app.presenter.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gohan.mikebamb.main_app.data.EquipmentsRepository
import com.gohan.mikebamb.main_app.domain.EquipmentConstants
import com.gohan.mikebamb.main_app.domain.EquipmentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CategoryViewModel @Inject constructor(
    val app: Application,
    repository: EquipmentsRepository
) : ViewModel() {
    var firstStart = true
    var exitToSubSubCategory= false
    lateinit var categorySelected: List<String>
    lateinit var secondCategorySelected: List<String>
    lateinit var thirdCategorySelected: List<String>
    var exitToSubCategory = false
    var exitToMainCategory = false
    var checkedRemote = false
    private val equipmentUseCase = EquipmentUseCase(repository)
    private var _currentCategory = MutableLiveData<List<String>>()
    var currentCategory = _currentCategory as LiveData<List<String>>
    var subCategory = " "
    var subSubCategory = " "
    var isObserverStarted = false

    fun localGetMainCategory() {
        CoroutineScope(IO).launch {
            val mainCategoryList = equipmentUseCase.localGetCategory1()
            val distinctCategoryList = mainCategoryList.distinct()
            if (distinctCategoryList.isNullOrEmpty()) {
            } else {
                _currentCategory.postValue(distinctCategoryList)
                categorySelected = distinctCategoryList
            }
        }
    }

    fun localGetSubCategory(subCategory: String) {
        CoroutineScope(IO).launch {
            val search = equipmentUseCase.localGetCategory2(subCategory).distinct()
            _currentCategory.postValue(search)
            secondCategorySelected = search
            postSubCategory(search)
        }
        this.subCategory = subCategory
    }

    fun localGetSubSubCategory(subSubCategory: String) {
        CoroutineScope(IO).launch {
            val search2 = equipmentUseCase.localGetCaregory3(subSubCategory).distinct()
            _currentCategory.postValue(search2)
            thirdCategorySelected = search2
            postSubCategory(search2)
            exitToSubCategory = true
        }
        this.subSubCategory = subSubCategory
    }

    private fun postSubCategory(subCategoryList: List<String>) {
        _currentCategory.postValue(ArrayList())
        _currentCategory.postValue(subCategoryList)
        categorySelected = subCategoryList
        exitToMainCategory = true
    }

    fun remoteInitializeDatabase() {
        equipmentUseCase.remoteInitializeDatabase(getShipId())
    }

    fun remoteGetAllData() {
        CoroutineScope(IO).launch {
            equipmentUseCase.remoteGetAllData()
            checkedRemote = true
        }
    }

    fun compareRemoteAndLocalData(remoteDBdata: MutableCollection<Any>) {
        CoroutineScope(IO).launch {
        equipmentUseCase.compareRemoteAndLocalData(remoteDBdata)
        localGetMainCategory()}
    }

    private fun getShipId(): String {
        val sharedPref = app.getSharedPreferences(EquipmentConstants.SHARED_PREF, Context.MODE_PRIVATE)
        val shipId = sharedPref?.getString(EquipmentConstants.SHIP_ID, "Empty")!!
        return shipId
    }

    fun localDeleteAllData() {
        equipmentUseCase.localDeleteAllData()
    }

    fun writeOfflineItemsInCache() {
        equipmentUseCase.writeOfflineItemsInCache()
    }
}

