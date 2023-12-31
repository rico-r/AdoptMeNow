package com.kelompok5.adoptmenow.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.kelompok5.adoptmenow.MainFragmentDirections
import com.kelompok5.adoptmenow.R
import com.kelompok5.adoptmenow.databinding.TabContentHomeBinding

class HomeFragment() : Fragment() {

    lateinit var binding: TabContentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.tab_content_home, container, false)

        binding.recyclerView.adapter = HomeItemAdapter(this, RecommendationItemClickListener {
            this.findNavController().navigate(
                MainFragmentDirections
                    .actionMainFragmentToAdoptionInfoFragment(it, null))
        })

        val manager = GridLayoutManager(activity, 2)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) =  when (position) {
                0 -> 2
                else -> 1
            }
        }
        binding.recyclerView.layoutManager = manager

        binding.searchButton.setOnClickListener {
            searchPost()
        }
        binding.searchQuery.setOnEditorActionListener { view, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                searchPost()
            }
            true
        }

        binding.createPostButton.setOnClickListener {
            this.findNavController()
                .navigate(MainFragmentDirections.actionMainFragmentToCreatePostFragment())
        }

        binding.notificationButton.setOnClickListener {
            this.findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToNotificationFragment())
        }

        return binding.root
    }

    fun searchPost() {
        val query = binding.searchQuery.text.toString()
        if(query.isEmpty()) return
        this.findNavController().navigate(
            MainFragmentDirections
                .actionMainFragmentToSearchResultFragment(query))
    }

}
