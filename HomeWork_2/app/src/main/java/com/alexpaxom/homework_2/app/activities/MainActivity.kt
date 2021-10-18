package com.alexpaxom.homework_2.app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Adapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexpaxom.homework_2.app.adapters.ChatHistoryAdapter
import com.alexpaxom.homework_2.customview.MassageViewGroup
import com.alexpaxom.homework_2.data.models.Message
import com.alexpaxom.homework_2.data.models.Reaction
import com.alexpaxom.homework_2.data.repositories.TestMessagesRepository
import com.alexpaxom.homework_2.databinding.FragmentChatBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding: FragmentChatBinding

    private val chatHistory: ArrayList<Message> = arrayListOf()
    private val chatHistoryAdapter = ChatHistoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = FragmentChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(savedInstanceState == null) {
            chatHistoryAdapter.updateItems(TestMessagesRepository().getMessages(30))
        }
        else {

            val arr = savedInstanceState.getParcelableArrayList<Message>(SAVED_BUNDLE_MESSAGES)
            chatHistoryAdapter.updateItems(arr?: listOf())
        }

        binding.chatingHistory.layoutManager = LinearLayoutManager(this)
        binding.chatingHistory.adapter = chatHistoryAdapter

        chatHistoryAdapter.setOnReactionClickListener { parentMessage, message ->
            if(isSelected) {
                message.reactionList.add(
                    Reaction(
                        MY_USER_ID,
                        this.displayEmoji
                    )
                )
            }
            else {
                message.reactionList.remove(Reaction(
                    MY_USER_ID,
                    this.displayEmoji
                ))

                // это вынесено на случай ошибки сервера, если например нет связи
                // что бы не удалять реакцию раньше времени, но возможно нужно перенести в MassageViewGroup
                if(countReaction == 0)
                    parentMessage.removeReaction(this)
            }


        }

        super.onCreate(savedInstanceState)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        val list: ArrayList<Message> = ArrayList(chatHistoryAdapter.dataList)
        outState.putParcelableArrayList(SAVED_BUNDLE_MESSAGES, list)
        super.onSaveInstanceState(outState)
    }

    companion object {
        private const val SAVED_BUNDLE_MESSAGES = "com.alexpaxom.SAVED_BUNDLE_MESSAGES"
        private const val MY_USER_ID = 99999
    }
}