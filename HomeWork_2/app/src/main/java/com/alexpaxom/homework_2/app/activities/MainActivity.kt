package com.alexpaxom.homework_2.app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.fragments.ChannelsFragment
import com.alexpaxom.homework_2.app.fragments.ProfileFragment
import com.alexpaxom.homework_2.app.fragments.UsersFragment
import com.alexpaxom.homework_2.data.repositories.TestMessagesRepository
import com.alexpaxom.homework_2.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

        val user = TestMessagesRepository().getUsers().first()

        // Обработчик нажатий основного нижнего меню
        binding.mainBottomNavMenu.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.bottom_menu_item_channels -> navigate(
                    ChannelsFragment.newInstance(),
                    ChannelsFragment.FRAGMENT_ID
                )

                R.id.bottom_menu_item_people -> navigate(
                    UsersFragment.newInstance(),
                    UsersFragment.FRAGMENT_ID
                )

                R.id.bottom_menu_item_profile -> navigate(
                    ProfileFragment.newInstance(user.id, true),
                    ProfileFragment.FRAGMENT_ID
                )
            }

            true
        }

        if(savedInstanceState == null) {
            // Выбираем пункт основного нижнего меню по умолчанию и открываем переходим
            binding.mainBottomNavMenu.selectedItemId = R.id.bottom_menu_item_channels
        }
    }

    fun navigate(fragment: Fragment, backstackId: String, addToBackStack:Boolean = false) {
        val transition = supportFragmentManager.beginTransaction().replace(
            binding.mainFragmentContainer.id,
            fragment,
            backstackId
        )

        if(addToBackStack) {
            transition.addToBackStack(backstackId)
        }

        transition.commit()
    }
}