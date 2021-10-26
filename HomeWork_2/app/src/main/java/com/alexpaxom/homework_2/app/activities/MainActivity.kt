package com.alexpaxom.homework_2.app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.fragments.ChannelsFragment
import com.alexpaxom.homework_2.app.fragments.ChatFragment
import com.alexpaxom.homework_2.app.fragments.ProfileFragment
import com.alexpaxom.homework_2.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
               binding.mainFragmentContainer.id,
                ChannelsFragment.newInstance(),
                ChannelsFragment.FRAGMENT_ID
            ).commit()
        }
    }
}