package com.kelompok5.adoptmenow.account

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
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kelompok5.adoptmenow.R
import com.kelompok5.adoptmenow.databinding.FragmentEditAccountBinding
import kotlin.random.Random

class EditAccountFragment : Fragment() {

    lateinit var binding: FragmentEditAccountBinding
    lateinit var viewModel: AccountViewModel
    lateinit var getContent: ActivityResultLauncher<String>
    var newImageUri: Uri? = null
    var newImagePath: String? = null

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
        binding.saveButton.setOnClickListener(::saveImage)

        return binding.root
    }

    private fun changePhoto(view: View) {
        getContent.launch("image/*")
    }

    private fun getImageUrl(): String {
        return if(newImageUri == null) {
            viewModel.user.value!!.photo
        } else if(newImagePath == null) {
            newImagePath = "/users/${Firebase.auth.currentUser!!.uid}/profile-${Random.nextInt()}.jpg"
            newImagePath!!
        } else {
            newImagePath!!
        }
    }

    private fun saveImage(view: View) {
        binding.loading.visibility = View.VISIBLE
        // Directly save the data if profile image is not changed
        if(newImageUri == null) {
            saveData()
            return
        }
        // TODO: Scale down the image to save storage
        // TODO: Do something when upload failed
        val oldPhoto = viewModel.user.value!!.photo
        if(oldPhoto.isNotEmpty()) {
            // Delete old photo
            Firebase.storage.getReference(oldPhoto).delete()
        }
        Firebase.storage.getReference(getImageUrl())
            .putStream(requireActivity().contentResolver.openInputStream(newImageUri!!)!!)
            .addOnSuccessListener { saveData() }
            .addOnFailureListener { onFailed(R.string.upload_image_failed) }
    }

    private fun saveData() {
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()
        val newData = UserInfo(
            binding.name.text.toString(),
            getImageUrl(),
            email,
            binding.address.text.toString(),
            binding.phone.text.toString(),
        )
        val currentUser = Firebase.auth.currentUser!!
        // Update email if changed
        if(currentUser.email != email) {
            currentUser.updateEmail(email)
        }
        // Update password if changed
        if(password.isNotEmpty()) {
            currentUser.updatePassword(password)
        }
        Firebase.database.getReference("users")
            .child(currentUser.uid)
            .setValue(newData)
            .addOnSuccessListener { onSaveSuccess() }
            .addOnFailureListener { onFailed(R.string.save_failed) }
    }

    private fun onFailed(msgResId: Int) {
        binding.loading.visibility = View.GONE
        Toast.makeText(requireContext(), msgResId, Toast.LENGTH_SHORT).show()
    }

    private fun onSaveSuccess() {
        this.findNavController().navigateUp()
        binding.loading.visibility = View.GONE
        Toast.makeText(requireContext(), R.string.profile_saved, Toast.LENGTH_SHORT).show()
    }

}