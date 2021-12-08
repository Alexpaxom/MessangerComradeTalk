package com.alexpaxom.homework_2.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.adapters.ChannelsTabAdapter
import com.alexpaxom.homework_2.databinding.FragmentChannelsBinding
import com.google.android.material.tabs.TabLayoutMediator

class ChannelsFragment: ViewBindingFragment<FragmentChannelsBinding>() {

    override fun createBinding(): FragmentChannelsBinding =
        FragmentChannelsBinding.inflate(layoutInflater)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val ownUserId = arguments?.getInt(PARAM_OWNER_USER_ID) ?: error("User id is required!")

        val fragmentsIdsList = listOf(
            R.string.channels_tab_subscribed,
            R.string.channels_tab_all_streams
        )

        binding.cannelsViewPager.adapter =
            ChannelsTabAdapter(
                childFragmentManager,
                lifecycle,
                fragmentsIdsList = fragmentsIdsList,
                createFragmentById = { fragmentId ->
                    when(fragmentId) {
                        R.string.channels_tab_subscribed ->
                            ChannelsListSubscribedFragment.newInstance(ownUserId)
                        else ->
                            ChannelsListAllFragment.newInstance(ownUserId)
                    }
                }
            )


        TabLayoutMediator(binding.topNavMenu, binding.cannelsViewPager) { tab, position ->
            tab.text = resources.getString(fragmentsIdsList[position])
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
        (binding.cannelsViewPager.adapter as ChannelsTabAdapter).let { tabAdapter ->
            tabAdapter.fragmentAt(binding.cannelsViewPager.currentItem)?.let {
                (it as? ChannelsListFragment)?.apply { searchChannels(searchString) }
            }
        }
    }


    companion object {
        const val FRAGMENT_ID = "com.alexpaxom.CHANNELS_FRAGMENT_ID"
        private const val PARAM_OWNER_USER_ID = "com.alexpaxom.USER_ID_PARAM"

        @JvmStatic
        fun newInstance(ownerUserId: Int) = ChannelsFragment().apply {
            arguments = Bundle().apply {
                putInt(PARAM_OWNER_USER_ID, ownerUserId)
            }
        }
    }
}