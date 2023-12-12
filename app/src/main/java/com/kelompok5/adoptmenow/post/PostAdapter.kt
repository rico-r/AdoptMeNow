package com.kelompok5.adoptmenow.post

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kelompok5.adoptmenow.databinding.ListItemPostBinding
import com.kelompok5.adoptmenow.petinfo.PetInfo
import com.kelompok5.adoptmenow.petinfo.PetInfoDiffCallback

class PostAdapter(
    val onItemClick: (post: PetInfo) -> Unit,
    val onItemDelete: (post: PetInfo) -> Unit,
    val onItemEdit: ((post: PetInfo) -> Unit)? = null
) : ListAdapter<PetInfo, PostAdapter.ViewHolder>(PetInfoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemPostBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding, onItemClick, onItemDelete, onItemEdit)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        val binding: ListItemPostBinding,
        val onItemClick: (post: PetInfo) -> Unit,
        val onItemDelete: (post: PetInfo) -> Unit,
        val onItemEdit: ((post: PetInfo) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: PetInfo) {
            binding.data = post
            if(onItemEdit != null) {
                binding.editButton.visibility = View.VISIBLE
                binding.editButton.setOnClickListener {
                    onItemEdit.invoke(post)
                }
            } else {
                binding.editButton.visibility = View.GONE
            }
            binding.deleteButton.setOnClickListener {
                onItemDelete(post)
            }
            binding.root.setOnClickListener {
                onItemClick(post)
            }
        }
    }

}