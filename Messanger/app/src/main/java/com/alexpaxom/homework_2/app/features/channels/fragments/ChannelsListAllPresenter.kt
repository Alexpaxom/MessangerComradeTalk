package com.alexpaxom.homework_2.app.features.channels.fragments

import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.EditChannelsUseCaseZulip
import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.EditTopicsUseCaseZulip
import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.LoadChannelUseCaseZulip
import com.alexpaxom.homework_2.di.screen.ScreenScope
import com.alexpaxom.homework_2.domain.cache.helpers.CachedWrapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@ScreenScope
@InjectViewState
class ChannelsListAllPresenter @Inject constructor(
    private val loadChannelUseCase:LoadChannelUseCaseZulip,
    editChannelsUseCaseZulip: EditChannelsUseCaseZulip,
    editTopicsUseCaseZulip: EditTopicsUseCaseZulip
): ChannelsListPresenter(
    loadChannelUseCase,
    editChannelsUseCaseZulip,
    editTopicsUseCaseZulip
) {

    private val compositeDisposable = CompositeDisposable()

    init {
        initChannelsGroupSearchListener()
        // загружаем первые данные о пользователях
        super.searchChannels(INITIAL_SEARCH_QUERY)
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
                loadChannelUseCase.searchInAllChannelGroups(it)
                    .subscribeOn(Schedulers.io())
            }
            .doOnError { initChannelsGroupSearchListener() }
            .observeOn(AndroidSchedulers.mainThread(), true)
            .subscribeBy(
                onNext = { channelsGroups ->

                    currentViewState = ChannelsViewState(
                        isEmptyLoad = channelsGroups is CachedWrapper.CachedData,
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

    companion object {
        private const val INITIAL_SEARCH_QUERY = ""
    }
}