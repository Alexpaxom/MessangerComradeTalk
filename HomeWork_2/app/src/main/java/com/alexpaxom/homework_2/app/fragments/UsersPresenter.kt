package com.alexpaxom.homework_2.app.fragments

import com.alexpaxom.homework_2.data.models.UserItem
import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.SearchUsersZulip
import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.UserStatusUseCaseZulip
import com.alexpaxom.homework_2.di.screen.ScreenComponent
import com.alexpaxom.homework_2.domain.cache.helpers.CachedWrapper
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import moxy.MvpPresenter
import retrofit2.HttpException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UsersPresenter(
    screenComponent: ScreenComponent
): MvpPresenter<BaseView<UsersViewState, UsersEffect>>() {

    @Inject
    lateinit var searchUsers: SearchUsersZulip
    @Inject
    lateinit var userStatusInfo:UserStatusUseCaseZulip

    private var currentViewState: UsersViewState = UsersViewState()
        set(value) {
            field = value
            viewState.processState(value)
        }

    private val compositeDisposable = CompositeDisposable()

    private var searchUsersSubject: BehaviorSubject<String>? = null

    init {
        screenComponent.inject(this)
        initUsersSearchListener()
        // загружаем первую порцию данных
        searchUsers(INITIAL_SEARCH_QUERY)
    }

    fun processEvent(event: UsersEvent) {
        when(event) {
            is UsersEvent.SearchUsers -> searchUsers(event.searchString)
        }
    }

    private fun initUsersSearchListener() {

        searchUsersSubject = BehaviorSubject.create()

        searchUsersSubject!!
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .distinctUntilChanged()
            .debounce(500, TimeUnit.MILLISECONDS, Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                currentViewState = UsersViewState(
                    isEmptyLoad = true
                )
            }
            .observeOn(Schedulers.io())
            .switchMap { searchUsers.search(it)
                .subscribeOn(Schedulers.io())
                .concatMapSingleDelayError { usersWrap ->
                    when(usersWrap) {
                        is CachedWrapper.CachedData ->
                            Single.just(usersWrap)

                        is CachedWrapper.OriginalData ->
                            loadUsersStates(usersWrap.data )
                                .map{ CachedWrapper.OriginalData(it) }
                    }
                }
            }
            .doOnError { initUsersSearchListener() }
            .observeOn(AndroidSchedulers.mainThread(), true)
            .subscribeBy(
                onNext = {usersWrap->
                    currentViewState = UsersViewState(
                        users = usersWrap.data.sortedBy{ it.name}
                    )
                },
                onError = { processError(it) }
            )
            .addTo(compositeDisposable)
    }

    private fun searchUsers(searchString: String) {
        searchUsersSubject?.onNext(searchString)
    }

    private fun loadUsersStates(users: List<UserItem>): Single<List<UserItem>> {

        val usersMap = users.map { it.id to it }.toMap()

        return Observable.fromIterable(users)
            .subscribeOn(Schedulers.io())
            .concatMapSingle {
                userStatusInfo.getStatusForUser(it.id)
            }
            .toList()
            .map { statuses ->
                statuses.map { status ->
                    usersMap[status.userId]!!.copy(status = status)
                }
            }
    }

    private fun processError(error: Throwable) {
        val errorMsg = if(error is HttpException)
            error.response()?.errorBody()?.string() ?: "Http Error!"
        else
            error.localizedMessage ?: "Error!"

        viewState.processEffect(
            UsersEffect.ShowError(errorMsg)
        )
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    companion object {
        private const val INITIAL_SEARCH_QUERY = ""
    }
}