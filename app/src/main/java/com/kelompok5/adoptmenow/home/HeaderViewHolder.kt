package com.kelompok5.adoptmenow.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kelompok5.adoptmenow.R
import com.kelompok5.adoptmenow.databinding.HomeContentHeaderBinding

class HeaderViewHolder(
    val binding: HomeContentHeaderBinding,
    val homeFragment: HomeFragment
): RecyclerView.ViewHolder(binding.root) {

    lateinit var images: List<String>

    fun bind() {
        Firebase.storage.reference.child("/carousel")
            .listAll().addOnSuccessListener {
                images = List(it.items.size) {position ->
                    it.items[position].path
                }
                bindAdapter()
            }.addOnFailureListener {
                // TODO: Do something when fail to get the list
                it.printStackTrace()
            }
    }

    private fun bindAdapter() {
        binding.carousel.adapter = object: FragmentStateAdapter(homeFragment) {
            override fun getItemCount(): Int {
                return images.size
            }

            override fun createFragment(position: Int): Fragment {
                val result = CarouselItem()
                result.imageUrl = images[position]
                result.header = this@HeaderViewHolder
                return result
            }
        }
    }

    companion object {
        fun from(parent: ViewGroup, homeFragment: HomeFragment): HeaderViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = DataBindingUtil.inflate<HomeContentHeaderBinding>(
                inflater, R.layout.home_content_header, parent, false)
            return HeaderViewHolder(binding, homeFragment)
        }
    }
}
