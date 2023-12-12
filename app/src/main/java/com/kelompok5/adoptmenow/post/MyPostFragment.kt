package com.kelompok5.adoptmenow.post

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kelompok5.adoptmenow.R
import com.kelompok5.adoptmenow.databinding.FragmentMyPostBinding
import com.kelompok5.adoptmenow.petinfo.PetInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyPostFragment : Fragment() {

    private lateinit var binding: FragmentMyPostBinding
    private lateinit var viewModel: PostViewModel
    private lateinit var adapter: PostAdapter
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[PostViewModel::class.java]
        adapter = PostAdapter(::onItemClick, ::onItemDelete, ::onItemEdit)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyPostBinding.inflate(
            inflater, container, false)
        binding.lifecycleOwner = this
        binding.recyclerView.adapter = adapter

        viewModel.list.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        return binding.root
    }

    private fun onItemClick(post: PetInfo) {
        this.findNavController().navigate(
            MyPostFragmentDirections.actionMyPostFragmentToAdoptionInfoFragment(post, null))
    }

    private fun onItemDelete(post: PetInfo) {
        AlertDialog.Builder(requireContext())
            .setMessage(resources.getString(R.string.confirm_delete_post, post.title))
            .setPositiveButton(R.string.yes) {_, _ -> onConfirmDelete(post)}
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun onConfirmDelete(post: PetInfo) {
        progressDialog = ProgressDialog.show(requireContext(), "",
            resources.getString(R.string.deleting_post, post.title), true)
        GlobalScope.launch(Dispatchers.IO) {
            viewModel.deletePost(post, ::onDeleteSuccess)
        }
    }

    private fun onDeleteSuccess() {
        progressDialog.dismiss()
        Toast.makeText(requireContext(), R.string.delete_post_success, Toast.LENGTH_SHORT).show()
    }

    private fun onItemEdit(post: PetInfo) {
        this.findNavController().navigate(
            MyPostFragmentDirections
                .actionMyPostFragmentToEditPostFragment(post))
    }
}