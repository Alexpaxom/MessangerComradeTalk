package com.alexpaxom.homework_2.domain.repositories.zulipapirepositories

import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

private const val CHAT_URL = "https://tinkoff-android-fall21.zulipchat.com/"
private const val LOGIN = "email"
private const val PASSWORD = "api-kay"

object GetRetrofitObject {
    private val httpLoggingInterceptor = HttpLoggingInterceptor()

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .authenticator(object: Authenticator {
            override fun authenticate(route: Route?, response: Response): Request? {
                if (response.request.header("Authorization") != null)
                    error("Bad login or password")
                return response.request.newBuilder()
                    .header("Authorization", Credentials.basic(LOGIN, PASSWORD))
                    .build();
            }

        })
        .build();


    val retrofit = Retrofit.Builder()
        .baseUrl(CHAT_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()


    init {
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    }





}