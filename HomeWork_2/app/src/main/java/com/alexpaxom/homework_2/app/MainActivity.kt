package com.alexpaxom.homework_2.app

import android.content.Context
import android.net.*
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.os.Bundle
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.features.login.fragments.LoginFragment
import com.alexpaxom.homework_2.app.features.mainnavigation.fragments.MainNavigationFragment
import com.alexpaxom.homework_2.databinding.ActivityMainBinding
import com.alexpaxom.homework_2.domain.entity.LoginResult
import moxy.MvpAppCompatActivity
import android.os.Build
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.core.view.isVisible


class MainActivity : MvpAppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).appComponent
            .getScreenComponent()
            .create()
            .inject(this)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Проверка есть ли на момент запуска подключение к интернету
        binding.noInternetStatus.isVisible = !hasInternetConnection()

        // Обработчик логина пользователя
        supportFragmentManager.setFragmentResultListener(
            LoginFragment.FRAGMENT_ID,
            this
        ) { _, resultBundle ->
            resultBundle.getParcelable<LoginResult>(LoginFragment.RESULT_LOGIN_PARAM)?.let {
                (application as App).reCreateAppComponent(it)

                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container, MainNavigationFragment.newInstance())
                    .commit()
            }
        }

    }

    fun hasInternetConnection(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            cm.allNetworks
                .mapNotNull(cm::getNetworkCapabilities)
                .any { capabilities -> capabilities.hasCapability(NET_CAPABILITY_INTERNET) }
        } else {
            cm.activeNetworkInfo?.isConnected == true
        }
    }


}