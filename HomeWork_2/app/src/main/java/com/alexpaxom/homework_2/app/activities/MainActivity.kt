package com.alexpaxom.homework_2.app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.alexpaxom.homework_2.app.adapters.BaseAdapterCallback
import com.alexpaxom.homework_2.app.adapters.ChatHistoryAdapter
import com.alexpaxom.homework_2.app.adapters.decorators.ChatDateDecorator
import com.alexpaxom.homework_2.app.fragments.FragmentEmojiSelector
import com.alexpaxom.homework_2.data.models.Message
import com.alexpaxom.homework_2.data.models.Reaction
import com.alexpaxom.homework_2.data.repositories.TestMessagesRepository
import com.alexpaxom.homework_2.databinding.FragmentChatBinding
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    lateinit var binding: FragmentChatBinding

    private val chatHistoryAdapter = ChatHistoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = FragmentChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Обрабочики нажатий на элементы списка сообщений

        chatHistoryAdapter.attachCallback(object: BaseAdapterCallback<Message> {
            override fun onItemClick(model: Message, view: ViewBinding){}

            override fun onLongClick(model: Message, view: ViewBinding): Boolean {
                val fragmentEmojiSelector = FragmentEmojiSelector.newInstance(model.id)
                fragmentEmojiSelector.show(supportFragmentManager, FragmentEmojiSelector.EMOJI_SELECT_RESULT_DIALOG_ID)
                return true
            }
        })

        if(savedInstanceState == null) {
            chatHistoryAdapter.updateItems(TestMessagesRepository().getMessages(30))
        }
        else {

            val arr = savedInstanceState.getParcelableArrayList<Message>(SAVED_BUNDLE_MESSAGES)
            chatHistoryAdapter.updateItems(arr?: listOf())
        }

        binding.chatingHistory.layoutManager = LinearLayoutManager(this)
        binding.chatingHistory.adapter = chatHistoryAdapter


        val decorator = ChatDateDecorator(binding.chatingHistory)
        binding.chatingHistory.addItemDecoration(decorator)

        chatHistoryAdapter.setOnReactionClickListener { message ->
            if(isSelected) {
                chatHistoryAdapter.addReactionByMessageID(
                    message.id,
                    Reaction(
                        MY_USER_ID,
                        this.displayEmoji
                    )
                )
            }
            else {
                chatHistoryAdapter.removeReactionByMessageID(
                    message.id,
                    Reaction(
                        MY_USER_ID,
                        this.displayEmoji
                    )
                )
            }
        }


        // Обработка результата выбора эмоджи через диалог

        supportFragmentManager.setFragmentResultListener(FragmentEmojiSelector.EMOJI_SELECT_RESULT_DIALOG_ID, this,
            { _, resultBundle ->
                val emojiUnicode = resultBundle.getString(FragmentEmojiSelector.EMOJI_UNICODE)
                val messageId = resultBundle.getInt(FragmentEmojiSelector.RESULT_ID)
                emojiUnicode?.let { emojiUnicode ->
                    chatHistoryAdapter.addReactionByMessageID(messageId, Reaction(MY_USER_ID, emojiUnicode))
                }
            })


        // Обработка нажатия на кнопку отправки сообщения

        binding.messageSendBtn.setOnClickListener() {
            binding.messageEnterEdit.text?.let {
                if (it.isNotEmpty()) {
                    chatHistoryAdapter.addItem(Message(
                        MESSAGE_ID++,
                        MY_USER_ID,
                        "My message",
                        binding.messageEnterEdit.text.toString(),
                        Date(),
                        null
                    ))
                    binding.messageEnterEdit.text?.clear()
                }
            }
        }

        // Обработка состояний при вводе данных в поле сообщения

        binding.messageEnterEdit.doOnTextChanged { text, start, before, count ->
            binding.messageEnterEdit.text?.let {
                binding.messageSendBtn.setImageResource(
                    if(it.isNotEmpty())
                        android.R.drawable.ic_menu_send
                    else
                        android.R.drawable.ic_menu_add
                )
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
        private var MESSAGE_ID = 1000 // временное поле нужно для того что бы у сообщений было уникльное id
    }
}