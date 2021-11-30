package com.alexpaxom.homework_2.app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.adapters.MainNavigationViewpageAdapter
import com.alexpaxom.homework_2.app.fragments.ChannelsFragment
import com.alexpaxom.homework_2.app.fragments.ProfileFragment
import com.alexpaxom.homework_2.app.fragments.UsersFragment
import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.UserProfileUseCaseZulipApi
import com.alexpaxom.homework_2.databinding.ActivityMainBinding
import com.alexpaxom.homework_2.domain.cache.helpers.CachedWrapper
import com.alexpaxom.homework_2.domain.repositories.zulipapirepositories.UsersZulipDateRepository
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

    var ownUserId = 0

    private val compositeDisposable = CompositeDisposable()

    private val profileHandler = UserProfileUseCaseZulipApi()

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

        if(savedInstanceState == null || savedInstanceState.getInt(SAVE_BUNDLE_MY_USER_ID_KEY) == 0) {
            profileHandler.getUserByID()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread(), true)
                .subscribeBy(
                    onNext = {
                        ownUserId = it.data.id
                    },
                    onError = {
                        // Если произошла ошибка но мы смогли получить данные из кэша
                        if(ownUserId != 0)
                            init(ownUserId)
                        showError(it.localizedMessage)
                    },
                    onComplete = { init(ownUserId) }
                )
                .addTo(compositeDisposable)
        }
        else
            init(savedInstanceState.getInt(SAVE_BUNDLE_MY_USER_ID_KEY))
    }

    private fun init(ownUserId: Int) {
        this.ownUserId = ownUserId
        binding.mainNavigatinViewPager.adapter = mainNavigationViewpageAdapter.value
        binding.mainNavigatinViewPager.isUserInputEnabled = false

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

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(SAVE_BUNDLE_MY_USER_ID_KEY, ownUserId)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    private fun showError(errorMsg: String) {
        Toast.makeText(applicationContext, errorMsg, Toast.LENGTH_SHORT).show()
    }


    companion object {
        private const val POSITION_CHANNELS_BOTTOM_NAVIGATION = 0
        private const val POSITION_PEOPLE_BOTTOM_NAVIGATION = 1
        private const val POSITION_PROFILE_BOTTOM_NAVIGATION = 2
        private const val SAVE_BUNDLE_MY_USER_ID_KEY = "com.alexpaxom.SAVE_BUNDLE_MY_USER_ID_KEY"
        private const val VIEW_PAGER_TAG = "f"
    }
}