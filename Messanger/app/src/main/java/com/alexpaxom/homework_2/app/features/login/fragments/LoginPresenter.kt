package com.alexpaxom.homework_2.app.features.login.fragments

import com.alexpaxom.homework_2.app.features.baseelements.BaseView
import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.LoginUseCaseZulip
import com.alexpaxom.homework_2.di.screen.ScreenScope
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter
import retrofit2.HttpException
import javax.inject.Inject

@ScreenScope
@InjectViewState
class LoginPresenter @Inject constructor(
    private val loginUseCaseZulip: LoginUseCaseZulip
): MvpPresenter<BaseView<LoginState, LoginEffect>>() {

    private var currentViewState: LoginState = LoginState()
        set(value) {
            field = value
            viewState.processState(value)
        }

    private val compositeDisposable = CompositeDisposable()

    init {
        logIn(LoginEvent.LogIn())
    }

    fun processEvent(event: LoginEvent) {
        when(event) {
            is LoginEvent.LogIn -> logIn(event)
        }
    }

    private fun logIn(event: LoginEvent.LogIn) {
        loginUseCaseZulip.logIn(
            username = event.login,
            password = event.password,
            organizationPath = processUrl(event.chatUrl ?: "")
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                currentViewState = currentViewState.copy(isEmptyLoad = true)
            }
            .subscribeBy(
                onSuccess = {
                    viewState.processEffect(LoginEffect.Logined(it.data))
                },
                onError = {
                    currentViewState = currentViewState.copy(isEmptyLoad = false)
                    processError(it)
                }
            )
            .addTo(compositeDisposable)
    }

    private fun processError(error: Throwable) {
        val errorMsg = if(error is HttpException)
            error.response()?.errorBody()?.string() ?: "Load messages http Error!"
        else
            error.localizedMessage ?: "Load messages error!"

        viewState.processEffect(
            LoginEffect.ShowError(errorMsg)
        )
    }

    private fun processUrl(url: String): String {
        val newUrl = URL_PREFIX_HTTPS +
                url.replace(URL_PREFIX_HTTPS, "", ignoreCase = true)
                    .replace(URL_PREFIX_HTTP, "", ignoreCase = true)

        return if(newUrl.endsWith("/")) {
            newUrl.substring(0, newUrl.length-1);
        } else {
            newUrl
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    companion object {
        private const val URL_PREFIX_HTTPS = "https://"
        private const val URL_PREFIX_HTTP = "http://"
    }
}