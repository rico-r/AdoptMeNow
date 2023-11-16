package com.kelompok5.adoptmenow.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kelompok5.adoptmenow.R
import com.kelompok5.adoptmenow.databinding.FragmentSearchResultBinding

class SearchResultFragment : Fragment() {

    lateinit var binding: FragmentSearchResultBinding
    lateinit var adapter: SearchItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_search_result, container, false)
        val arguments = SearchResultFragmentArgs.fromBundle(requireArguments())

        binding.searchQuery.setText(arguments.query)

        adapter = SearchItemAdapter(SearchItemClickListener {
            this.findNavController().navigate(
                SearchResultFragmentDirections
                    .actionSearchResultFragmentToAdoptionInfoFragment(it))
        })
        binding.recyclerView.adapter = adapter

        binding.recyclerView.layoutManager = LinearLayoutManager(activity)

        binding.navigateUp.setOnClickListener {
            this.findNavController()
                .navigateUp()
        }

        binding.searchButton.setOnClickListener {
            searchPost()
        }
        binding.searchQuery.setOnEditorActionListener { view, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                searchPost()
            }
            true
        }

        searchPost()
        return binding.root
    }

    fun searchPost() {
        adapter.changeQuery(binding.searchQuery.text.toString())
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