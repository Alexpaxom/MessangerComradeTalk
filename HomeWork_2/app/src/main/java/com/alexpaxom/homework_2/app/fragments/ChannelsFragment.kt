package com.alexpaxom.homework_2.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.adapters.ChannelsTabAdapter
import com.alexpaxom.homework_2.databinding.FragmentChannelsBinding
import com.google.android.material.tabs.TabLayoutMediator

class ChannelsFragment: Fragment() {
    private var _binding: FragmentChannelsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChannelsBinding.inflate(inflater, container, false)

        val channelsTabAdapter = ChannelsTabAdapter(
            fragment = this,
            tabsNames = mapOf(
                0 to resources.getString(R.string.channels_tab_subscribed),
                1 to resources.getString(R.string.channels_tab_all_streams)
            )
        )
        binding.cannelsViewPager.adapter = channelsTabAdapter


        TabLayoutMediator(binding.topNavMenu, binding.cannelsViewPager) { tab, position ->
            tab.text = channelsTabAdapter.tabsNames[position]
        }.attach()


        return binding.root
    }

    companion object {
        //private const val SAVED_BUNDLE_CHANNELS = "com.alexpaxom.SAVED_BUNDLE_CHANNELS"
        const val FRAGMENT_ID = "com.alexpaxom.CHANNELS_FRAGMENT_ID"

        @JvmStatic
        fun newInstance() = ChannelsFragment()
    }
}