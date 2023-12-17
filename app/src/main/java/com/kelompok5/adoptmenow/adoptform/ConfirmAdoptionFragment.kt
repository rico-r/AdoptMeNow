package com.kelompok5.adoptmenow.adoptform

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kelompok5.adoptmenow.R
import com.kelompok5.adoptmenow.bindImage
import com.kelompok5.adoptmenow.databinding.FragmentConfirmAdoptionBinding
import com.kelompok5.adoptmenow.network.FirebaseData
import com.kelompok5.adoptmenow.notification.NotificationItem
import com.kelompok5.adoptmenow.petinfo.ImageListAdapter
import java.util.Date

class ConfirmAdoptionFragment : Fragment() {

    lateinit var form: AdoptionForm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = ConfirmAdoptionFragmentArgs.fromBundle(requireArguments())
        form = args.form
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentConfirmAdoptionBinding.inflate(inflater, container, false)
        binding.post = form.post
        binding.content.setText(
            "${resources.getString(R.string.full_name)}: ${form.name}\n" +
            "${resources.getString(R.string.email)}: ${form.email}\n" +
            "${resources.getString(R.string.no_phone)}: ${form.phone}\n" +
            "${resources.getString(R.string.address)}: ${form.address}\n" +
            "${resources.getString(R.string.adopt_reason)}: ${form.reason}"
        )

        val imageAdapter = ImageListAdapter()
        binding.imageContainer.adapter = imageAdapter

        val post = form.post!!
        imageAdapter.submitList(post.images.drop(1))
        bindImage(binding.firstImage, post.images[0])
        if(post.images.size == 1) binding.imageContainer.visibility = View.GONE

        binding.rejectButton.setOnClickListener(::onReject)
        binding.acceptButton.setOnClickListener(::onAccept)

        return binding.root
    }

    private fun onReject(view: View) {
        FirebaseData.formRef.child(form.id).child("response").setValue("rejected")
        notifySender()
        findNavController().navigate(
            ConfirmAdoptionFragmentDirections.actionConfirmAdoptionFragmentToConfirmResultFragment("rejecting"))
    }

    private fun onAccept(view: View) {
        FirebaseData.formRef.child(form.id).child("response").setValue("accepted")
        FirebaseData.postRef.child(form.post!!.id).child("available").setValue(false)
        notifySender()
        findNavController().navigate(
            ConfirmAdoptionFragmentDirections.actionConfirmAdoptionFragmentToConfirmResultFragment("accepting"))
    }

    private fun notifySender() {
        FirebaseData.notifyRef.child(form.from).push().setValue(
            NotificationItem.AdoptionResponse(
            form.name,
            form.to,
            form.postId,
            form.id,
            Date().time
        ))
    }
}