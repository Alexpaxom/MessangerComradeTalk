package com.alexpaxom.homework_2.app.features.chat.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.App
import com.alexpaxom.homework_2.app.features.baseelements.adapters.PagingRecyclerUtil
import com.alexpaxom.homework_2.app.features.chat.adapters.ChatHistoryAdapter
import com.alexpaxom.homework_2.app.features.chat.adapters.ChatMessageFactory
import com.alexpaxom.homework_2.app.features.chat.adapters.decorators.ChatDateDecorator
import com.alexpaxom.homework_2.customview.EmojiReactionCounter
import com.alexpaxom.homework_2.data.models.MessageItem
import com.alexpaxom.homework_2.data.models.TopicItem
import com.alexpaxom.homework_2.databinding.FragmentChatBinding
import moxy.MvpAppCompatDialogFragment
import moxy.presenter.InjectPresenter

import moxy.presenter.ProvidePresenter
import java.util.*
import javax.inject.Inject
import javax.inject.Provider

import android.content.ClipData
import android.content.ClipboardManager

import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.alexpaxom.homework_2.app.features.baseelements.BaseView
import com.alexpaxom.homework_2.app.features.menubottom.fragments.BottomMenuFragment
import com.alexpaxom.homework_2.app.features.emojiselector.fragments.FragmentEmojiSelector
import com.alexpaxom.homework_2.app.features.topicselector.fragments.SelectTopicFragment


class ChatFragment : MvpAppCompatDialogFragment(), BaseView<ChatViewState, ChatEffect> {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val chatMessageFactory = ChatMessageFactory(
        { openMenu(it) },
        { adapterPos, emojiView -> clickOnReaction(adapterPos, emojiView) }
    )

    private val clipboard: Lazy<ClipboardManager?> = lazy {
        getSystemService(
            requireContext(),
            ClipboardManager::class.java
        )
    }

