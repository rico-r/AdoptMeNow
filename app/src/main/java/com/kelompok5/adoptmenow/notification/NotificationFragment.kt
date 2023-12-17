package com.kelompok5.adoptmenow.notification

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kelompok5.adoptmenow.R
import com.kelompok5.adoptmenow.databinding.FragmentNotificationBinding
import com.kelompok5.adoptmenow.network.FirebaseData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationFragment : Fragment() {

    lateinit var binding: FragmentNotificationBinding
    lateinit var viewModel: NotificationViewModel
    lateinit var adapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[NotificationViewModel::class.java]
        adapter = NotificationAdapter(::onRequestClick, ::onResponseClick)
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

    private fun onRequestClick(item: NotificationItem.AdoptionRequest) {
        val progressDialog = ProgressDialog.show(requireContext(),
            "", resources.getString(R.string.loading), true)
        GlobalScope.launch(Dispatchers.IO) {
            val form = FirebaseData.getForm(item.formId)
            form.post = FirebaseData.getPostData(form.postId)
            withContext(Dispatchers.Main) {
                if(form.response.isEmpty()) {
                    findNavController().navigate(
                        NotificationFragmentDirections.actionNotificationFragmentToConfirmAdoptionFragment(form))
                } else {
                    findNavController().navigate(
                        NotificationFragmentDirections.actionNotificationFragmentToConfirmResultFragment(
                            if(form.response == "accepted") "accepting" else "rejecting"))
                }
                progressDialog.dismiss()
            }
        }
        viewModel.markRead(item)
    }

    private fun onResponseClick(item: NotificationItem.AdoptionResponse) {
        val progressDialog = ProgressDialog.show(requireContext(),
            "", resources.getString(R.string.loading), true)
        GlobalScope.launch(Dispatchers.IO) {
            val form = FirebaseData.getForm(item.formId)
            withContext(Dispatchers.Main) {
                findNavController().navigate(
                    NotificationFragmentDirections.actionNotificationFragmentToConfirmResultFragment(form.response))
                progressDialog.dismiss()
            }
        }
        viewModel.markRead(item)
    }
}