package com.kelompok5.adoptmenow.petinfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kelompok5.adoptmenow.databinding.ListItemInfoImageBinding

class ImageListAdapter : ListAdapter<String, ImageListAdapter.ImageViewHolder>(DiffCallback) {

    class ImageViewHolder(private var binding: ListItemInfoImageBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(url: String) {
            binding.imageSrc = url
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(ListItemInfoImageBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = getItem(position)
        holder.bind(image)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }

}