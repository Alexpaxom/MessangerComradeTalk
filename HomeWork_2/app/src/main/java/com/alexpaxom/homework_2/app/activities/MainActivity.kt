package com.alexpaxom.homework_2.app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexpaxom.homework_2.app.adapters.ChatHistoryAdapter
import com.alexpaxom.homework_2.data.models.Message
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
            chatHistory.addAll(TestMessagesRepository().getMessages(30))
        }
        else {

            val arr = savedInstanceState.getParcelableArrayList<Message>(SAVED_BUNDLE_MESSAGES)
            chatHistory.addAll(arr?: listOf())
        }

        binding.chatingHistory.layoutManager = LinearLayoutManager(this)
        binding.chatingHistory.adapter = chatHistoryAdapter

        chatHistoryAdapter.updateItems(chatHistory)

        super.onCreate(savedInstanceState)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(SAVED_BUNDLE_MESSAGES, chatHistory)
        super.onSaveInstanceState(outState)
    }

    companion object {
        private const val SAVED_BUNDLE_MESSAGES = "com.alexpaxom.SAVED_BUNDLE_MESSAGES"
    }
}