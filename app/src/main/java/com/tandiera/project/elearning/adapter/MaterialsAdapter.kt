package com.tandiera.project.elearning.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tandiera.project.elearning.databinding.ItemMaterialBinding
import com.tandiera.project.elearning.model.Material

class MaterialsAdapter : RecyclerView.Adapter<MaterialsAdapter.Viewholder>(), Filterable {

    private var listener: ((Material, Int) -> Unit)? = null
    var materials = mutableListOf<Material>()
        set(value) {
            field = value
            materialsFilter = value
            notifyDataSetChanged()
        }

    private var materialsFilter = mutableListOf<Material>()

    private val filters = object :Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var filteredList = mutableListOf<Material>()
            val filteredPattern = constraint.toString().trim().lowercase()

            if (filteredPattern.isEmpty()) {
                filteredList = materials
            } else {
                for (material in materials) {
                    val title = material.titleMaterial?.trim()?.lowercase()

                    if(title?.contains(filteredPattern) == true) {
                        filteredList.add(material)
                    }
                }
            }
            val result = FilterResults()
            result.values = filteredList
            return result
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            materialsFilter = results?.values as MutableList<Material>
            notifyDataSetChanged()
        }

    }

    class Viewholder(private val binding: ItemMaterialBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(material: Material, listener: ((Material, Int) -> Unit)?) {
            Glide.with(itemView)
                .load(material.thumbnailMaterial)
                .placeholder(android.R.color.darker_gray)
                .into(binding.ivMaterial)

            listener?.let {
                itemView.setOnClickListener {
                    it(material, adapterPosition)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val binding =
            ItemMaterialBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.bindItem(materialsFilter[position], listener)
    }

    override fun getItemCount(): Int {
        return materialsFilter.size
    }

    override fun getFilter(): Filter = filters
}
