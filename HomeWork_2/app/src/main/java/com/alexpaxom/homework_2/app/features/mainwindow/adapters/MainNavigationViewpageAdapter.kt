package com.alexpaxom.homework_2.app.features.mainwindow.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainNavigationViewpageAdapter(
    private val fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val fragmentsIdsList: List<Int> = listOf(),
    private val createFragmentById: (fragmentId: Int) -> Fragment
): FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = fragmentsIdsList.size

    override fun createFragment(position: Int): Fragment {
        return createFragmentById(fragmentsIdsList[position])
    }

    fun getFragmentPositionById(id: Int): Int {
        val pos = fragmentsIdsList.indexOfLast { it == id }
        return if(pos != -1) pos else error("Fragment with this id not exists!")
    }

    fun getFragmentById(id: Int): Fragment? {
        return fragmentManager.findFragmentByTag(
            "$VIEW_PAGER_TAG${getFragmentPositionById(id)}"
        )
    }

    companion object {
        private const val VIEW_PAGER_TAG = "f"
    }
}