package com.kelompok5.adoptmenow.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kelompok5.adoptmenow.adoptform.AdoptionForm
import com.kelompok5.adoptmenow.adoptform.AdoptionFormDiffCallback
import com.kelompok5.adoptmenow.databinding.ListItemAdoptHistoryBinding
import java.util.Date

class AdoptHistoryAdapter(
    val clickListener: (item: AdoptionForm) -> Unit
) : ListAdapter<AdoptionForm, RecyclerView.ViewHolder>(AdoptionFormDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemAdoptHistoryBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(getItem(position), clickListener, position)
    }

    class ViewHolder(val binding: ListItemAdoptHistoryBinding) :  RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AdoptionForm, clickListener: (item: AdoptionForm) -> Unit, position: Int) {
            val date = Date(item.date)
            binding.title.text = item.post!!.title
            binding.description.text = item.post!!.description
            binding.date.text = String.format("%02d-%02d-%04d",
                date.date, date.month, date.year + 1900)
            binding.root.setOnClickListener {
                clickListener(item)
            }
            binding.executePendingBindings()
        }
    }

}
