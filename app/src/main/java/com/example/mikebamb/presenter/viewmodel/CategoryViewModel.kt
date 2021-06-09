package com.example.mikebamb.presenter.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mikebamb.domain.EquipmentUseCase
import com.example.mikebamb.presenter.adapter.CategoryAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CategoryViewModel @Inject constructor(
    val app: Application,
    val equipmentUseCase : EquipmentUseCase
) : ViewModel() {
    var exitCategoryMenu = false
    lateinit var categorySelected: List<String>
    var mAdapter = CategoryAdapter()
    var currentCategory = MutableLiveData<List<String>>()

    fun getMainCategory() {
        CoroutineScope(IO).launch {
            val mainCategoryList = equipmentUseCase.getMainCategory().distinct()
            if (mainCategoryList.isNullOrEmpty()) {
                Log.e("Info","Your Local Database is Empty!")
            }
            else {
                postMainCategory(mainCategoryList)
            }
        }
    }

    private fun postMainCategory(mainCategoryList: List<String>) {
        currentCategory.postValue(mainCategoryList)
        categorySelected = mainCategoryList as ArrayList<String>
    }

    fun getSubCategory(subCategory: String) {
        CoroutineScope(IO).launch {
            val search = equipmentUseCase.getSubCategory(subCategory).distinct()
            currentCategory.postValue(search)
            postSubCategory(search)
        }
    }

    private fun postSubCategory(subCategoryList: List<String>) {
        currentCategory.postValue(ArrayList())
        currentCategory.postValue(subCategoryList)
        categorySelected = subCategoryList
        exitCategoryMenu = true
    }

    fun initializeRemoteDatabase() {
        equipmentUseCase.initializeRemoteDatabase()
    }
}