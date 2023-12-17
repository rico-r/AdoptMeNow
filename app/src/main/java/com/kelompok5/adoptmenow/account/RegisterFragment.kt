package com.kelompok5.adoptmenow.account

import android.app.ProgressDialog
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
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kelompok5.adoptmenow.R
import com.kelompok5.adoptmenow.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    lateinit var binding: FragmentRegisterBinding
    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_register, container, false)

        binding.registerButton.setOnClickListener {
            register()
        }

        auth = Firebase.auth
        return binding.root
    }

    private fun register() {
        val name = binding.fullNameField.text.toString()
        val email = binding.emailField.text.toString()
        val password = binding.passwordField.text.toString()
        val repeatPassword = binding.repeatPasswordField.text.toString()
        if(password != repeatPassword) {
            Toast.makeText(requireContext(), resources.getString(R.string.password_not_match), Toast.LENGTH_SHORT).show()
            binding.repeatPasswordField.requestFocus()
            return
        }
        val progressDialog = ProgressDialog.show(requireContext(), "", resources.getString(R.string.loading), true)
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser!!
                    Firebase.database.getReference("users")
                        .child(user.uid).setValue(UserInfo(name, "", email, "", ""))
                        .addOnSuccessListener {
                            user.sendEmailVerification()
                            val navController = this.findNavController()
                            navController.popBackStack()
                            navController.navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())
                            Toast.makeText(requireContext(), resources.getString(R.string.register_success), Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(requireContext(), resources.getString(R.string.email_registered), Toast.LENGTH_SHORT).show()
                    binding.emailField.requestFocus()
                }
                progressDialog.dismiss()
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