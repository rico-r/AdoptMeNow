package com.kelompok5.adoptmenow.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.kelompok5.adoptmenow.R
import com.kelompok5.adoptmenow.adoptform.AdoptionForm
import com.kelompok5.adoptmenow.bindImage
import com.kelompok5.adoptmenow.databinding.FragmentAdoptDetailBinding
import com.kelompok5.adoptmenow.petinfo.ImageListAdapter

class AdoptDetailFragment : Fragment() {

    lateinit var binding: FragmentAdoptDetailBinding
    lateinit var form: AdoptionForm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = AdoptDetailFragmentArgs.fromBundle(requireArguments())
        form = args.data
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdoptDetailBinding.inflate(
            inflater, container, false)
        binding.lifecycleOwner = this

        val imageAdapter = ImageListAdapter()
        val layoutManager = GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        binding.imageContainer.layoutManager =  layoutManager
        binding.imageContainer.adapter = imageAdapter

        val post = form.post!!
        imageAdapter.submitList(post.images.drop(1))
        bindImage(binding.firstImage, post.images[0])
        if(post.images.size == 1) binding.imageContainer.visibility = View.GONE

        binding.title.text = post.title
        binding.availableStatus.setText(post.getStatus())
        binding.content.setText(
            "${resources.getString(R.string.full_name)}: ${form.name}\n" +
            "${resources.getString(R.string.email)}: ${form.email}\n" +
            "${resources.getString(R.string.no_phone)}: ${form.phone}\n" +
            "${resources.getString(R.string.address)}: ${form.address}\n" +
            "${resources.getString(R.string.adopt_reason)}: ${form.reason}"
        )

        binding.imageStatus.setImageResource(when(form.response) {
            "accepted" -> R.drawable.ic_accepted
            "rejected" -> R.drawable.ic_rejected
            else -> R.drawable.ic_waiting
        })

        binding.textStatus.setText(when(form.response) {
            "accepted" -> R.string.status_accpeted
            "rejected" -> R.string.ststus_rejected
            else -> R.string.status_waiting
        })

        return binding.root
    }
}