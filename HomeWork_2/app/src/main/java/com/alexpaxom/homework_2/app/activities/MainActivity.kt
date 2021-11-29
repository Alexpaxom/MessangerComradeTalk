package com.alexpaxom.homework_2.app.activities

import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.adapters.MainNavigationViewpageAdapter
import com.alexpaxom.homework_2.app.fragments.*
import com.alexpaxom.homework_2.databinding.ActivityMainBinding
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter


class MainActivity : MvpAppCompatActivity(), BaseView<MainActivityState, MainActivityEffect> {

    lateinit var binding: ActivityMainBinding

    @InjectPresenter
    lateinit var presenter: MainActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
    }

    override fun processState(state: MainActivityState) {
        binding.mainActivityProgress.isVisible = state.isEmptyLoad
        state.userInfo?.let{ user -> init(user.id) }
    }

    override fun processEffect(effect: MainActivityEffect) {
        when(effect) {
            is MainActivityEffect.ShowError -> showError(effect.error)
        }
    }

    private fun init(ownUserId: Int) {
        binding.mainNavigatinViewPager.adapter =
            MainNavigationViewpageAdapter(
                supportFragmentManager,
                lifecycle,
                fragmentsIdsList = listOf(
                    R.id.bottom_menu_item_channels,
                    R.id.bottom_menu_item_people,
                    R.id.bottom_menu_item_profile
                ),
                createFragmentById = {
                    when(it) {
                        R.id.bottom_menu_item_channels ->
                            ChannelsFragment.newInstance(ownUserId)
                        R.id.bottom_menu_item_people ->
                            UsersFragment.newInstance()
                        else -> ProfileFragment.newInstance(ownUserId, true)
                    }
                }
            )

        binding.mainNavigatinViewPager.isUserInputEnabled = false

        // Обработчик нажатий основного нижнего меню
        binding.mainBottomNavMenu.setOnItemSelectedListener { menuItem ->
            (binding.mainNavigatinViewPager.adapter as MainNavigationViewpageAdapter)
                .let { pageAdapter ->
                binding.mainNavigatinViewPager.currentItem =
                    pageAdapter.getFragmentPositionById(menuItem.itemId)
            }
            true
        }
    }

    private fun showError(errorMsg: String) {
        Toast.makeText(applicationContext, errorMsg, Toast.LENGTH_SHORT).show()
    }
}