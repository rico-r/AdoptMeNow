package com.kelompok5.adoptmenow.account

import android.app.ProgressDialog
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kelompok5.adoptmenow.R
import com.kelompok5.adoptmenow.databinding.FragmentEditAccountBinding
import com.kelompok5.adoptmenow.network.FirebaseData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class EditAccountFragment : Fragment() {

    private lateinit var binding: FragmentEditAccountBinding
    private lateinit var viewModel: AccountViewModel
    private lateinit var getContent: ActivityResultLauncher<String>
    private var newImageUri: Uri? = null
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
            newImageUri = it
            val img = requireActivity().contentResolver.openInputStream(it!!)
            binding.photo.setImageDrawable(BitmapDrawable(resources, img))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_account, container, false)

        viewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.changePhotoButton.setOnClickListener(::changePhoto)
        binding.saveButton.setOnClickListener(::save)

        return binding.root
    }

    private fun changePhoto(view: View) {
        getContent.launch("image/*")
    }

    private fun save(view: View) {
        progressDialog = ProgressDialog.show(requireContext(), "", resources.getString(R.string.saving_profile), true)
        GlobalScope.launch(Dispatchers.IO) { save() }
    }

    private fun onSaved() {
        progressDialog.dismiss()
        Toast.makeText(requireContext(), R.string.profile_saved, Toast.LENGTH_SHORT).show()
        this.findNavController().navigateUp()
    }

    private suspend fun save() {
        val currentUser = Firebase.auth.currentUser!!
        val user = viewModel.user.value!!
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()
        val newData = UserInfo(
            binding.name.text.toString(),
            user.photo,
            email,
            binding.address.text.toString(),
            binding.phone.text.toString(),
        )
        // Update photo if changed
        newImageUri?.let {
            newData.photo = FirebaseData.uploadFiles(listOf(it))[0]
            try {
                // Delete old photo
                Firebase.storage.getReference(user.photo).delete().await()
            } catch (_: Exception) {}
        }
        // Update email if changed
        if(currentUser.email != email) {
            try {
                currentUser.updateEmail(email).await()
            } catch (_: Exception) {}
        }
        // Update password if changed
        if(password.isNotEmpty()) {
            try {
                currentUser.updatePassword(password).await()
            } catch (_: Exception) {}
        }
        FirebaseData.userRef.setValue(newData).await()
        withContext(Dispatchers.Main) { onSaved() }
    }

}