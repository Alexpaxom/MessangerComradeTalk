package com.alexpaxom.homework_2.app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.adapters.MainNavigationViewpageAdapter
import com.alexpaxom.homework_2.app.fragments.ChannelsFragment
import com.alexpaxom.homework_2.app.fragments.ProfileFragment
import com.alexpaxom.homework_2.app.fragments.UsersFragment
import com.alexpaxom.homework_2.databinding.ActivityMainBinding
import com.alexpaxom.homework_2.domain.repositories.zulipapirepositories.UsersZulipDateRepositoryImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private val mainNavigationViewpageAdapter = lazy {
        MainNavigationViewpageAdapter(
            supportFragmentManager,
            lifecycle,
            getMainNavigationFragments()
        )
    }

    private var ownUserId = 0

    private val compositeDisposable = CompositeDisposable()

    private val usersRepository = UsersZulipDateRepositoryImpl()

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

        usersRepository.getUserById()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    ownUserId = it.id
                    createAdapter()
                },
                onError = { error(it.localizedMessage) }
            )
            .addTo(compositeDisposable)


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

    private fun createAdapter() {
        binding.mainNavigatinViewPager.adapter = mainNavigationViewpageAdapter.value
        binding.mainNavigatinViewPager.isUserInputEnabled = false;
    }

    private fun getMainNavigationFragments(): Map<Int, Fragment> {

        // При первом заходе создаем новые фрагменты в последующем получаем их из FragmentManager
        val channelFragment =
            supportFragmentManager.findFragmentByTag("$VIEW_PAGER_TAG$POSITION_CHANNELS_BOTTOM_NAVIGATION")
                ?: ChannelsFragment.newInstance()

        val usersFragment =
            supportFragmentManager.findFragmentByTag("$VIEW_PAGER_TAG$POSITION_PEOPLE_BOTTOM_NAVIGATION")
                ?: UsersFragment.newInstance()

        val profileFragment =
            supportFragmentManager.findFragmentByTag("$VIEW_PAGER_TAG$POSITION_PROFILE_BOTTOM_NAVIGATION")
                ?: ProfileFragment.newInstance(ownUserId, true)

        return mapOf(
            POSITION_CHANNELS_BOTTOM_NAVIGATION to channelFragment,
            POSITION_PEOPLE_BOTTOM_NAVIGATION to usersFragment,
            POSITION_PROFILE_BOTTOM_NAVIGATION to profileFragment,
        )
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }


    companion object {
        private const val POSITION_CHANNELS_BOTTOM_NAVIGATION = 0
        private const val POSITION_PEOPLE_BOTTOM_NAVIGATION = 1
        private const val POSITION_PROFILE_BOTTOM_NAVIGATION = 2
        private const val VIEW_PAGER_TAG = "f"
    }
}