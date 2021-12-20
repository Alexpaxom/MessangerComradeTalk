package com.alexpaxom.homework_2.app.features.mainwindow.activities

import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.App
import com.alexpaxom.homework_2.app.features.mainwindow.adapters.MainNavigationViewpageAdapter
import com.alexpaxom.homework_2.app.features.baseelements.BaseView
import com.alexpaxom.homework_2.app.features.channels.fragments.ChannelsFragment
import com.alexpaxom.homework_2.app.features.userprofile.fragments.ProfileFragment
import com.alexpaxom.homework_2.app.features.userslist.fragments.UsersFragment
import com.alexpaxom.homework_2.databinding.ActivityMainBinding
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject
import javax.inject.Provider


class MainActivity : MvpAppCompatActivity(), BaseView<MainActivityState, MainActivityEffect> {

    lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var daggerPresenter: Provider<MainActivityPresenter>

    @InjectPresenter
    lateinit var presenter: MainActivityPresenter

    @ProvidePresenter
    fun providePresenter(): MainActivityPresenter = daggerPresenter.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).appComponent
            .getScreenComponent()
            .create()
            .inject(this)

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