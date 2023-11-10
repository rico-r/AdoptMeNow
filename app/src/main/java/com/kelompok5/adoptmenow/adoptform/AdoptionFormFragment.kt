package com.kelompok5.adoptmenow.adoptform

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.kelompok5.adoptmenow.R
import com.kelompok5.adoptmenow.databinding.FragmentAdoptionFormBinding

class AdoptionFormFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: FragmentAdoptionFormBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_adoption_form, container, false)
        return binding.root
    }

}