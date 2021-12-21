package com.alexpaxom.homework_2.app.features.mainnavigation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.App
import com.alexpaxom.homework_2.app.features.baseelements.BaseView
import com.alexpaxom.homework_2.app.features.baseelements.ViewBindingFragment
import com.alexpaxom.homework_2.app.features.channels.fragments.ChannelsFragment
import com.alexpaxom.homework_2.app.features.mainnavigation.activities.MainNavigationEffect
import com.alexpaxom.homework_2.app.features.mainnavigation.activities.MainNavigationState
import com.alexpaxom.homework_2.app.features.mainnavigation.adapters.MainNavigationViewpageAdapter
import com.alexpaxom.homework_2.app.features.userprofile.fragments.ProfileFragment
import com.alexpaxom.homework_2.app.features.userslist.fragments.UsersFragment
import com.alexpaxom.homework_2.databinding.FragmentMainNavigationBinding
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject
import javax.inject.Provider


class MainNavigationFragment : ViewBindingFragment<FragmentMainNavigationBinding>(),
    BaseView<MainNavigationState, MainNavigationEffect> {

    override fun createBinding(): FragmentMainNavigationBinding =
        FragmentMainNavigationBinding.inflate(layoutInflater)

    @Inject
    lateinit var daggerPresenter: Provider<MainNavigationPresenter>

    @InjectPresenter
    lateinit var presenter: MainNavigationPresenter

    @ProvidePresenter
    fun providePresenter(): MainNavigationPresenter = daggerPresenter.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        (requireActivity().application as App).appComponent
            .getScreenComponent()
            .create()
            .inject(this)

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }


    override fun processState(state: MainNavigationState) {
        binding.mainActivityProgress.isVisible = state.isEmptyLoad
        state.userInfo?.let { user -> init(user.id) }
    }

    override fun processEffect(effect: MainNavigationEffect) {
        when (effect) {
            is MainNavigationEffect.ShowError -> showError(effect.error)
        }
    }

    private fun init(ownUserId: Int) {
        binding.mainNavigatinViewPager.adapter =
            MainNavigationViewpageAdapter(
                childFragmentManager,
                lifecycle,
                fragmentsIdsList = listOf(
                    R.id.bottom_menu_item_channels,
                    R.id.bottom_menu_item_people,
                    R.id.bottom_menu_item_profile
                ),
                createFragmentById = {
                    when (it) {
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
        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
    }

    companion object {

        @JvmStatic
        fun newInstance() = MainNavigationFragment()
    }
}