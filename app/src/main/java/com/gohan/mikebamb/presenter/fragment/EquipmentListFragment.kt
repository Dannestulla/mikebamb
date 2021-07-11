package com.gohan.mikebamb.presenter.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gohan.mikebamb.R
import com.gohan.mikebamb.data.local.EquipmentEntity
import com.gohan.mikebamb.databinding.FragmentEquipmentListBinding
import com.gohan.mikebamb.presenter.adapter.EquipmentAdapter
import com.gohan.mikebamb.presenter.viewmodel.EquipmentListViewModel


class EquipmentListFragment : Fragment() {
    private var _binding: FragmentEquipmentListBinding? = null
    private val binding get() = _binding!!
    private var mAdapter = EquipmentAdapter(ArrayList())
    private val viewModel by activityViewModels<EquipmentListViewModel>()
    private val args: EquipmentListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEquipmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val subSubCategory = args.equipmentsList
        setupRecyclerView()

        if (subSubCategory.isBlank()) {
            viewModel.localGetAllEquipments()
        } else {
            binding.editName.text = subSubCategory
            viewModel.localGetASubSubCategory(subSubCategory)
        }

        setHasOptionsMenu(true)
        appBarInit()


    }

    private fun appBarInit() {
        val appBar = (activity as AppCompatActivity)
        appBar.setSupportActionBar(binding.toolbar)
        appBar.title = "Search:"
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_search_view, menu)
        val item = menu.findItem(R.id.action_search)
        val searchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                mAdapter.filter.filter(newText)
                return false
            }
        })
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
        mAdapter.onItemClick = { position ->
            navigateToDescription(position)
        }
        viewModel.recyclerViewItems.observe(
            viewLifecycleOwner,
            {
                mAdapter.submitList(it)
                mAdapter.allEquipmentList = it as MutableList<EquipmentEntity>
            })
    }

    private fun navigateToDescription(position: Int) {
        val partNumberClicked = viewModel.recyclerViewItems.value?.get(position)?.partNumber!!
        val action =
            EquipmentListFragmentDirections.actionEquipmentListFragmentToDescriptionEquipmentFragment(
                partNumberClicked
            )
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}