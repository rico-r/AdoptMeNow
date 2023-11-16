package com.kelompok5.adoptmenow.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kelompok5.adoptmenow.bindImage
import com.kelompok5.adoptmenow.databinding.ListItemSearchResultBinding
import com.kelompok5.adoptmenow.petinfo.PetInfo

class SearchItemAdapter(
    private val clickListener: SearchItemClickListener
) : ListAdapter<PetInfo, RecyclerView.ViewHolder>(SearchItemDiffCallback()) {

    fun changeQuery(query: String) {
        Firebase.database.getReference("posts").orderByChild("title")
            .startAt(query).endAt(query + "\uf8ff")
            .get().addOnSuccessListener {
                val items = it.getValue<HashMap<String, PetInfo>>()
                if(items!=null) submitList(items.values.toList())
                else submitList(listOf())
            }.addOnFailureListener {
                // TODO: Do something when failed to get the list
                it.printStackTrace()
            }
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
