package com.kelompok5.adoptmenow.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.kelompok5.adoptmenow.MainFragmentDirections
import com.kelompok5.adoptmenow.R
import com.kelompok5.adoptmenow.databinding.TabContentHomeBinding

class HomeFragment() : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: TabContentHomeBinding = DataBindingUtil.inflate(
            inflater, R.layout.tab_content_home, container, false)

        binding.recyclerView.adapter = HomeItemAdapter(RecommendationItemClickListener {
            this.findNavController().navigate(
                MainFragmentDirections
                    .actionMainFragmentToAdoptionInfoFragment(it))
        })

        val manager = GridLayoutManager(activity, 2)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) =  when (position) {
                0 -> 2
                else -> 1
            }
        }
        binding.recyclerView.layoutManager = manager

        return binding.root
    }

}