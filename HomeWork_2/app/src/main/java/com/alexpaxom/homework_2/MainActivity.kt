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

        binding.massage.nameTextView.text = "Иванов Иван"
        binding.massage.messageTextView.text = "Привет! Это тестовое сообщение. На него можно не отвечать, лучше отдохни и выпей чаю."

        binding.massage.reactionsListLayout.apply {
            repeat(10) {
                val emoji = View.inflate(this@MainActivity, R.layout.emoji_view, null)

                emoji.setOnClickListener {
                    it as EmojiReactionCounter
                    it.countReaction += if(it.isSelected) -1 else 1

                    it.isSelected = !it.isSelected
                }

                addView(
                    emoji,
                    LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                )
            }
        }
    }
}