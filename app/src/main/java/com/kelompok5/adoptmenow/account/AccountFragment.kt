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
import com.kelompok5.adoptmenow.bindImage
import com.kelompok5.adoptmenow.databinding.TabContentAccountBinding
import com.kelompok5.adoptmenow.post.PostViewModel

class AccountFragment() : Fragment() {

    lateinit var binding: TabContentAccountBinding
    lateinit var viewModel: AccountViewModel
    lateinit var postViewModel: PostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        postViewModel = ViewModelProvider(requireActivity())[PostViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.tab_content_account, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.logoutButton.setOnClickListener(::logout)
        binding.editButton.setOnClickListener(::edit)
        binding.editPostButton.setOnClickListener(::editPost)
        binding.postCard.setOnClickListener(::openPost)
        binding.seeAll.setOnClickListener(::openMyPost)

        postViewModel.list.observe(viewLifecycleOwner) {
            if(it.isEmpty()) {
                binding.noPost.visibility = View.VISIBLE
                binding.postCard.visibility = View.GONE
            } else {
                val post = it[0]
                binding.noPost.visibility = View.GONE
                binding.postCard.visibility = View.VISIBLE
                binding.postTitle.text = post.title
                bindImage(binding.postImage, post.images[0])
                binding.postStatus.setText(post.getStatus())
            }
        }

        return binding.root
    }

    private fun openMyPost(view: View) {
        this.findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToMyPostFragment())
    }

    private fun editPost(view: View) {
        this.findNavController().navigate(
            MainFragmentDirections
                .actionMainFragmentToEditPostFragment(postViewModel.list.value!![0]))
    }

    private fun openPost(view: View) {
        this.findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToAdoptionInfoFragment(
                postViewModel.list.value!![0], null))
    }

    private fun edit(view: View) {
        this.findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToEditAccountFragment())
    }

    private fun logout(view: View) {
        Firebase.auth.signOut()
        Toast.makeText(requireContext(), resources.getString(R.string.logout_success), Toast.LENGTH_SHORT).show()
        val activity = requireActivity()
        activity.finish()
        startActivity(activity.intent)
    }

}