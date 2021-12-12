package com.alexpaxom.homework_2.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.App
import com.alexpaxom.homework_2.app.adapters.BaseElements.PagingRecyclerUtil
import com.alexpaxom.homework_2.app.adapters.chathistory.ChatHistoryAdapter
import com.alexpaxom.homework_2.app.adapters.chathistory.ChatMessageFactory
import com.alexpaxom.homework_2.app.adapters.decorators.ChatDateDecorator
import com.alexpaxom.homework_2.customview.EmojiReactionCounter
import com.alexpaxom.homework_2.databinding.FragmentChatBinding
import moxy.MvpAppCompatDialogFragment
import moxy.presenter.InjectPresenter

import moxy.presenter.ProvidePresenter
import javax.inject.Inject
import javax.inject.Provider

class ChatFragment : MvpAppCompatDialogFragment(), BaseView<ChatViewState, ChatEffect> {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val chatMessageFactory = ChatMessageFactory(
        { selectNewReaction(it) },
        { adapterPos, emojiView -> clickOnReaction(adapterPos, emojiView) }
    )

    private val chatParams: Lazy<ChatParams> = lazy {
        ChatParams(
            topicName = arguments?.getString(ARGUMENT_TOPIC_NAME) ?: DEFAULT_TOPIC_NAME,
            channelName = arguments?.getString(ARGUMENT_CHANNEL_NAME) ?: error("Bad parameter 'channel name' "),
            channelId = arguments?.getInt(ARGUMENT_CHANNEL_ID) ?: error("Bad parameter 'channel id' "),
            myUserId = arguments?.getInt(ARGUMENT_MY_USER_ID) ?: error("Bad parameter 'user id' ")
        )
    }

    private val chatHistoryAdapter = ChatHistoryAdapter(chatMessageFactory)

    private var chatPager: ChatPager? = null

    @Inject
    lateinit var daggerPresenter: Provider<ChatPresenter>

    @InjectPresenter
    lateinit var presenter: ChatPresenter

    @ProvidePresenter
    fun providePresenter(): ChatPresenter = daggerPresenter.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        (requireActivity().application as App).appComponent
            .getScreenComponent()
            .create()
            .inject(this)

        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.Theme_DialogFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)

        // Обрабочики нажатий на элементы списка сообщений
        binding.channelName.text = "#${chatParams.value.channelName}"

        binding.topicName.text = chatParams.value.topicName
        binding.sendTopicName.text = chatParams.value.topicName

        binding.topicName.isVisible = chatParams.value.topicName != DEFAULT_TOPIC_NAME
        binding.sendTopicName.isVisible = chatParams.value.topicName == DEFAULT_TOPIC_NAME

        if(savedInstanceState == null)
            presenter.processEvent(ChatEvent.LoadHistory(chatParams.value))

        binding.chatingHistory.layoutManager = LinearLayoutManager(context)
        binding.chatingHistory.adapter = chatHistoryAdapter
        chatPager = ChatPager(binding.chatingHistory)


        val decorator = ChatDateDecorator(binding.chatingHistory)
        binding.chatingHistory.addItemDecoration(decorator)


        // Обработка результата выбора эмоджи через диалог
        childFragmentManager.setFragmentResultListener(FragmentEmojiSelector.EMOJI_SELECT_RESULT_DIALOG_ID, this,
            { _, resultBundle ->
                val emojiUnicode = resultBundle.getString(FragmentEmojiSelector.EMOJI_UNICODE)
                val messageId = resultBundle.getInt(FragmentEmojiSelector.RESULT_ID)
                emojiUnicode?.let { emojiUnicode ->
                    presenter.processEvent(
                        ChatEvent.EmojiStateChange(emojiUnicode, messageId, true, chatParams.value)
                    )
                }
            })


        // Обработка нажатия на кнопку отправки сообщения
        binding.messageSendBtn.setOnClickListener() {
            binding.messageEnterEdit.text?.let {
                if (it.isNotEmpty()) {
                    presenter.processEvent(
                        ChatEvent.SendMessage(it.toString(), chatParams.value)
                    )
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

        chatHistoryAdapter.setAfterUpdateDataCallBack {
            presenter.processEvent(ChatEvent.MessagesInserted)
        }

        return binding.root
    }

    private fun selectNewReaction(adapterPos: Int) {
        val fragmentEmojiSelector = FragmentEmojiSelector.newInstance(
            chatHistoryAdapter.dataList[adapterPos].id
        )
        fragmentEmojiSelector.show(childFragmentManager, FragmentEmojiSelector.EMOJI_SELECT_RESULT_DIALOG_ID)
    }

    private fun clickOnReaction(adapterPos: Int, emojiView: EmojiReactionCounter) {
        presenter.processEvent(
            ChatEvent.EmojiStateChange(
                emojiView.displayEmoji,
                chatHistoryAdapter.dataList[adapterPos].id,
                emojiView.isSelected,
                chatParams.value
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun processState(state: ChatViewState) {
        chatHistoryAdapter.dataList = state.messages
        binding.messagesLoadingProgress.isVisible = state.isEmptyLoading
    }

    override fun processEffect(effect: ChatEffect) {
        when(effect) {
            is ChatEffect.ScrollToPosition -> scrollToMessage(effect.massageId, effect.isSmoothScroll)
            is ChatEffect.ShowError -> showError(effect.error)
        }
    }

    private fun showError(errorMsg: String) {
        binding.messagesLoadingProgress.isVisible = false
        Toast.makeText( context, errorMsg, Toast.LENGTH_LONG).show()
    }

    private fun scrollToMessage(messageId: Int, isSmoothScroll: Boolean) {
        val pos = chatHistoryAdapter.dataList.indexOfLast { it.id == messageId }
        if(pos != -1) {
            if (isSmoothScroll)
                binding.chatingHistory.smoothScrollToPosition(pos)
            else
                binding.chatingHistory.scrollToPosition(pos)
        }
        else
            error("Try scroll to not exist position message pos: $pos")
    }

    companion object {
        private const val DEFAULT_TOPIC_NAME = "(no topic)"
        private const val ARGUMENT_TOPIC_NAME = "com.alexpaxom.ARGUMENT_TOPIC_NAME"
        private const val ARGUMENT_CHANNEL_NAME = "com.alexpaxom.ARGUMENT_STREAM_NAME"
        private const val ARGUMENT_CHANNEL_ID = "com.alexpaxom.ARGUMENT_STREAM_ID"
        private const val ARGUMENT_MY_USER_ID = "com.alexpaxom.ARGUMENT_MY_USER_ID"

        const val FRAGMENT_ID = "com.alexpaxom.CHAT_FRAGMENT_ID"

        @JvmStatic
        fun newInstance(
            topicName: String = "",
            channelName: String,
            channelId: Int,
            myUserId: Int
        ) = ChatFragment().apply {
            arguments = Bundle().apply {
                putString(ARGUMENT_TOPIC_NAME, topicName)
                putString(ARGUMENT_CHANNEL_NAME, channelName)
                putInt(ARGUMENT_CHANNEL_ID, channelId)
                putInt(ARGUMENT_MY_USER_ID, myUserId)
            }
        }
    }

    inner class ChatPager(recyclerView: RecyclerView) : PagingRecyclerUtil(recyclerView) {
        override fun checkLoadData(bottomPos: Int, topPos: Int) {
            presenter.processEvent(ChatEvent.ChangedScrollPosition(bottomPos, topPos, chatParams.value))
        }
    }
}