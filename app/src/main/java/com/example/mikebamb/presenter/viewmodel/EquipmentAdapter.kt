package com.example.mikebamb.presenter.viewmodel


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mikebamb.data.local.EquipmentEntity
import com.example.mikebamb.databinding.CardViewBinding

class EquipmentAdapter :
    ListAdapter<EquipmentEntity, EquipmentAdapter.AdapterViewHolder>(ReceitasComparator()) {
    var onItemClick: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val binding =
            CardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bindView(currentItem)
        }
    }

    inner class AdapterViewHolder(private val binding: CardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(equipment: EquipmentEntity) {
            binding.apply {
                partNumber.text = equipment.partNumber
                equipName.text = equipment.equip_name
            }
            itemView.setOnClickListener { onItemClick!!.invoke(adapterPosition) }
        }
    }

    class ReceitasComparator : DiffUtil.ItemCallback<EquipmentEntity>() {
        override fun areItemsTheSame(oldItem: EquipmentEntity, newItem: EquipmentEntity) =
            oldItem.partNumber == newItem.partNumber

        override fun areContentsTheSame(oldItem: EquipmentEntity, newItem: EquipmentEntity) =
            oldItem == newItem
    }

}

