package com.alexpaxom.homework_2.app.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainNavigationViewpageAdapter(
    private val fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    val fragmentsList: Map<Int, Fragment> = mapOf()
): FragmentStateAdapter(fragmentManager, lifecycle) {


    override fun getItemCount(): Int = fragmentsList.size

    override fun createFragment(position: Int): Fragment {
        return fragmentsList[position] ?: error("Not found fragment for this position")
    }

    fun fragmentAt(position: Int): Fragment? {
        return fragmentsList[position]
    }
}