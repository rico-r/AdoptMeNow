package com.kelompok5.adoptmenow.home

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kelompok5.adoptmenow.petinfo.PetInfo

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class HomeItemAdapter(
    private  val homeFragment: HomeFragment,
    private val clickListener: RecommendationItemClickListener
) : ListAdapter<DataItem, RecyclerView.ViewHolder>(HomeItemDiffCallback()) {

    private val header = DataItem.Header()

    init {
        Firebase.database.getReference("posts")
            .orderByKey().limitToLast(6).get()
            .addOnSuccessListener {
                val items = it.getValue<List<PetInfo>>()
                updateList(items!!)
            }.addOnSuccessListener {
                // TODO: Do something when failed to get the list
            }
    }

    private fun updateList(items: List<PetInfo>) {
        submitList(List(items.size + 1) {
            when (it) {
                0 -> header
                else -> DataItem.RecommendationItem(items[it - 1])
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderViewHolder.from(parent, homeFragment)
            ITEM_VIEW_TYPE_ITEM -> RecommendationItemViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RecommendationItemViewHolder -> {
                val item = getItem(position) as DataItem.RecommendationItem
                holder.bind(item.petInfo, clickListener, position)
            }
            is HeaderViewHolder -> {
                holder.bind()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(position) {
            0 -> ITEM_VIEW_TYPE_HEADER
            else -> ITEM_VIEW_TYPE_ITEM
        }
    }
}

class RecommendationItemClickListener(val clickListener: (recommended: PetInfo) -> Unit) {
    fun onClick(recommended: PetInfo) = clickListener(recommended)
}

class HomeItemDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

}

sealed class DataItem {
    data class RecommendationItem(val petInfo: PetInfo): DataItem() {
        override val id = petInfo.dataUrl
    }

    class Header(): DataItem() {
        override val id = "(Header)"
    }

    abstract val id: String
}
