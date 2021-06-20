package com.example.mikebamb.presenter.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mikebamb.data.EquipmentsRepository
import com.example.mikebamb.domain.EquipmentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CategoryViewModel @Inject constructor(
    val app: Application,
    repository: EquipmentsRepository

) : ViewModel() {
    var exitSubCategory = false
    var exitMainCategory = false
    var checkedRemote = false
    lateinit var categorySelected: List<String>
    lateinit var remoteDBdata: MutableCollection<Any>
    private val equipmentUseCase = EquipmentUseCase(repository)
    private var _currentCategory = MutableLiveData<List<String>>()
    var currentCategory = _currentCategory as LiveData<List<String>>

    fun localGetMainCategory() {
        CoroutineScope(IO).launch {
            val mainCategoryList = equipmentUseCase.localGetMainCategory()
            val distinctCategoryList = mainCategoryList.distinct()
            if (distinctCategoryList.isNullOrEmpty()) {
                Log.e("Info", "Your Local Database is Empty!")
            } else {
                _currentCategory.postValue(distinctCategoryList)
                categorySelected = distinctCategoryList
            }
        }
    }

    fun localGetSubCategory(subCategory: String) {
        CoroutineScope(IO).launch {
            val search = equipmentUseCase.localGetSubCategory(subCategory).distinct()
            _currentCategory.postValue(search)
            categorySelected = search
            postSubCategory(search)
        }
    }
    fun localGetSubSubCategory(subSubCategory: String) {
        CoroutineScope(IO).launch {
            val search2 = equipmentUseCase.localGetSubSubCategory(subSubCategory).distinct()
            _currentCategory.postValue(search2)
            categorySelected = search2
            postSubCategory(search2)
            exitSubCategory = true
        }
    }

    private fun postSubCategory(subCategoryList: List<String>) {
        _currentCategory.postValue(ArrayList())
        _currentCategory.postValue(subCategoryList)
        categorySelected = subCategoryList
        exitMainCategory = true
    }

    fun remoteInitializeDatabase() {
        equipmentUseCase.remoteInitializeDatabase()
    }

    fun remoteGetAllData() {
        CoroutineScope(IO).launch {
            equipmentUseCase.remoteGetAllData()
            checkedRemote = true
        }
    }

    fun compareRemoteAndLocalData(remoteDBdata: MutableCollection<Any>) {
        equipmentUseCase.compareRemoteAndLocalData(remoteDBdata)
    }
}

