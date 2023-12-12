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
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kelompok5.adoptmenow.R
import com.kelompok5.adoptmenow.databinding.FragmentCreatePostBinding
import com.kelompok5.adoptmenow.network.FirebaseData
import com.kelompok5.adoptmenow.petinfo.PetInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class EditPostFragment: Fragment() {

    private lateinit var getContent: ActivityResultLauncher<String>
    private lateinit var adapter: UploadImageListAdapter
    private lateinit var binding: FragmentCreatePostBinding
    private lateinit var postViewModel: PostViewModel
    private lateinit var progressDialog: ProgressDialog
    private val toDelete = mutableListOf<String>()
    private lateinit var oldPost: PetInfo
    private var changeImageIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getContent = registerForActivityResult(ActivityResultContracts.GetContent(), ::onGetContentSuccess)
        postViewModel = ViewModelProvider(requireActivity())[PostViewModel::class.java]

        val args = EditPostFragmentArgs.fromBundle(requireArguments())
        oldPost = args.data

        this.adapter = UploadImageListAdapter(
            resources, requireActivity().contentResolver, ::changeImage, oldPost.images)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_create_post, container, false)
        binding.title.setText(oldPost.title)
        binding.description.setText(oldPost.description)
        binding.phoneNumber.setText(oldPost.phone)
        binding.address.setText(oldPost.address)

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
        GlobalScope.launch(Dispatchers.IO) { editPost() }
    }

    private suspend fun editPost() {
        val imagesUri = adapter.currentList.filterNotNull()
        val newImageUri = imagesUri.filter { it.scheme != "gs" }
        val newImagePath = FirebaseData.uploadFiles(newImageUri)
        val newImageMap = newImageUri.zip(newImagePath).toMap()
        val images = imagesUri.map {
            if(it.scheme == "gs") it.path!!
            else newImageMap[it]!!
        }
        toDelete.forEach { Firebase.storage.getReference(it).delete().await() }
        val post = PetInfo(
            oldPost.id,
            oldPost.owner,
            binding.title.text.toString().lowercase(),
            oldPost.available,
            binding.description.text.toString(),
            binding.phoneNumber.text.toString(),
            binding.address.text.toString(),
            images
        )
        FirebaseData.postRef.child(oldPost.id).setValue(post).await()
        withContext(Dispatchers.Main) {
            postViewModel.list.value = postViewModel.list.value?.map {
                if(it.id == post.id) post
                else it
            }
            onEditPostSuccess()
        }
    }

    private fun onEditPostSuccess() {
        this.findNavController().navigateUp()
        Toast.makeText(requireContext(), R.string.post_edited, Toast.LENGTH_SHORT).show()
        progressDialog.dismiss()
    }

    private fun changeImage(i: Int) {
        changeImageIndex = i
        getContent.launch("image/*")
    }

    private fun onGetContentSuccess(uri: Uri?) {
        adapter.currentList[changeImageIndex]?.let {
            if(it.scheme == "gs") {
                toDelete.add(it.path!!)
            }
        }
        adapter.updateImage(changeImageIndex, uri)
    }

}