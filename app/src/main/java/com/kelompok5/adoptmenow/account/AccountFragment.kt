package com.kelompok5.adoptmenow.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kelompok5.adoptmenow.MainFragmentDirections
import com.kelompok5.adoptmenow.R
import com.kelompok5.adoptmenow.bindImage
import com.kelompok5.adoptmenow.databinding.TabContentAccountBinding

class AccountFragment() : Fragment() {

    lateinit var binding: TabContentAccountBinding
    lateinit var user: UserInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Firebase.database.getReference("users")
            .child(Firebase.auth.currentUser!!.uid).get()
            .addOnSuccessListener {
                this.user = it.getValue<UserInfo>()!!
                updateUI()
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.tab_content_account, container, false)

        binding.logoutButton.setOnClickListener {
            Firebase.auth.signOut()
            this.findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToLoginFragment())
            Toast.makeText(requireContext(), resources.getString(R.string.logout_success), Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun updateUI() {
        binding.name.text = user.name
        binding.email.text = ": ${user.email}"
        binding.address.text = ": ${user.address}"
        binding.phone.text = ": ${user.phone}"
        if(user.photo != "")
            bindImage(binding.photo, user.photo)
        else
            binding.photo.setImageResource(R.drawable.pets)
    }
}