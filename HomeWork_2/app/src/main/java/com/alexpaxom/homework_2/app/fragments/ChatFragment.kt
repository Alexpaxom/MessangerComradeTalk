package com.alexpaxom.homework_2.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.adapters.chathistory.ChatHistoryAdapter
import com.alexpaxom.homework_2.app.adapters.chathistory.ChatMessageFactory
import com.alexpaxom.homework_2.app.adapters.decorators.ChatDateDecorator
import com.alexpaxom.homework_2.customview.EmojiReactionCounter
import com.alexpaxom.homework_2.data.models.MessageItem
import com.alexpaxom.homework_2.data.models.ReactionItem
import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.MessageSendUseCaseZulipApiImpl
import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.MessagesLoadUseCaseZulipApiImpl
import com.alexpaxom.homework_2.databinding.FragmentChatBinding
import com.alexpaxom.homework_2.helpers.EmojiHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.*


class ChatFragment : DialogFragment(), ChatStateMachine {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val chatMessageFactory = ChatMessageFactory(
        { selectNewReaction(it) },
        { adapterPos, emojiView -> clickOnReaction(adapterPos, emojiView) }
    )

    override var currentState: ChatState = ChatState.InitialState
    private val chatHistoryAdapter = ChatHistoryAdapter(chatMessageFactory)
    private val compositeDisposable = CompositeDisposable()

    private val topicName = "swimming turtles"
    private val streamName = "general"
    private val streamId = 306312