    private val chatParams: Lazy<ChatParams> = lazy {
        ChatParams(
            topicName = arguments?.getString(ARGUMENT_TOPIC_NAME),
            channelName = arguments?.getString(ARGUMENT_CHANNEL_NAME)
                ?: error("Bad parameter 'channel name' "),
            channelId = arguments?.getInt(ARGUMENT_CHANNEL_ID)
                ?: error("Bad parameter 'channel id' "),
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

        val topicName = arguments?.getString(ARGUMENT_TOPIC_NAME)

        binding.topicName.text = topicName ?: DEFAULT_TOPIC_NAME
        binding.sendTopicName.text = topicName ?: DEFAULT_TOPIC_NAME

        binding.topicName.isVisible = topicName != null
        binding.sendTopicName.isVisible = topicName == null

        if (savedInstanceState == null)
            presenter.processEvent(ChatEvent.LoadHistory(chatParams.value))

        binding.chatingHistory.layoutManager = LinearLayoutManager(context)
        binding.chatingHistory.adapter = chatHistoryAdapter
        chatPager = ChatPager(binding.chatingHistory)


        val decorator = ChatDateDecorator(binding.chatingHistory)
        binding.chatingHistory.addItemDecoration(decorator)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.exitFromChat.setOnClickListener {
            dismiss()
        }

        // Обработка результата выбора эмоджи через диалог
        childFragmentManager.setFragmentResultListener(
            FragmentEmojiSelector.EMOJI_SELECT_RESULT_DIALOG_ID,
            this,
            { _, resultBundle ->
                val messageId = resultBundle.getInt(FragmentEmojiSelector.RESULT_ID)
                resultBundle.getString(FragmentEmojiSelector.EMOJI_UNICODE)?.let { emojiUnicode ->
                    presenter.processEvent(
                        ChatEvent.EmojiStateChange(emojiUnicode, messageId, true, chatParams.value)
                    )
                }
            })


        // Обработка нажатия на кнопку отправки сообщения
        binding.messageSendBtn.setOnClickListener() {
            binding.messageEnterEdit.text?.let {
                if (it.isEmpty())
                    return@let

                // Берем редактируемое сообщение или создаем новое
                val message = presenter.currentViewState.editMessage ?: MessageItem()

                presenter.processEvent(
                    ChatEvent.SendMessage(
                        message.copy(
                            text = binding.messageEnterEdit.text.toString(),
                            topicName = binding.sendTopicName.text.toString()
                        ),
                        chatParams.value
                    )
                )

                binding.messageEnterEdit.text?.clear()
            }
        }

        // Обработка состояний при вводе данных в поле сообщения
        binding.messageEnterEdit.doAfterTextChanged { text ->
            text?.let {
                binding.messageSendBtn.setImageResource(
                    if (presenter.currentViewState.editMessage != null)
                        R.drawable.ic_check_24
                    else {
                        if (it.isNotEmpty())
                            android.R.drawable.ic_menu_send
                        else
                            android.R.drawable.ic_menu_add
                    }
                )
            }
        }

        chatHistoryAdapter.setAfterUpdateDataCallBack {
            presenter.processEvent(ChatEvent.MessagesInserted)
        }

        childFragmentManager.setFragmentResultListener(
            BottomMenuFragment.FRAGMENT_ID,
            this
        ) { _, resultBundle ->
            val menuItemId = resultBundle.getInt(BottomMenuFragment.RESULT_PARAM_MENU_ITEM_ID)
            resultBundle.getParcelable<MessageItem>(BottomMenuFragment.RESULT_PARAM_CALLBACK_PARAMS)
                ?.let {
                    presenter.processEvent(ChatEvent.MessageContextMenuSelect(menuItemId, it))
                }

        }

        binding.sendTopicName.setOnClickListener {
            showTopicSelector()
        }

        binding.cancelEdit.setOnClickListener {
            presenter.processEvent(ChatEvent.EditMessage(cancel = true))
        }

        childFragmentManager.setFragmentResultListener(
            SelectTopicFragment.FRAGMENT_ID,
            this
        ) { _, resultBundle ->

            resultBundle.getParcelable<TopicItem>(SelectTopicFragment.RESULT_PARAM_TOPIC_ITEM)
                ?.let {
                    val message = resultBundle.getParcelable<MessageItem>(SelectTopicFragment.RESULT_PARAM_MESSAGE_ITEM)
                    if(message != null)
                        presenter.processEvent(
                            ChatEvent.SendMessage(
                                messageItem = message.copy(
                                    topicName = it.name
                                ),
                                chatParams = chatParams.value
                            )
                        )
                    else
                        binding.sendTopicName.text = it.name
                }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun selectNewReaction(messageId: Int) {
        val fragmentEmojiSelector = FragmentEmojiSelector.newInstance(
            messageId
        )
        fragmentEmojiSelector.show(
            childFragmentManager,
            FragmentEmojiSelector.EMOJI_SELECT_RESULT_DIALOG_ID
        )
    }

    private fun openMenu(adapterPos: Int) {
        val bottomMessageMenuFragment = BottomMenuFragment.newInstance(
            R.menu.message_bottom_context_menu,
            chatHistoryAdapter.dataList[adapterPos],
        )
        bottomMessageMenuFragment.show(childFragmentManager, BottomMenuFragment.FRAGMENT_ID)
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
        binding.cancelEdit.isVisible = state.editMessage != null
    }

    override fun processEffect(effect: ChatEffect) {
        when (effect) {
            is ChatEffect.ScrollToPosition -> scrollToMessage(
                effect.massageId,
                effect.isSmoothScroll
            )
            is ChatEffect.ShowError -> showError(effect.error)
            is ChatEffect.ShowEmojiSelector -> selectNewReaction(effect.messageId)
            is ChatEffect.SetEditMessageParams -> setEditMessage(effect.messageItem)
            is ChatEffect.SetMessageEditField -> binding.messageEnterEdit.setText(effect.text)
            is ChatEffect.ShowTopicSelector -> showTopicSelector(effect.messageItem)
            is ChatEffect.CopyToClipBoard -> copyToClipBoard(effect.text)
        }
    }

    private fun showError(errorMsg: String) {
        binding.messagesLoadingProgress.isVisible = false
        Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
    }

    private fun showTopicSelector(messageItem: MessageItem? = null) {
        val selectTopicFragment =
            SelectTopicFragment.newInstance(chatParams.value.channelId, messageItem)

        selectTopicFragment.show(childFragmentManager, SelectTopicFragment.FRAGMENT_ID)
    }

    private fun setEditMessage(messageItem: MessageItem) {
        binding.messageEnterEdit.setText(messageItem.text)
        binding.sendTopicName.text = messageItem.topicName
    }

    private fun scrollToMessage(messageId: Int, isSmoothScroll: Boolean) {
        val pos = chatHistoryAdapter.dataList.indexOfLast { it.id == messageId }
        if (pos != -1) {
            if (isSmoothScroll)
                binding.chatingHistory.smoothScrollToPosition(pos)
            else
                binding.chatingHistory.scrollToPosition(pos)
        } else
            error("Try scroll to not exist position message pos: $pos")
    }

    private fun copyToClipBoard(text: String) {
        val clip = ClipData.newPlainText("plain text", text)
        clipboard.value?.setPrimaryClip(clip)
    }

    companion object {
        private const val DEFAULT_TOPIC_NAME = "(no topic)"
        private const val ARGUMENT_TOPIC_NAME = "com.alexpaxom.ARGUMENT_TOPIC_NAME"
        private const val ARGUMENT_CHANNEL_NAME = "com.alexpaxom.ARGUMENT_CHANNEL_NAME"
        private const val ARGUMENT_CHANNEL_ID = "com.alexpaxom.ARGUMENT_CHANNEL_ID"
        private const val ARGUMENT_MY_USER_ID = "com.alexpaxom.ARGUMENT_MY_USER_ID"

        const val FRAGMENT_ID = "com.alexpaxom.CHAT_FRAGMENT_ID"

        @JvmStatic
        fun newInstance(
            topicName: String? = null,
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
            presenter.processEvent(
                ChatEvent.ChangedScrollPosition(
                    bottomPos,
                    topPos,
                    chatParams.value
                )
            )
        }
    }
}