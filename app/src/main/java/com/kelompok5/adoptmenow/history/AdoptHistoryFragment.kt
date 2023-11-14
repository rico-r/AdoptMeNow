package com.kelompok5.adoptmenow.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kelompok5.adoptmenow.R
import com.kelompok5.adoptmenow.databinding.FragmentRecyclerviewBinding

class AdoptHistoryFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentRecyclerviewBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_recyclerview, container, false)
        binding.recyclerview.adapter = AdoptHistoryAdapter {
        }
        binding.recyclerview.layoutManager = LinearLayoutManager(activity)
        return binding.root
    }
}