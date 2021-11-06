package com.alexpaxom.homework_2.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.adapters.ChannelsTabAdapter
import com.alexpaxom.homework_2.databinding.FragmentChannelsBinding
import com.google.android.material.tabs.TabLayoutMediator
class ChannelsFragment: ViewBindingFragment<FragmentChannelsBinding>() {

    override var _binding: Lazy<FragmentChannelsBinding>? = lazy {
        FragmentChannelsBinding.inflate(layoutInflater)
    }

    val channelsTabAdapter = lazy { ChannelsTabAdapter(
            fragment = this,
            getChannelsTabsFragments()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding.cannelsViewPager.adapter = channelsTabAdapter.value

        val tabsNames = mapOf(
            POSITION_SUBSCRIBED_TAB_NAVIGATION to resources.getString(R.string.channels_tab_subscribed),
            POSITION_ALL_STREAMS_TAB_NAVIGATION to resources.getString(R.string.channels_tab_all_streams)
        )
        TabLayoutMediator(binding.topNavMenu, binding.cannelsViewPager) { tab, position ->
            tab.text = tabsNames[position]
        }.attach()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchChannels.searchEdit.doAfterTextChanged {
            searchInCurrentFragment(it.toString())
        }

        binding.searchChannels.searchBtn.setOnClickListener {
            searchInCurrentFragment(binding.searchChannels.searchEdit.text.toString())
        }
    }

    private fun searchInCurrentFragment(searchString: String) {
        channelsTabAdapter.value.fragmentAt(binding.cannelsViewPager.currentItem)?.let {
            (it as? ChannelsListFragment)?.apply { searchChannels(searchString) }
        }
    }

    private fun getChannelsTabsFragments(): Map<Int, Fragment> {
        // При первом заходе создаем новые фрагменты в последующем получаем их из FragmentManager
        val subscribed =
            childFragmentManager.findFragmentByTag("$VIEW_PAGER_TAG$POSITION_SUBSCRIBED_TAB_NAVIGATION")
                ?: ChannelsListFragment.newInstance()

        val allStreams =
            childFragmentManager.findFragmentByTag("$VIEW_PAGER_TAG$POSITION_ALL_STREAMS_TAB_NAVIGATION")
                ?: ChannelsListFragment.newInstance()
        return mapOf(
            POSITION_SUBSCRIBED_TAB_NAVIGATION to subscribed,
            POSITION_ALL_STREAMS_TAB_NAVIGATION to allStreams
        )
    }

    companion object {
        //private const val SAVED_BUNDLE_CHANNELS = "com.alexpaxom.SAVED_BUNDLE_CHANNELS"
        const val FRAGMENT_ID = "com.alexpaxom.CHANNELS_FRAGMENT_ID"

        private const val POSITION_SUBSCRIBED_TAB_NAVIGATION = 0
        private const val POSITION_ALL_STREAMS_TAB_NAVIGATION = 1
        private const val VIEW_PAGER_TAG = "f"


        @JvmStatic
        fun newInstance() = ChannelsFragment()
    }


}