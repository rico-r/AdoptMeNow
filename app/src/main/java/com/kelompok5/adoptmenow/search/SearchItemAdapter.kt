package com.kelompok5.adoptmenow.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kelompok5.adoptmenow.bindImage
import com.kelompok5.adoptmenow.databinding.ListItemSearchResultBinding
import com.kelompok5.adoptmenow.petinfo.PetInfo
import com.kelompok5.adoptmenow.petinfo.PetInfoDiffCallback

class SearchItemAdapter(
    private val clickListener: (item: PetInfo) -> Unit
) : ListAdapter<PetInfo, RecyclerView.ViewHolder>(PetInfoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SearchItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is SearchItemViewHolder -> {
                val item = getItem(position)
                holder.bind(item, clickListener, position)
            }
        }
    }

    class SearchItemViewHolder private constructor(val binding: ListItemSearchResultBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: PetInfo, clickListener: (item: PetInfo) -> Unit, position: Int) {
            bindImage(binding.image, item.images[0])
            binding.title.text = item.title
            binding.status.setText(item.getStatus())
            binding.viewButton.setOnClickListener { clickListener(item) }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): SearchItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSearchResultBinding.inflate(layoutInflater, parent, false)
                return SearchItemViewHolder(binding)
            }
        }
    }
}
