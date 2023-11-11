package com.kelompok5.adoptmenow.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kelompok5.adoptmenow.R
import com.kelompok5.adoptmenow.databinding.HomeContentRecommendationItemBinding
import com.kelompok5.adoptmenow.petinfo.PetInfo

class RecommendationItemViewHolder private constructor(val binding: HomeContentRecommendationItemBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(item: PetInfo, clickListener: RecommendationItemClickListener, position: Int) {
        binding.item = item
        binding.clickListener = clickListener
        val margin = binding.root.layoutParams as ViewGroup.MarginLayoutParams
        val res = binding.root.resources
        val marginInner = res.getDimensionPixelSize(R.dimen.card_margin)
        val marginOutter = res.getDimensionPixelSize(R.dimen.home_item_margin)
        margin.marginStart = if(position % 2 == 0) marginInner else marginOutter
        margin.marginEnd = if(position % 2 == 0) marginOutter else marginInner
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): RecommendationItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HomeContentRecommendationItemBinding.inflate(layoutInflater, parent, false)
            return RecommendationItemViewHolder(binding)
        }
    }
}