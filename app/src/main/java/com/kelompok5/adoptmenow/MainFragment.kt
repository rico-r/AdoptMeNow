package com.kelompok5.adoptmenow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.kelompok5.adoptmenow.account.AccountFragment
import com.kelompok5.adoptmenow.databinding.FragmentMainBinding
import com.kelompok5.adoptmenow.history.HistoryFragment
import com.kelompok5.adoptmenow.home.HomeFragment
import com.kelompok5.adoptmenow.saved.SavedFragment


class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: FragmentMainBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main, container, false)

        // Disable swiping
        binding.mainPager.isUserInputEnabled = false

        binding.mainPager.adapter = object: FragmentStateAdapter(this) {
            val homeFragment = HomeFragment()
            val savedFragment = SavedFragment()
            val historyFragment = HistoryFragment()
            val accountFragment = AccountFragment()

            override fun getItemCount(): Int {
                return 4
            }

            override fun createFragment(position: Int): Fragment {
                return when(position) {
                    0 -> homeFragment
                    1 -> savedFragment
                    2 -> historyFragment
                    else -> accountFragment
                }
            }

        }

        TabLayoutMediator(binding.mainTabs, binding.mainPager) {tabs, position ->
            tabs.text = when(position) {
                0 -> resources.getString(R.string.home)
                1 -> resources.getString(R.string.saved)
                2 -> resources.getString(R.string.history)
                else -> resources.getString(R.string.account)
            }
            tabs.setIcon(when(position) {
                0 -> R.drawable.home
                1 -> R.drawable.bookmark
                2 -> R.drawable.history
                else -> R.drawable.user
            })
        }.attach()

        return binding.root
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
