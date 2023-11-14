package com.kelompok5.adoptmenow.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kelompok5.adoptmenow.bindImage
import com.kelompok5.adoptmenow.databinding.ListItemSearchResultBinding
import com.kelompok5.adoptmenow.petinfo.PetInfo

class SearchItemAdapter(
    private val clickListener: SearchItemClickListener
) : ListAdapter<PetInfo, RecyclerView.ViewHolder>(SearchItemDiffCallback()) {

    init {
        val items = MutableList(7) {position ->
            PetInfo(
                "",
                "Search result $position",
                true,
                "This is Search Result no.$position",
                "+62857-1234-100$position",
                "Address $position",
                List((1..9).random()) { "images/sample/cat3/cat-3-${(1..11).random()}.jpg" }
            )
        }
        submitList(items)
    }

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
        fun bind(item: PetInfo, clickListener: SearchItemClickListener, position: Int) {
            bindImage(binding.image, item.images[0])
            binding.title.text = item.title
            binding.status.text = item.getStatus(binding.root.resources)
            binding.viewButton.setOnClickListener {
                clickListener.onClick(item)
            }
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

class SearchItemClickListener(val clickListener: (item: PetInfo) -> Unit) {
    fun onClick(item: PetInfo) = clickListener(item)
}

class SearchItemDiffCallback : DiffUtil.ItemCallback<PetInfo>() {
    override fun areItemsTheSame(oldItem: PetInfo, newItem: PetInfo): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PetInfo, newItem: PetInfo): Boolean {
        return oldItem == newItem
    }

}
