package com.alexpaxom.homework_2.app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.adapters.chathistory.ChatHistoryAdapter
import com.alexpaxom.homework_2.app.adapters.chathistory.ChatMessageFactory
import com.alexpaxom.homework_2.app.adapters.decorators.ChatDateDecorator
import com.alexpaxom.homework_2.app.fragments.FragmentEmojiSelector
import com.alexpaxom.homework_2.customview.EmojiReactionCounter
import com.alexpaxom.homework_2.data.models.Message
import com.alexpaxom.homework_2.data.models.Reaction
import com.alexpaxom.homework_2.data.models.ReactionsGroup
import com.alexpaxom.homework_2.data.repositories.TestMessagesRepository
import com.alexpaxom.homework_2.databinding.FragmentChatBinding
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    lateinit var binding: FragmentChatBinding

    private val chatMessageFactory = ChatMessageFactory(
        { selectNewReaction(it) },
        { adapterPos, emojiView -> clickOnReaction(adapterPos, emojiView) }
    )

    private val chatHistoryAdapter = ChatHistoryAdapter(chatMessageFactory)

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = FragmentChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Обрабочики нажатий на элементы списка сообщений

        if(savedInstanceState == null) {
            chatHistoryAdapter.dataList = TestMessagesRepository().getMessages(30)
        }
        else {

            val arr = savedInstanceState.getParcelableArrayList<Message>(SAVED_BUNDLE_MESSAGES)
            chatHistoryAdapter.dataList = arr?: listOf()
        }

        binding.chatingHistory.layoutManager = LinearLayoutManager(this)
        binding.chatingHistory.adapter = chatHistoryAdapter
        binding.chatingHistory.scrollToPosition(chatHistoryAdapter.dataList.size-1)


        val decorator = ChatDateDecorator(binding.chatingHistory)
        binding.chatingHistory.addItemDecoration(decorator)


        // Обработка результата выбора эмоджи через диалог
        supportFragmentManager.setFragmentResultListener(FragmentEmojiSelector.EMOJI_SELECT_RESULT_DIALOG_ID, this,
            { _, resultBundle ->
                val emojiUnicode = resultBundle.getString(FragmentEmojiSelector.EMOJI_UNICODE)
                val messageId = resultBundle.getInt(FragmentEmojiSelector.RESULT_ID)
                emojiUnicode?.let { emojiUnicode ->
                    chatHistoryAdapter.addReactionByMessageID(
                        messageId,
                        Reaction(
                            R.layout.emoji_for_select_view,
                            MY_USER_ID,
                            emojiUnicode
                        )
                    )
                }
            })


        // Обработка нажатия на кнопку отправки сообщения
        binding.messageSendBtn.setOnClickListener() {
            binding.messageEnterEdit.text?.let {
                if (it.isNotEmpty()) {
                    chatHistoryAdapter.addItem(Message(
                        typeId = R.layout.my_message_item,
                        id = MESSAGE_ID++,
                        userId = MY_USER_ID,
                        userName = "My message",
                        text = binding.messageEnterEdit.text.toString(),
                        datetime = Date(),
                        avatarUrl = null,
                        reactionsGroup = ReactionsGroup(listOf(), MY_USER_ID)
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

    private fun selectNewReaction(adapterPos: Int) {
        val fragmentEmojiSelector = FragmentEmojiSelector.newInstance(
            chatHistoryAdapter.dataList[adapterPos].id
        )
        fragmentEmojiSelector.show(supportFragmentManager, FragmentEmojiSelector.EMOJI_SELECT_RESULT_DIALOG_ID)
    }

    private fun clickOnReaction(adapterPos: Int, emojiView: EmojiReactionCounter) {
        if(emojiView.isSelected) {
            chatHistoryAdapter.addReactionByMessageID(
                chatHistoryAdapter.dataList[adapterPos].id,
                Reaction(
                    R.layout.emoji_for_select_view,
                    MY_USER_ID,
                    emojiView.displayEmoji
                )
            )
        }
        else {
            chatHistoryAdapter.removeReactionByMessageID(
                chatHistoryAdapter.dataList[adapterPos].id,
                Reaction(
                    R.layout.emoji_for_select_view,
                    MY_USER_ID,
                    emojiView.displayEmoji
                )
            )
        }
    }

    companion object {
        private const val SAVED_BUNDLE_MESSAGES = "com.alexpaxom.SAVED_BUNDLE_MESSAGES"
        private const val MY_USER_ID = 99999
        private var MESSAGE_ID = 1000 // временное поле нужно для того что бы у сообщений было уникльное id
    }
}