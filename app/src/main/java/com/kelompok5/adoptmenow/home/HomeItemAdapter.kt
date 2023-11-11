package com.kelompok5.adoptmenow.home

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kelompok5.adoptmenow.petinfo.PetInfo

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class HomeItemAdapter(
    private  val homeFragment: HomeFragment,
    private val clickListener: RecommendationItemClickListener
) : ListAdapter<DataItem, RecyclerView.ViewHolder>(HomeItemDiffCallback()) {

    private var items: MutableList<DataItem>

    init {
        items = MutableList<DataItem>(7) { position ->
            when(position) {
                0-> DataItem.Header()
                else-> DataItem.RecommendationItem(
                    PetInfo(
                        "",
                        "Recommendation $position",
                        PetInfo.Status.Available,
                        "This is recommendation no.$position",
                        "+62857-1234-000$position",
                        "Address $position",
                        List((1..9).random()) { "images/sample/cat3/cat-3-${(1..11).random()}.jpg" }
                    )
                )
            }
        }
        submitList(items)
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
