package com.example.mikebamb.presenter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mikebamb.data.local.EquipmentEntity
import com.example.mikebamb.databinding.CardViewCategoryBinding

class CategoryAdapter :
    androidx.recyclerview.widget.ListAdapter<String, CategoryAdapter.AdapterViewHolder>(
        CategoryComparator()
    ) {
    var onItemClick: ((Int) -> Unit)? = null

    class CategoryComparator  : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterViewHolder {
        val binding =
            CardViewCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdapterViewHolder(binding)
    }
    inner class AdapterViewHolder(private val binding: CardViewCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(equipment: String) {
            binding.apply {
                category1.text = equipment
            }
            itemView.setOnClickListener { onItemClick!!.invoke(adapterPosition) }
        }
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bindView(currentItem)
        }
    }
}