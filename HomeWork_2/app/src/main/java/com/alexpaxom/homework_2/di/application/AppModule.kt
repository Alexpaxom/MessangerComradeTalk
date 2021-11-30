package com.alexpaxom.homework_2.di.application

import android.content.Context
import androidx.room.Room
import com.alexpaxom.homework_2.app.App
import com.alexpaxom.homework_2.di.screen.ScreenComponent
import com.alexpaxom.homework_2.domain.cache.CacheDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

private const val CHAT_URL = "https://tinkoff-android-fall21.zulipchat.com/"
private const val LOGIN = "email"
private const val PASSWORD = "api-kay"

@Module(
    includes = [AppModule.BindingModule::class],
    subcomponents = [ScreenComponent::class]
)
class AppModule {

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .authenticator(object: Authenticator {
                override fun authenticate(route: Route?, response: Response): Request? {
                    if (response.request.header("Authorization") != null)
                        error("Bad login or password")
                    return response.request.newBuilder()
                        .header("Authorization", Credentials.basic(LOGIN, PASSWORD))
                        .build()
                }

            })
            .build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(CHAT_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    fun provide(context: Context): CacheDatabase {
        return Room.databaseBuilder(
            context,
            CacheDatabase::class.java, "cacheDatabase"
        ).build()
    }

    @Module
    interface BindingModule {
        @Binds
        fun bind(application: App): Context
    }
}