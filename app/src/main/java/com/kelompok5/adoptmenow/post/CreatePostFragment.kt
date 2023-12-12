package com.kelompok5.adoptmenow.post

import android.app.ProgressDialog
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
import androidx.recyclerview.widget.GridLayoutManager
import com.kelompok5.adoptmenow.R
import com.kelompok5.adoptmenow.databinding.FragmentCreatePostBinding
import com.kelompok5.adoptmenow.network.FirebaseData
import com.kelompok5.adoptmenow.petinfo.PetInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CreatePostFragment : Fragment() {

    private lateinit var getContent: ActivityResultLauncher<String>
    private lateinit var adapter: UploadImageListAdapter
    private lateinit var binding: FragmentCreatePostBinding
    private lateinit var postViewModel: PostViewModel
    private lateinit var progressDialog: ProgressDialog
    private var changeImageIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getContent = registerForActivityResult(ActivityResultContracts.GetContent(), ::onGetContentSuccess)
        postViewModel = ViewModelProvider(requireActivity())[PostViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_create_post, container, false)
        this.adapter = UploadImageListAdapter(
            resources, requireActivity().contentResolver, ::changeImage)
        FirebaseData.getCurrentUser {
            binding.phoneNumber.setText(it.phone)
            binding.address.setText(it.address)
        }

        binding.imageList.layoutManager = GridLayoutManager(activity, 5)
        binding.imageList.adapter = adapter
        binding.submitButton.setOnClickListener(::onSubmit)
        return binding.root
    }

    private fun onSubmit(view: View) {
        var ok = true
        val images = adapter.currentList.filterNotNull()
        if(images.isEmpty()) {
            Toast.makeText(requireContext(), R.string.require_image, Toast.LENGTH_SHORT).show()
            ok = false
        }
        if(!ok) return
        progressDialog = ProgressDialog.show(requireContext(), "", resources.getString(R.string.creating_post), true)
        GlobalScope.launch(Dispatchers.IO) { createPost() }
    }

    private fun onCreatePostSuccess() {
        this.findNavController().navigateUp()
        Toast.makeText(requireContext(), R.string.post_created, Toast.LENGTH_SHORT).show()
        progressDialog.dismiss()
    }

    private fun onGetContentSuccess(uri: Uri?) {
        adapter.updateImage(changeImageIndex, uri)
    }

    private fun changeImage(i: Int) {
        changeImageIndex = i
        getContent.launch("image/*")
    }

    private suspend fun createPost() {
        val images = FirebaseData.uploadFiles(adapter.currentList.filterNotNull())
        val post = PetInfo(
            "",
            FirebaseData.uid,
            binding.title.text.toString().lowercase(),
            true,
            binding.description.text.toString(),
            binding.phoneNumber.text.toString(),
            binding.address.text.toString(),
            images
        )
        postViewModel.createPost(post) {
            onCreatePostSuccess()
        }
    }

}