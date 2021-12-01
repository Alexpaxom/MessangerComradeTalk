package com.alexpaxom.homework_2.app.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alexpaxom.homework_2.app.fragments.ChannelsListFragment

class ChannelsTabAdapter(
    private val fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val fragmentsIdsList: List<Int> = listOf(),
    private val createFragmentById: (fragmentId: Int) -> Fragment
): FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = fragmentsIdsList.size

    override fun createFragment(position: Int): Fragment {
        return createFragmentById(fragmentsIdsList[position])
    }

    fun fragmentAt(pos: Int): Fragment? {
        return fragmentManager.findFragmentByTag(
            "$VIEW_PAGER_TAG$pos"
        )
    }

    companion object {
        private const val VIEW_PAGER_TAG = "f"
    }
}