package com.kelompok5.adoptmenow.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.kelompok5.adoptmenow.R
import com.kelompok5.adoptmenow.databinding.CarouselItemBinding

class CarouselItem: Fragment() {
    private var _imageUrl = ""
    var imageUrl: String
        get() = _imageUrl
        set(value) {
            _imageUrl = value
        }

    var header: HeaderViewHolder? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: CarouselItemBinding = DataBindingUtil.inflate(
            inflater, R.layout.carousel_item, container, false)
        binding.imageUrl = imageUrl
        binding.prev.setOnClickListener {
            val carousel = header!!.binding.carousel
            val currentItem = carousel.currentItem
            if(currentItem > 0) carousel.currentItem = currentItem - 1
        }
        binding.next.setOnClickListener {
            val carousel = header!!.binding.carousel
            val currentItem = carousel.currentItem
            if(currentItem + 1 < carousel.adapter!!.itemCount) carousel.currentItem = currentItem + 1
        }
        return binding.root
    }

}