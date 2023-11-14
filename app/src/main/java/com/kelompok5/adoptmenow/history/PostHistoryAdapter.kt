package com.kelompok5.adoptmenow.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kelompok5.adoptmenow.databinding.ListItemPostHistoryBinding
import java.util.Date

class PostHistoryAdapter(
    val clickListener: (item: DataItem) -> Unit
) : ListAdapter<PostHistoryAdapter.DataItem, RecyclerView.ViewHolder>(DataItemDiffCallback()) {

    init {
        val items = List(4) {position ->
            DataItem(
                "Title $position",
                "Description $position",
                Date()
            )
        }
        submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(getItem(position), clickListener, position)
    }

    class ViewHolder(val binding: ListItemPostHistoryBinding) :  RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataItem, clickListener: (item: DataItem) -> Unit, position: Int) {
            binding.title.text = item.title
            binding.description.text = item.description
            binding.date.text = String.format("%02d-%02d-%04d",
                item.date.day,
                item.date.month,
                item.date.year)
            binding.formButton.setOnClickListener {
                clickListener(item)
            }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemPostHistoryBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    class DataItemDiffCallback : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }
    }

    data class DataItem(
        var title: String,
        var description: String,
        var date: Date
    )

}
