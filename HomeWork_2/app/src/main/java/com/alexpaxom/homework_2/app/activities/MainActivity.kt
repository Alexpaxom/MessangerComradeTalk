package com.alexpaxom.homework_2.app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
               binding.mainFragmentContainer.id,
                ProfileFragment.newInstance(user),
                ProfileFragment.FRAGMENT_ID
            ).commit()
        }
    }
}