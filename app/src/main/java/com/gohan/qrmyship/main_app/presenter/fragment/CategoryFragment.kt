package com.gohan.qrmyship.main_app.presenter.fragment

import android.app.AlertDialog
import android.content.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.gohan.qrmyship.R
import com.gohan.qrmyship.databinding.FragmentCategoryBinding
import com.gohan.qrmyship.main_app.data.remote.CloudFirestore.Companion.documentsLiveData
import com.gohan.qrmyship.main_app.domain.myConstants.EMAIL
import com.gohan.qrmyship.main_app.domain.myConstants.NEW_SHIP_ACCOUNT
import com.gohan.qrmyship.main_app.domain.myConstants.SHARED_PREF
import com.gohan.qrmyship.main_app.domain.myConstants.VESSEL_ID
import com.gohan.qrmyship.main_app.presenter.adapter.CategoryAdapter
import com.gohan.qrmyship.main_app.presenter.viewmodel.CategoryViewModel
import kotlin.properties.Delegates

class CategoryFragment : Fragment() {
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<CategoryViewModel>()
    private var mAdapter = CategoryAdapter()
    private var positionMain by Delegates.notNull<Int>()
    private var positionSubSub by Delegates.notNull<Int>()
    private var positionSub by Delegates.notNull<Int>()
    private var subCategory = " "
    private var subSubCategory = " "
    private lateinit var sharedPref : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        applyBinding()
        viewModel.remoteInitializeDatabase()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.currentCategory.observe(viewLifecycleOwner, {
            mAdapter.submitList(it)
        })
        loadSharedPref()
        deleteAllDataIfDifferentVesselAccount()
        localGetMainCategory()
        getOnlineDataOnce()
        mAdapter.onItemClick = { position ->
            handleItemClick(position)
        }
        writeOfflineItemsInCache()
        onBackPressed()
    }

    private fun deleteAllDataIfDifferentVesselAccount() {

        if (sharedPref.getBoolean("New_Ship_Account", false)) {
            viewModel.localDeleteAllData()
        }
    }

    private fun writeOfflineItemsInCache() {
        if (viewModel.firstStart) {
            viewModel.writeOfflineItemsInCache()
        }
    }

    private fun getOnlineDataOnce() {
        if (viewModel.firstStart) {
            viewModel.remoteGetAllData()
            observeRemoteChangesAndCompareRemoteToLocal()
            viewModel.firstStart = false
        }
    }

    private fun applyBinding() {
        binding.apply {
            categoryRecyclerview.apply {
                adapter = mAdapter
                layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            }
            /*accountInfo.setOnClickListener {
                accountInfoDialogBox()
            }*/
            /*account.setOnClickListener {
                copyToClipBoard(binding.account.text)
                Toast.makeText(context, "Ship Code Copied to Clipboard!", Toast.LENGTH_LONG).show()
            }*/
        }
    }

    private fun accountInfoDialogBox() {
        val vesselEmail = sharedPref.getString(VESSEL_ID, "")
        val userEmail = sharedPref.getString(EMAIL, "")
        val builder = AlertDialog.Builder(context)
        builder.setMessage("Vessel Email: $vesselEmail \n User Email: $userEmail")
            .setPositiveButton("Ok") { _, _ -> }
        val alert = builder.create()
        alert.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(resources.getColor(R.color.white))
        alert.show()
    }

    private fun copyToClipBoard(accountId: CharSequence?) {
        val clipboard = getSystemService(
            requireContext(),
            ClipboardManager::class.java
        )
        val clip = ClipData.newPlainText("ShipAccount:", accountId)
        clipboard?.setPrimaryClip(clip)
    }

    private fun loadSharedPref() {
        sharedPref =
            context?.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)!!
        if (sharedPref.getBoolean(NEW_SHIP_ACCOUNT, false)) {
            viewModel.localDeleteAllData()
            sharedPref.edit().remove(NEW_SHIP_ACCOUNT).apply()
        }
    }

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (viewModel.exitToSubSubCategory) {
                        getSecondCategory(positionSubSub)
                        viewModel.exitToSubSubCategory = false
                        viewModel.exitToSubCategory = false
                        viewModel.exitToMainCategory = false
                    }
                    if (viewModel.exitToSubCategory) {
                        getSecondCategory(positionSub)
                        viewModel.exitToSubCategory = false
                        viewModel.exitToMainCategory = false
                    }
                    if (viewModel.exitToMainCategory) {
                        localGetMainCategory()
                        viewModel.exitToMainCategory = false
                        binding.editCategory.text = ""
                    }
                }
            })
    }

    private fun handleItemClick(position: Int) {
        this.positionMain = position
        if (!viewModel.exitToMainCategory) {
            getSecondCategory(position)
        } else if (!viewModel.exitToSubCategory) {
            getThirdCategory(position)
        } else {
            navigateToEquipmentList(position)
        }
    }

    private fun localGetMainCategory() {
        viewModel.localGetMainCategory()
    }

    private fun getSecondCategory(position: Int) {
        this.positionSub = position
        if (!viewModel.exitToSubCategory) {
            subCategory = viewModel.categorySelected[position]
            viewModel.localGetSubCategory(subCategory)
            binding.editCategory.text = subCategory
        } else {
            viewModel.localGetSubCategory(subCategory)
            binding.editCategory.text = subCategory
            subCategory = " "
        }
    }

    private fun getThirdCategory(position: Int) {
        this.positionSubSub = position
        if (!viewModel.exitToSubSubCategory) {
            subSubCategory = viewModel.secondCategorySelected[position]
            viewModel.localGetSubSubCategory(subSubCategory)
            binding.editCategory.text = subSubCategory
        } else {
            viewModel.localGetSubSubCategory(subSubCategory)
            binding.editCategory.text = subSubCategory
            subSubCategory = " "
        }
        binding.editCategory.text = subSubCategory
    }

    private fun observeRemoteChangesAndCompareRemoteToLocal() {
        documentsLiveData.observe(viewLifecycleOwner, {
            viewModel.compareRemoteAndLocalData(it)
        })
    }

    private fun navigateToEquipmentList(position: Int) {
        val equipmentCategory = viewModel.categorySelected[position]
        val action = CategoryFragmentDirections.actionCategoryFragmentToEquipmentListFragment(
            equipmentCategory
        )
        findNavController().navigate(action)
    }

    override fun onPause() {
        super.onPause()
        viewModel.exitToMainCategory = false
        viewModel.exitToSubCategory = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}