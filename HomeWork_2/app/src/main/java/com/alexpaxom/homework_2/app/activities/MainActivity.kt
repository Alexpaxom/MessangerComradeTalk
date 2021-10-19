package com.alexpaxom.homework_2.app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.adapters.BaseAdapterCallback
import com.alexpaxom.homework_2.app.adapters.ChatHistoryAdapter
import com.alexpaxom.homework_2.app.fragments.FragmentEmojiSelector
import com.alexpaxom.homework_2.data.models.Message
import com.alexpaxom.homework_2.data.models.Reaction
import com.alexpaxom.homework_2.data.repositories.TestMessagesRepository
import com.alexpaxom.homework_2.databinding.FragmentChatBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding: FragmentChatBinding

    private val chatHistoryAdapter = ChatHistoryAdapter()

    val fragmentEmojiSelector = FragmentEmojiSelector()



    override fun onCreate(savedInstanceState: Bundle?) {

        binding = FragmentChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chatHistoryAdapter.attachCallback(object: BaseAdapterCallback<Message> {
            override fun onItemClick(model: Message, view: ViewBinding){}

            override fun onLongClick(model: Message, view: ViewBinding): Boolean {
                val params = Bundle()
                params.putInt(FragmentEmojiSelector.MESSAGE_ID, model.id)
                fragmentEmojiSelector.arguments = params
                fragmentEmojiSelector.show(supportFragmentManager, FragmentEmojiSelector.TAG)

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

        fragmentEmojiSelector.setOnResultCallback { resultBundle ->
            val emojiUnicode = resultBundle.getString(FragmentEmojiSelector.EMOJI_UNICODE)
            val messageId = resultBundle.getInt(FragmentEmojiSelector.MESSAGE_ID)
            emojiUnicode?.let { emojiUnicode ->
                chatHistoryAdapter.addReactionByMessageID(messageId, Reaction(MY_USER_ID, emojiUnicode))
            }
        }

        binding.messageSendBtn.setOnClickListener() {
            chatHistoryAdapter.addItem(Message(
                MESSAGE_ID++,
                "My message",
                binding.messageEnterEdit.text.toString(),
                null
            ))
            binding.messageEnterEdit.text?.clear()
        }

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