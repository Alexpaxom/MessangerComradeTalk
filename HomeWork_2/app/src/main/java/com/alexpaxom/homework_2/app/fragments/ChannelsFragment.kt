package com.alexpaxom.homework_2.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.adapters.ChannelsTabAdapter
import com.alexpaxom.homework_2.databinding.FragmentChannelsBinding
import com.google.android.material.tabs.TabLayoutMediator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class ChannelsFragment: ViewBindingFragment<FragmentChannelsBinding>() {

    override var _binding: Lazy<FragmentChannelsBinding>? = lazy {
        FragmentChannelsBinding.inflate(layoutInflater)
    }

    private val compositeDisposable = CompositeDisposable()
    private val searchSearchSubject: PublishSubject<String> = PublishSubject.create()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val channelsTabAdapter = ChannelsTabAdapter(
            fragment = this,
            tabsNames = mapOf(
                0 to resources.getString(R.string.channels_tab_subscribed),
                1 to resources.getString(R.string.channels_tab_all_streams)
            )
        )
        binding.cannelsViewPager.adapter = channelsTabAdapter


        TabLayoutMediator(binding.topNavMenu, binding.cannelsViewPager) { tab, position ->
            tab.text = channelsTabAdapter.tabsNames[position]
        }.attach()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchChannels.searchEdit.doAfterTextChanged {
            searchSearchSubject.onNext(it.toString())
        }

        searchSearchSubject
            .subscribeOn(Schedulers.io())
            .distinctUntilChanged()
            .debounce(500, TimeUnit.MILLISECONDS, Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { searchString ->
                    val curFragment = childFragmentManager.findFragmentByTag (
                        "f" + binding.cannelsViewPager.currentItem
                    )

                    (curFragment as? ChannelsListFragment)?.apply {
                        //searchInChannels(searchString)
                    }

                },
                onError =  { throw it }
            )
            .addTo(compositeDisposable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.dispose()
        _binding = null
    }

    companion object {
        //private const val SAVED_BUNDLE_CHANNELS = "com.alexpaxom.SAVED_BUNDLE_CHANNELS"
        const val FRAGMENT_ID = "com.alexpaxom.CHANNELS_FRAGMENT_ID"

        @JvmStatic
        fun newInstance() = ChannelsFragment()
    }


}