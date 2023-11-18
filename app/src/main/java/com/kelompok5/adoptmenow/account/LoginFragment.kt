package com.kelompok5.adoptmenow.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kelompok5.adoptmenow.R
import com.kelompok5.adoptmenow.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding
    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login, container, false)

        binding.register.setOnClickListener {
            this.findNavController().navigate(
                LoginFragmentDirections
                    .actionLoginFragmentToRegisterFragment())
        }

        binding.fogotPassword.setOnClickListener {
        }

        binding.loginButton.setOnClickListener {
            attemptLogin()
        }

        auth = Firebase.auth
        return binding.root
    }

    private fun attemptLogin() {
        val email = binding.emailField.text.toString()
        val password = binding.passwordField.text.toString()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    this.findNavController().navigate(
                        LoginFragmentDirections.actionLoginFragmentToMainFragment())
                } else {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.auth_failed),
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (requireActivity() as AppCompatActivity).supportActionBar!!.show()
    }
}