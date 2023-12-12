package com.kelompok5.adoptmenow.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kelompok5.adoptmenow.MainFragmentDirections
import com.kelompok5.adoptmenow.R
import com.kelompok5.adoptmenow.databinding.FragmentRecyclerviewBinding

class AdoptHistoryFragment : Fragment() {

    lateinit var viewModel: AdoptHistoryViewModel
    lateinit var binding: FragmentRecyclerviewBinding
    lateinit var adapter: AdoptHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[AdoptHistoryViewModel::class.java]
        adapter = AdoptHistoryAdapter {
            this.findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToAdoptDetailFragment(it))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_recyclerview, container, false)
        binding.lifecycleOwner = this
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(activity)

        viewModel.list.observe(viewLifecycleOwner, adapter::submitList)
        return binding.root
    }
}