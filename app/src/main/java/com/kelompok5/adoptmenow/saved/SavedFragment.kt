package com.kelompok5.adoptmenow.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.kelompok5.adoptmenow.R
import com.kelompok5.adoptmenow.databinding.TabContentSavedBinding

class SavedFragment() : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: TabContentSavedBinding = DataBindingUtil.inflate(
            inflater, R.layout.tab_content_saved, container, false)
        return binding.root
    }

}