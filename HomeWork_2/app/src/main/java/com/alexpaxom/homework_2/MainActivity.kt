package com.alexpaxom.homework_2

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.TextView
import com.alexpaxom.homework_2.customview.EmojiReactionCounter
import com.alexpaxom.homework_2.customview.FlexBoxLayout
import com.alexpaxom.homework_2.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.massage.userName = "Иванов Иван"
        binding.massage.messageText = "Привет! Это тестовое сообщение. На него можно не отвечать, лучше отдохни и выпей чаю."

        binding.massage.setOnReactionClickListener {
            countReaction += if(isSelected) -1 else 1

            if(countReaction < 1)
                binding.massage.removeReaction(this)
            else
                isSelected = !isSelected
        }

        binding.massage.setAvatarByUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/1/19/Vladimir_Putin_11-10-2020_%28cropped%29.jpg/250px-Vladimir_Putin_11-10-2020_%28cropped%29.jpg")


        binding.massage.addReaction()
        binding.massage.addReaction("\uD83D\uDE06")
        binding.massage.addReaction("\uD83E\uDD23", count=10, selected= true)
        binding.massage.addReaction("\uD83E\uDD29")
        binding.massage.addReaction("\uD83D\uDE1D")
        binding.massage.addReaction("\uD83E\uDD2A", selected= true)
        binding.massage.addReaction("\uD83D\uDC4D", count=99)
        binding.massage.addReaction("\uD83D\uDE10")
        binding.massage.addReaction("\uD83D\uDE0E")
        binding.massage.addReaction("\uD83D\uDC7B")
        binding.massage.addReaction("\uD83D\uDD96")



    }
}