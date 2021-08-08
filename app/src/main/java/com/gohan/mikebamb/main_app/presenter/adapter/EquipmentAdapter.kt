package com.gohan.mikebamb.main_app.presenter.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gohan.mikebamb.main_app.data.local.EquipmentEntity
import com.gohan.mikebamb.databinding.CardViewEquipmentBinding
import java.util.*


class EquipmentAdapter(var allEquipmentList: MutableList<EquipmentEntity>) :
    ListAdapter<EquipmentEntity, EquipmentAdapter.AdapterViewHolder>(ReceitasComparator()),
    Filterable {
    var onItemClick: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val binding =
            CardViewEquipmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bindView(currentItem)
        }
    }

    inner class AdapterViewHolder(private val binding: CardViewEquipmentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(equipment: EquipmentEntity) {
            binding.apply {
                partNumber.text = equipment.partNumber
                equipName.text = equipment.equipNameEntity
                equipFluig.text = equipment.fluigEntity
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

    override fun getFilter(): Filter {
        return myFilter
    }

    private var myFilter: Filter = object : Filter() {
        //Automatic on background thread
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val filteredList: MutableList<EquipmentEntity> = ArrayList()
            if (charSequence.isEmpty()) {
                filteredList.addAll(allEquipmentList)
            } else {
                for (equipment in allEquipmentList) {
                    if (equipment.equipNameEntity.contains(
                            charSequence, true
                        ) || equipment.fluigEntity.contains(
                            charSequence,
                            true
                        ) || equipment.partNumber.contains(
                            charSequence, true
                        )
                    ) {
                        filteredList.add(equipment)
                    }
                }
            }
            val filterResults = FilterResults()
            filterResults.values = filteredList
            return filterResults
        }

        //Automatic on UI thread
        override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
            submitList(filterResults.values as MutableList<EquipmentEntity>)
        }
    }
}