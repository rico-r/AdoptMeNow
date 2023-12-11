package com.kelompok5.adoptmenow.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kelompok5.adoptmenow.R
import com.kelompok5.adoptmenow.databinding.FragmentNotificationBinding

class NotificationFragment : Fragment() {

    lateinit var binding: FragmentNotificationBinding
    lateinit var viewModel: NotificationViewModel
    lateinit var adapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[NotificationViewModel::class.java]
        adapter = NotificationAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_notification, container, false)
        binding.lifecycleOwner = this
        binding.recyclerView.adapter = adapter

        viewModel.notification.observe(viewLifecycleOwner) {
            adapter.updateList(it)
        }
        return binding.root
    }
}