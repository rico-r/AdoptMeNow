package com.kelompok5.adoptmenow.adoptform

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kelompok5.adoptmenow.R
import com.kelompok5.adoptmenow.databinding.FragmentAdoptionFormBinding
import com.kelompok5.adoptmenow.history.AdoptHistoryViewModel
import com.kelompok5.adoptmenow.network.FirebaseData
import com.kelompok5.adoptmenow.petinfo.PetInfo
import java.util.Date

class AdoptionFormFragment : Fragment() {

    lateinit var binding: FragmentAdoptionFormBinding
    lateinit var adoptHistoryViewModel: AdoptHistoryViewModel
    lateinit var post: PetInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = AdoptionFormFragmentArgs.fromBundle(requireArguments())
        post = args.post

        adoptHistoryViewModel = ViewModelProvider(requireActivity())[AdoptHistoryViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_adoption_form, container, false)
        FirebaseData.getCurrentUser {
            binding.fullName.setText(it.name)
            binding.email.setText(it.email)
            binding.phoneNumber.setText(it.phone)
            binding.address.setText(it.address)
        }
        binding.sendButton.setOnClickListener {
            onSubmit()
        }
        return binding.root
    }

    private fun onSubmit() {
        val form = AdoptionForm()
        form.date = Date().time
        form.from = FirebaseData.uid
        form.to = post.owner
        form.postId = post.id
        form.name = binding.fullName.text.toString()
        form.email = binding.email.text.toString()
        form.phone = binding.phoneNumber.text.toString()
        form.profession = binding.profession.text.toString()
        form.address = binding.address.text.toString()
        form.reason = binding.adoptReason.text.toString()
        form.post = post
        adoptHistoryViewModel.add(form)
        this.findNavController().navigateUp()
        Toast.makeText(requireContext(), R.string.form_submited, Toast.LENGTH_SHORT).show()
    }

}