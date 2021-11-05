package com.alexpaxom.homework_2.app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.adapters.MainNavigationViewpageAdapter
import com.alexpaxom.homework_2.app.fragments.ChannelsFragment
import com.alexpaxom.homework_2.app.fragments.FragmentWrapperContainer
import com.alexpaxom.homework_2.app.fragments.ProfileFragment
import com.alexpaxom.homework_2.app.fragments.UsersFragment
import com.alexpaxom.homework_2.data.repositories.TestRepositoryImpl
import com.alexpaxom.homework_2.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private val mainNavigationViewpageAdapter = lazy {
        MainNavigationViewpageAdapter(
            supportFragmentManager,
            lifecycle,
            getMainNavigationFragments()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

        binding.mainNavigatinViewPager.adapter = mainNavigationViewpageAdapter.value
        binding.mainNavigatinViewPager.isUserInputEnabled = false;

        if (savedInstanceState == null) {
            val userId = TestRepositoryImpl().getUsers().first().id
            // Обработчик нажатий основного нижнего меню
            binding.mainBottomNavMenu.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.bottom_menu_item_channels ->
                        binding.mainNavigatinViewPager.currentItem =
                            POSITION_CHANNELS_BOTTOM_NAVIGATION

                    R.id.bottom_menu_item_people ->
                        binding.mainNavigatinViewPager.currentItem =
                            POSITION_PEOPLE_BOTTOM_NAVIGATION

                    R.id.bottom_menu_item_profile ->
                        binding.mainNavigatinViewPager.currentItem =
                            POSITION_PROFILE_BOTTOM_NAVIGATION
                }

                true
            }
        }
    }

    private fun getMainNavigationFragments(): Map<Int, Fragment> {

        val userId = TestRepositoryImpl().getUsers().first().id

        // При первом заходе создаем новые фрагменты в последующем получаем их из FragmentManager
        val channelFragment =
            supportFragmentManager.findFragmentByTag("$VIEW_PAGER_TAG$POSITION_CHANNELS_BOTTOM_NAVIGATION")
                ?: FragmentWrapperContainer.newInstance(ChannelsFragment.FRAGMENT_ID)

        val usersFragment =
            supportFragmentManager.findFragmentByTag("$VIEW_PAGER_TAG$POSITION_PEOPLE_BOTTOM_NAVIGATION")
                ?: FragmentWrapperContainer.newInstance(UsersFragment.FRAGMENT_ID)

        val profileFragment =
            supportFragmentManager.findFragmentByTag("$VIEW_PAGER_TAG$POSITION_PROFILE_BOTTOM_NAVIGATION")
                ?: ProfileFragment.newInstance(userId, true)

        return mapOf(
            POSITION_CHANNELS_BOTTOM_NAVIGATION to channelFragment,
            POSITION_PEOPLE_BOTTOM_NAVIGATION to usersFragment,
            POSITION_PROFILE_BOTTOM_NAVIGATION to profileFragment,
        )
    }


    override fun onBackPressed() {
        mainNavigationViewpageAdapter.value.fragmentAt(binding.mainNavigatinViewPager.currentItem)
            ?.let {
            //if assert with 0 will delete fragment and shown empty screen
            if(it.childFragmentManager.backStackEntryCount > 1) {
                it.childFragmentManager.popBackStack()
                return
            }
        }

        super.onBackPressed()
    }

    companion object {
        private const val POSITION_CHANNELS_BOTTOM_NAVIGATION = 0
        private const val POSITION_PEOPLE_BOTTOM_NAVIGATION = 1
        private const val POSITION_PROFILE_BOTTOM_NAVIGATION = 2
        private const val VIEW_PAGER_TAG = "f"
    }
}