package com.kelompok5.adoptmenow.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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
        return binding.root
    }

}