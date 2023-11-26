package com.kelompok5.adoptmenow.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kelompok5.adoptmenow.MainFragmentDirections
import com.kelompok5.adoptmenow.R
import com.kelompok5.adoptmenow.databinding.TabContentAccountBinding

class AccountFragment() : Fragment() {

    lateinit var binding: TabContentAccountBinding
    lateinit var viewModel: AccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.tab_content_account, container, false)

        viewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.logoutButton.setOnClickListener(::logout)
        binding.editButton.setOnClickListener(::edit)

        return binding.root
    }

    private fun edit(view: View) {
        this.findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToEditAccountFragment())
    }

    private fun logout(view: View) {
        Firebase.auth.signOut()
        this.findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToLoginFragment())
        Toast.makeText(requireContext(), resources.getString(R.string.logout_success), Toast.LENGTH_SHORT).show()
    }

}