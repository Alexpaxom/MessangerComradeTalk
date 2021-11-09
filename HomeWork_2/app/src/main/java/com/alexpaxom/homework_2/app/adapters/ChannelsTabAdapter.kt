package com.alexpaxom.homework_2.app.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alexpaxom.homework_2.app.fragments.ChannelsListFragment

class ChannelsTabAdapter(fragment: Fragment, val tabsNames: Map<Int, String>): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = tabsNames.size

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> ChannelsListFragment.newInstance()
            1 -> ChannelsListFragment.newInstance()
            else-> error("Bad position channels adapter: $position max position can be ${tabsNames.size}")
        }
    }
}