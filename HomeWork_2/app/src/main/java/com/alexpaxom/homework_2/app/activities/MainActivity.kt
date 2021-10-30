package com.alexpaxom.homework_2.app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.fragments.ChannelsFragment
import com.alexpaxom.homework_2.app.fragments.FragmentWrapperContainer
import com.alexpaxom.homework_2.app.fragments.ProfileFragment
import com.alexpaxom.homework_2.app.fragments.UsersFragment
import com.alexpaxom.homework_2.data.repositories.TestMessagesRepository
import com.alexpaxom.homework_2.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private var currentFragmentTag: String = ChannelsFragment.FRAGMENT_ID

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)


        if(savedInstanceState == null) {
            val userId = TestMessagesRepository().getUsers().first().id

            val channelFragment = FragmentWrapperContainer.newInstance(ChannelsFragment.FRAGMENT_ID)
            val usersFragment = FragmentWrapperContainer.newInstance(UsersFragment.FRAGMENT_ID)
            val profileFragment =  ProfileFragment.newInstance(userId, true)
            supportFragmentManager.beginTransaction()
                .add(binding.mainFragmentContainer.id, channelFragment, ChannelsFragment.FRAGMENT_ID)
                .add(binding.mainFragmentContainer.id, usersFragment, UsersFragment.FRAGMENT_ID)
                .hide(usersFragment)
                .add(binding.mainFragmentContainer.id, profileFragment, ProfileFragment.FRAGMENT_ID)
                .hide(profileFragment)
                .commit()
        }
        else
            currentFragmentTag = savedInstanceState.getString(SAVE_CURRENT_FRAGMENT_TAG) ?: currentFragmentTag



        // Обработчик нажатий основного нижнего меню
        binding.mainBottomNavMenu.setOnItemSelectedListener { it ->
            val currentFragment = supportFragmentManager.findFragmentByTag(currentFragmentTag)
                ?: error("Cent find current fragment")

            when(it.itemId) {
                R.id.bottom_menu_item_channels -> {
                    supportFragmentManager.findFragmentByTag(ChannelsFragment.FRAGMENT_ID)?.let {
                        supportFragmentManager.beginTransaction()
                            .hide(currentFragment)
                            .show(it)
                            .commit()
                    }
                    currentFragmentTag = ChannelsFragment.FRAGMENT_ID
                }


                R.id.bottom_menu_item_people -> {
                    supportFragmentManager.findFragmentByTag(UsersFragment.FRAGMENT_ID)?.let {
                        supportFragmentManager.beginTransaction()
                            .hide(currentFragment)
                            .show(it)
                            .commit()
                    }
                    currentFragmentTag = UsersFragment.FRAGMENT_ID
                }


                R.id.bottom_menu_item_profile -> {
                    supportFragmentManager.findFragmentByTag(ProfileFragment.FRAGMENT_ID)?.let {
                        supportFragmentManager.beginTransaction()
                            .hide(currentFragment)
                            .show(it)
                            .commit()
                    }
                    currentFragmentTag = ProfileFragment.FRAGMENT_ID
                }
            }

            true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(SAVE_CURRENT_FRAGMENT_TAG, currentFragmentTag)
        super.onSaveInstanceState(outState)
    }


    override fun onBackPressed() {
        supportFragmentManager.findFragmentByTag(currentFragmentTag)?.let {
            //if assert with 0 will delete last fragment and shown empty screen
            if(it.childFragmentManager.backStackEntryCount > 1) {
                it.childFragmentManager.popBackStack()
                return
            }
        }

        super.onBackPressed()
    }

    companion object {
        private const val SAVE_CURRENT_FRAGMENT_TAG = "com.alexpaxom.SAVE_CURRENT_FRAGMENT_TAG"
    }
}