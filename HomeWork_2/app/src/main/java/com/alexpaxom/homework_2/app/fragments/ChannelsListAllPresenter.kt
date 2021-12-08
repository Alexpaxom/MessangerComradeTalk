package com.alexpaxom.homework_2.app.fragments

import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.SearchExpandedChannelGroupZulip
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class ChannelsListAllPresenter(
    private val searchExpandedChannelGroup:SearchExpandedChannelGroupZulip =
        SearchExpandedChannelGroupZulip()
): ChannelsListPresenter() {
    private val compositeDisposable = CompositeDisposable()

    init {
        initChannelsGroupSearchListener()
        // загружаем первые данные о пользователях
        super.searchChannels(ChannelsListFragment.INITIAL_SEARCH_QUERY)
    }

    override fun initChannelsGroupSearchListener() {
        super.initChannelsGroupSearchListener()

        searchChannelsSubject!!
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                currentViewState = ChannelsViewState(isEmptyLoad = true)
            }
            .observeOn(Schedulers.io())
            .debounce(500, TimeUnit.MILLISECONDS, Schedulers.io())
            .switchMap {
                searchExpandedChannelGroup.searchInAllChannelGroups(it)
                    .subscribeOn(Schedulers.io())
            }
            .doOnError { initChannelsGroupSearchListener() }
            .observeOn(AndroidSchedulers.mainThread(), true)
            .subscribeBy(
                onNext = { channelsGroups ->

                    currentViewState = ChannelsViewState(
                        channels = refreshExpandedState(channelsGroups.data)
                    )
                },
                onError = { processError(it) }
            )
            .addTo(compositeDisposable)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}