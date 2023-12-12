package com.kelompok5.adoptmenow.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kelompok5.adoptmenow.MainFragmentDirections
import com.kelompok5.adoptmenow.R
import com.kelompok5.adoptmenow.databinding.TabContentSavedBinding
import com.kelompok5.adoptmenow.petinfo.PetInfo
import com.kelompok5.adoptmenow.post.PostAdapter

class SavedFragment() : Fragment() {

    lateinit var viewModel: SavedViewModel
    lateinit var adapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[SavedViewModel::class.java]
        adapter = PostAdapter(::onItemClick, viewModel::removeSaved)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: TabContentSavedBinding = DataBindingUtil.inflate(
            inflater, R.layout.tab_content_saved, container, false)
        binding.lifecycleOwner = this
        binding.recyclerView.adapter = adapter
        viewModel.saved.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        return binding.root
    }

    private fun onItemClick(post: PetInfo) {
        this.findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToAdoptionInfoFragment(post, null))
    }

}