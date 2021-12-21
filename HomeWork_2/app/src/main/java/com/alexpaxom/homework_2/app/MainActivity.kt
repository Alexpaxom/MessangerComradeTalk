package com.alexpaxom.homework_2.app

import android.os.Bundle
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.features.login.fragments.LoginFragment
import com.alexpaxom.homework_2.app.features.mainnavigation.fragments.MainNavigationFragment
import com.alexpaxom.homework_2.databinding.ActivityMainBinding
import com.alexpaxom.homework_2.domain.entity.LoginResult
import moxy.MvpAppCompatActivity


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


}