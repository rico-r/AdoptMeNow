package com.kelompok5.adoptmenow.petinfo

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kelompok5.adoptmenow.R
import com.kelompok5.adoptmenow.databinding.FragmentPetInfoBinding
import com.kelompok5.adoptmenow.history.AdoptHistoryViewModel
import com.kelompok5.adoptmenow.saved.SavedViewModel

class PetInfoFragment : Fragment() {

    lateinit var binding: FragmentPetInfoBinding
    lateinit var viewModel: PetInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityViewModelProvider = ViewModelProvider(requireActivity())
        val savedViewModel = activityViewModelProvider[SavedViewModel::class.java]
        val adoptHistoryViewModel = activityViewModelProvider[AdoptHistoryViewModel::class.java]
        val viewModelFactory = PetInfoViewModelFactory(savedViewModel, adoptHistoryViewModel)
        viewModel =
            ViewModelProvider(this, viewModelFactory)[PetInfoViewModel::class.java]

        val arguments = PetInfoFragmentArgs.fromBundle(requireArguments())
        arguments.petInfo?.let { viewModel.pet.value = it }

        arguments.postId?.let { postId ->
            Firebase.database.getReference("posts").child(postId)
                .get().addOnSuccessListener {
                    viewModel.pet.value = it.getValue<PetInfo>()
                }
            //TODO: Do something when failed to get info
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pet_info, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.adoptButton.setOnClickListener {
            findNavController().navigate(
                PetInfoFragmentDirections
                    .actionAdoptionInfoFragmentToAdoptionFormFragment(viewModel.pet.value!!))
        }

        binding.saveButton.setOnClickListener {
            viewModel.onSave()
            Toast.makeText(requireContext(), resources.getString(R.string.post_saved), Toast.LENGTH_SHORT).show()
        }

        val imageAdapter = ImageListAdapter()
        val layoutManager = GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        binding.imageContainer.layoutManager =  layoutManager
        binding.imageContainer.adapter = imageAdapter

        viewModel.images.observe(viewLifecycleOwner) { images ->
            imageAdapter.submitList(images)
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun share() {
        val pet = viewModel.pet.value ?: return
        val imgDrawable = binding.firstImage.drawable as BitmapDrawable

        val imgBitmapPath: String =
            MediaStore.Images.Media.insertImage(requireActivity().contentResolver, imgDrawable.bitmap, "result", null)
        val imgBitmapUri: Uri = Uri.parse(imgBitmapPath)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/*"
        shareIntent.putExtra(Intent.EXTRA_STREAM, imgBitmapUri)
        shareIntent.putExtra(Intent.EXTRA_TEXT,
            "${pet.title}\n\n" +
                    "${pet.description}\n" +
                    "${resources.getString(R.string.address)}: ${pet.address}" +
                    "${resources.getString(R.string.phone_number)}: ${pet.phone}" +
                    resources.getString(R.string.share_url, pet.id))
        startActivity(Intent.createChooser(shareIntent, resources.getString(R.string.share)))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_share, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.share -> share()
        }
        return super.onOptionsItemSelected(item)
    }
}