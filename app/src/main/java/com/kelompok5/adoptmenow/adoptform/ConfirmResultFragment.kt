package com.kelompok5.adoptmenow.adoptform

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kelompok5.adoptmenow.R
import com.kelompok5.adoptmenow.databinding.FragmentConfirmResultBinding

class ConfirmResultFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentConfirmResultBinding.inflate(inflater, container, false)
        val args = ConfirmResultFragmentArgs.fromBundle(requireArguments())
        when(args.result) {
            "accepting" -> {
                binding.anim.setImageResource(R.drawable.anim_accepting)
                binding.message.setText(R.string.form_accepted)
            }
            "rejecting" -> {
                binding.anim.setImageResource(R.drawable.anim_rejecting)
                binding.message.setText(R.string.form_rejected)
                binding.printButton.visibility = View.GONE
            }
            "accepted" -> {
                binding.anim.setImageResource(R.drawable.anim_adopt_confirmed)
                binding.message.setText(R.string.your_form_accepted)
            }
            "rejected" -> {
                binding.anim.setImageResource(R.drawable.anim_adopt_rejected)
                binding.message.setText(R.string.your_form_rejected)
                binding.printButton.visibility = View.GONE
            }
            else -> throw RuntimeException()
        }
        (binding.anim.drawable as AnimationDrawable).start()

        binding.okButton.setOnClickListener {
            this.findNavController().navigateUp()
        }
        return binding.root
    }
}