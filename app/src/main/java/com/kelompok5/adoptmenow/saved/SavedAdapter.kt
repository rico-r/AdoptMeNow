package com.kelompok5.adoptmenow.saved

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kelompok5.adoptmenow.databinding.ListItemPostBinding
import com.kelompok5.adoptmenow.petinfo.PetInfo

class SavedAdapter(
    val onItemClick: (post: PetInfo) -> Unit,
    val onItemDelete: (post: PetInfo) -> Unit
) : ListAdapter<PetInfo, SavedAdapter.ViewHolder>(SavedItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemPostBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding, onItemClick, onItemDelete)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        val binding: ListItemPostBinding,
        val onItemClick: (post: PetInfo) -> Unit,
        val onItemDelete: (post: PetInfo) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: PetInfo) {
            binding.data = post
            binding.editButton.visibility = View.GONE
            binding.deleteButton.setOnClickListener {
                onItemDelete(post)
            }
            binding.root.setOnClickListener {
                onItemClick(post)
            }
        }
    }

    class SavedItemDiffCallback : DiffUtil.ItemCallback<PetInfo>() {
        override fun areItemsTheSame(oldItem: PetInfo, newItem: PetInfo): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PetInfo, newItem: PetInfo): Boolean {
            return oldItem == newItem
        }

    }
}