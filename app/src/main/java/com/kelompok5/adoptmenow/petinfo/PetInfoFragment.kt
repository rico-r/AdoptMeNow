package com.kelompok5.adoptmenow.petinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.kelompok5.adoptmenow.R
import com.kelompok5.adoptmenow.databinding.FragmentPetInfoBinding

class PetInfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: FragmentPetInfoBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pet_info, container, false)
        val arguments = PetInfoFragmentArgs.fromBundle(requireArguments())
        binding.info = arguments.petInfo
        binding.adoptButton.setOnClickListener {
            findNavController().navigate(
                PetInfoFragmentDirections
                    .actionAdoptionInfoFragmentToAdoptionFormFragment())
        }

        val images = arguments.petInfo.images
        binding.firstImage.setImageResource(android.R.drawable.ic_menu_camera)

        if(images.size == 1) {
            binding.imageContainer.visibility = View.GONE
        } else {
            val layoutManager = GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
            binding.imageContainer.layoutManager =  layoutManager
            binding.imageContainer.adapter = ImageListAdapter(images.drop(1))
        }
        return binding.root
    }

}