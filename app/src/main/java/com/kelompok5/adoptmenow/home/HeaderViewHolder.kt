package com.kelompok5.adoptmenow.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kelompok5.adoptmenow.R
import com.kelompok5.adoptmenow.databinding.HomeContentHeaderBinding
import java.util.Timer
import java.util.TimerTask

private const val CAROUSEL_ROLL_DURATION = 10000L

class HeaderViewHolder(
    val binding: HomeContentHeaderBinding,
    val homeFragment: HomeFragment
): RecyclerView.ViewHolder(binding.root) {

    var images = listOf<String>()

    fun bind() {
        Firebase.database.getReference("carousel").get()
            .addOnSuccessListener {data ->
                images = data.getValue<List<String>>()!!
                bindAdapter()
            }.addOnFailureListener {
                Log.i("FB_DB", "Failed to get list of carousel")
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
        Timer().schedule(object: TimerTask() {
            override fun run() {
                val carousel = binding.carousel
                carousel.currentItem = (carousel.currentItem + 1) % carousel.adapter!!.itemCount
            }
        }, CAROUSEL_ROLL_DURATION, CAROUSEL_ROLL_DURATION)
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
