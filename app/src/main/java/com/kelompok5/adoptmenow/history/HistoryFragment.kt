package com.kelompok5.adoptmenow.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.kelompok5.adoptmenow.R
import com.kelompok5.adoptmenow.databinding.TabContentHistoryBinding

class HistoryFragment() : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: TabContentHistoryBinding = DataBindingUtil.inflate(
            inflater, R.layout.tab_content_history, container, false)

        binding.viewPager.adapter = object: FragmentStateAdapter(this) {
            val adoptHistory = AdoptHistoryFragment()
            val postHistory = PostHistoryFragment()

            override fun getItemCount(): Int {
                return 2
            }

            override fun createFragment(position: Int): Fragment {
                return when(position) {
                    0 -> adoptHistory
                    else -> postHistory
                }
            }

        }

        TabLayoutMediator(binding.tabs, binding.viewPager) {tabs, position ->
            tabs.text = when(position) {
                0 -> resources.getString(R.string.adopt)
                else -> resources.getString(R.string.post)
            }
        }.attach()

        return binding.root
    }

}