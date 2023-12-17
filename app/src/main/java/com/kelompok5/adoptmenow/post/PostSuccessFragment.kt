package com.kelompok5.adoptmenow.post

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kelompok5.adoptmenow.databinding.FragmentPostSuccessBinding

class PostSuccessFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostSuccessBinding.inflate(
            inflater, container, false)
        (binding.anim.drawable as AnimationDrawable).start()
        binding.button.setOnClickListener {
            this.findNavController().navigateUp()
        }
        return binding.root
    }
}