    private val messagesLoader = MessagesLoadUseCaseZulipApiImpl(MY_USER_ID)
    private val messagesSender = MessageSendUseCaseZulipApiImpl()
    private val emojiHelper = EmojiHelper()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)


        // Обрабочики нажатий на элементы списка сообщений

        if(savedInstanceState == null) {
            messagesLoader.getMessages(
                messageId = MessagesLoadUseCaseZulipApiImpl.NEWEST_MESSAGE,
                numBefore = DEFAULT_COUNT_LOAD_MESSAGES,
                numAfter = 0,
                filter = null
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { goToState(ChatState.LoadingState) }
            .subscribeBy(
                onSuccess = { goToState(ChatState.ResultState(it)) },
                onError = { goToState(ChatState.ErrorState(it)) }
            )
            .addTo(compositeDisposable)
        }
        else {
            goToState(ChatState.ResultState(
                savedInstanceState.getParcelableArrayList<MessageItem>(SAVED_BUNDLE_MESSAGES)
                ?: listOf()))
        }

        binding.chatingHistory.layoutManager = LinearLayoutManager(context)
        binding.chatingHistory.adapter = chatHistoryAdapter


        val decorator = ChatDateDecorator(binding.chatingHistory)
        binding.chatingHistory.addItemDecoration(decorator)


        // Обработка результата выбора эмоджи через диалог
        childFragmentManager.setFragmentResultListener(FragmentEmojiSelector.EMOJI_SELECT_RESULT_DIALOG_ID, this,
            { _, resultBundle ->
                val emojiUnicode = resultBundle.getString(FragmentEmojiSelector.EMOJI_UNICODE)
                val messageId = resultBundle.getInt(FragmentEmojiSelector.RESULT_ID)
                emojiUnicode?.let { emojiUnicode ->
                    addReaction(
                        ReactionItem(
                            typeId = R.layout.emoji_for_select_view,
                            userId = MY_USER_ID,
                            emojiUnicode = emojiUnicode,
                            emojiName = emojiHelper.getNameByUnicode(emojiUnicode)
                        ),
                        messageId
                    )
                }
            })


        // Обработка нажатия на кнопку отправки сообщения
        binding.messageSendBtn.setOnClickListener() {
            binding.messageEnterEdit.text?.let {
                if (it.isNotEmpty()) {
                    sendMessage(it.toString())
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

        return binding.root
    }

    private fun sendMessage(message: String) {
        messagesSender.sendMessageToStream(streamId, topicName, message)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { goToState(ChatState.LoadingState) }
            .subscribeBy(
                onSuccess = {
                    loadingNewMessages(scrollToEnd = true)
                    binding.messageEnterEdit.text?.clear()
                },
                onError = { goToState(ChatState.ErrorState(it)) }
            )
            .addTo(compositeDisposable)
    }

    private fun loadingNewMessages(scrollToEnd: Boolean = false) {
        messagesLoader.getMessages(
            messageId = chatHistoryAdapter.dataList.last().id.toLong(),
            numBefore = 0,
            numAfter = MAX_COUNT_LOAD_MESSAGES,
            filter = null
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { goToState(ChatState.LoadingState) }
            .subscribeBy(
                onSuccess = {
                    goToState(
                        ChatState.AddingMessagesState(
                            position = chatHistoryAdapter.dataList.size,
                            messages = it,
                        )
                    )

                    if(scrollToEnd)
                        binding.chatingHistory.scrollToPosition(chatHistoryAdapter.dataList.size-1)
                },
                onError = { goToState(ChatState.ErrorState(it)) }
            )
            .addTo(compositeDisposable)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val list: ArrayList<MessageItem> = ArrayList(chatHistoryAdapter.dataList)
        outState.putParcelableArrayList(SAVED_BUNDLE_MESSAGES, list)
        super.onSaveInstanceState(outState)
    }

    private fun selectNewReaction(adapterPos: Int) {
        val fragmentEmojiSelector = FragmentEmojiSelector.newInstance(
            chatHistoryAdapter.dataList[adapterPos].id
        )
        fragmentEmojiSelector.show(childFragmentManager, FragmentEmojiSelector.EMOJI_SELECT_RESULT_DIALOG_ID)
    }

    private fun clickOnReaction(adapterPos: Int, emojiView: EmojiReactionCounter) {
        val reaction = ReactionItem(
            typeId = R.layout.emoji_for_select_view,
            userId = MY_USER_ID,
            emojiUnicode = emojiView.displayEmoji,
            emojiName = emojiHelper.getNameByUnicode(emojiView.displayEmoji)
        )

        if(emojiView.isSelected)
            addReaction(reaction, chatHistoryAdapter.dataList[adapterPos].id)
        else
            removeReaction(reaction, chatHistoryAdapter.dataList[adapterPos].id)
    }

    private fun addReaction(reaction: ReactionItem, messageId: Int) {
        messagesSender.addReaction(messageId, reaction.emojiName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    chatHistoryAdapter.addReactionByMessageID(
                        messageId,
                        reaction
                    )
                },
                onError = { goToState(ChatState.ErrorState(it)) }
            )
            .addTo(compositeDisposable)
    }

    private fun removeReaction(reaction: ReactionItem, messageId: Int) {
        messagesSender.removeReaction(messageId, reaction.emojiName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    chatHistoryAdapter.removeReactionByMessageID(
                        messageId,
                        reaction
                    )
                },
                onError = { goToState(ChatState.ErrorState(it)) }
            )
            .addTo(compositeDisposable)
    }

    override fun toResult(resultState: ChatState.ResultState) {
        binding.messagesLoadingProgress.isVisible = false

        chatHistoryAdapter.dataList = resultState.messages
        binding.chatingHistory.scrollToPosition(chatHistoryAdapter.dataList.size-1)
    }

    override fun toLoading(loadingState: ChatState.LoadingState) {
        binding.messagesLoadingProgress.isVisible = true
    }

    override fun toError(errorState: ChatState.ErrorState) {
        binding.messagesLoadingProgress.isVisible = false
        Toast.makeText(context, errorState.error.localizedMessage, Toast.LENGTH_LONG).show()
    }

    override fun toAddingMessages(newMessages: ChatState.AddingMessagesState) {
        binding.messagesLoadingProgress.isVisible = false
        chatHistoryAdapter.addMessagesToPosition(newMessages.position, newMessages.messages)
    }

    override fun getTheme(): Int {
        return R.style.Theme_DialogFragment
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    companion object {

        private const val SAVED_BUNDLE_MESSAGES = "com.alexpaxom.SAVED_BUNDLE_MESSAGES"
        private const val MY_USER_ID = 456094
        const val FRAGMENT_ID = "com.alexpaxom.CHAT_FRAGMENT_ID"
        private const val DEFAULT_COUNT_LOAD_MESSAGES = 100
        private const val MAX_COUNT_LOAD_MESSAGES = 1000
        @JvmStatic
        fun newInstance() = ChatFragment()
    }

}