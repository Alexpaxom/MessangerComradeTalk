package com.alexpaxom.homework_2.app.fragments

import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.data.models.MessageItem
import com.alexpaxom.homework_2.data.models.ReactionItem
import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.ChatUseCase
import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.EditMessagesUseCaseZulip
import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.MessagesLoadUseCaseZulip
import com.alexpaxom.homework_2.di.screen.ScreenScope
import com.alexpaxom.homework_2.domain.cache.helpers.CachedWrapper
import com.alexpaxom.homework_2.domain.repositories.zulipapirepositories.NarrowParams
import com.alexpaxom.homework_2.helpers.EmojiHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter
import retrofit2.HttpException
import java.util.*
import javax.inject.Inject

@ScreenScope
@InjectViewState
class ChatPresenter @Inject constructor(
    private val messagesLoader: MessagesLoadUseCaseZulip,
    private val messagesSender: EditMessagesUseCaseZulip,
) : MvpPresenter<BaseView<ChatViewState, ChatEffect>>() {

    var currentViewState: ChatViewState = ChatViewState()
        private set(value) {
            field = value
            viewState.processState(value)
        }

    private val afterInsertEffectsList: ArrayList<ChatEffect> = arrayListOf()

    private val compositeDisposable = CompositeDisposable()

    private val chatHandler: ChatUseCase = ChatUseCase()

    private val emojiHelper = EmojiHelper()

    private var hasOldMessages = true

    fun processEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.LoadHistory ->
                if (!currentViewState.isEmptyLoading)
                    loadChatHistory(event.chatParams)

            is ChatEvent.ChangedScrollPosition ->
                if (!currentViewState.isEmptyLoading)
                    checkLoadNewPage(event.bottomPos, event.topPos, event.chatParams)

            is ChatEvent.SendMessage ->
                if (event.messageItem.id != 0)
                    editMessage(event.messageItem, event.chatParams)
                else
                    sendMessage(event.messageItem, event.chatParams)

            is ChatEvent.EmojiStateChange -> {
                if (event.isAdd)
                    addReaction(event.emojiUnicode, event.messageId, event.chatParams)
                else
                    removeReaction(event.emojiUnicode, event.messageId, event.chatParams)
            }

            is ChatEvent.MessageContextMenuSelect -> processMessageContextMenuSelect(
                event.menuItemId, event.messageItem
            )

            ChatEvent.MessagesInserted -> afterInsertEffectsList.forEach {
                viewState.processEffect(afterInsertEffectsList.removeAt(0))
            }
            is ChatEvent.EditMessage -> processEditMessage(event)
        }
    }

    private fun processEditMessage(event: ChatEvent.EditMessage) {

        if (event.cancel) {
            viewState.processEffect(
                ChatEffect.SetMessageEditField("")
            )
            currentViewState = currentViewState.copy(editMessage = null)

            return
        }

        if (event.messageItem != null) {

            currentViewState = currentViewState.copy(editMessage = event.messageItem)

            viewState.processEffect(
                ChatEffect.SetEditMessageParams(event.messageItem)
            )

            return
        }

        error("Bad params Edit Message event")
    }

    private fun loadChatHistory(
        chatParams: ChatParams
    ) {

        var isScrolled = false

        messagesLoader.getHistory(
            messageId = NEWEST_MESSAGE,
            countMessages = DEFAULT_COUNT_LOAD_MESSAGES,
            filter = createFilterForMessages(chatParams),
            ownUserId = chatParams.myUserId
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread(), true)
            .doOnSubscribe {
                currentViewState = ChatViewState(
                    messages = listOf(),
                    isEmptyLoading = true
                )
            }
            .subscribeBy(
                onNext = { messagesWrap ->
                    if (!isScrolled && messagesWrap.data.isNotEmpty()) {
                        isScrolled = true
                        afterInsertEffectsList.add(
                            ChatEffect.ScrollToPosition(messagesWrap.data.last().id)
                        )
                    }

                    currentViewState = ChatViewState(
                        messages = chatHandler.addMessagesToPosition(
                            0,
                            messagesWrap.data,
                            listOf()
                        ),
                        isEmptyLoading = messagesWrap is CachedWrapper.CachedData
                    )
                },
                onError = { processError(it) },
            )
            .addTo(compositeDisposable)
    }

    private fun loadPreviousPageMessages(
        lastMessageId: Int,
        chatParams: ChatParams
    ) {
        messagesLoader.getPrevPage(
            messageId = lastMessageId.toLong(),
            countMessages = DEFAULT_COUNT_LOAD_MESSAGES_PER_PAGE,
            filter = createFilterForMessages(chatParams),
            ownUserId = chatParams.myUserId
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                currentViewState = currentViewState.copy(
                    isEmptyLoading = true
                )
            }
            .subscribeBy(
                onNext = { messagesWrap ->
                    messagesWrap.data.let {
                        if (it.size == 1 && it.first().id == lastMessageId)
                            hasOldMessages = false

                        currentViewState = currentViewState.copy(
                            messages = chatHandler.addMessagesToPosition(
                                position = 0,
                                newMessages = it.subList(0, it.size - 1),
                                messages = currentViewState.messages
                            ),
                            isEmptyLoading = false
                        )
                    }
                },
                onError = { processError(it) }
            )
            .addTo(compositeDisposable)
    }

    private fun loadNextPageMessages(
        lastMessageId: Int,
        chatParams: ChatParams,
        scrollToEnd: Boolean = false
    ) {
        messagesLoader.getNextPage(
            messageId = lastMessageId.toLong(),
            countMessages = MAX_COUNT_LOAD_MESSAGES,
            filter = createFilterForMessages(chatParams),
            ownUserId = chatParams.myUserId
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                currentViewState = currentViewState.copy(
                    isEmptyLoading = true
                )
            }
            .subscribeBy(

                onNext = { messagesWrap ->
                    if (scrollToEnd && messagesWrap.data.size > 0) {
                        afterInsertEffectsList.add(
                            ChatEffect.ScrollToPosition(
                                messagesWrap.data.last().id,
                                true
                            )
                        )
                    }

                    currentViewState = currentViewState.copy(
                        messages = chatHandler.addMessagesToPosition(
                            position = currentViewState.messages.size,
                            newMessages = messagesWrap.data.subList(1, messagesWrap.data.size),
                            messages = currentViewState.messages
                        ),
                        isEmptyLoading = false
                    )
                },
                onError = { processError(it) }
            )
            .addTo(compositeDisposable)
    }

    private fun createFilterForMessages(chatParams: ChatParams): NarrowParams =
        NarrowParams(chatParams.channelId, chatParams.topicName)


    private fun editMessage(messageItem: MessageItem, chatParams: ChatParams) {
        messagesSender.editMessage(
            messageId = messageItem.id,
            topic = messageItem.topicName,
            message = messageItem.text
        )
            .subscribeOn(Schedulers.io())
            .toObservable()
            .concatMap {
                messagesLoader.getMessage(
                    ownUserId = chatParams.myUserId,
                    messageId = messageItem.id.toLong(),
                    createFilterForMessages(chatParams)
                )
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                currentViewState = currentViewState.copy(
                    isEmptyLoading = true,
                    editMessage = null
                )
            }
            .subscribeBy (
                onNext = { editMessage ->

                    // если сообщение было перенесено в другой топик его нужно удалить
                    val newMessaged = if(editMessage.isNotEmpty()) {
                            afterInsertEffectsList.add(
                                ChatEffect.ScrollToPosition(messageItem.id)
                            )

                            chatHandler.updateMessageById(
                                messageId = messageItem.id,
                                messageItem,
                                currentViewState.messages
                            )
                        }
                    else
                        chatHandler.deleteMessageById(
                            messageId = messageItem.id,
                            currentViewState.messages
                        )


                    currentViewState = currentViewState.copy(
                        messages = newMessaged,
                        isEmptyLoading = false,
                    )
                },

                onError = { processError(it) }
            )
            .addTo(compositeDisposable)
    }

    private fun sendMessage(messageItem: MessageItem, chatParams: ChatParams) {
        messagesSender.sendMessageToStream(
            chatParams.channelId,
            messageItem.topicName,
            messageItem.text
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                currentViewState = currentViewState.copy(
                    isEmptyLoading = true
                )
            }
            .subscribeBy(
                onSuccess = {
                    // фикс ошибки если мы отправляем сообщение в пустой топик
                    if(currentViewState.messages.isEmpty()) {
                        loadChatHistory(chatParams)
                        return@subscribeBy
                    }

                    loadNextPageMessages(
                        lastMessageId = currentViewState.messages.last().id,
                        chatParams,
                        true
                    )
                },
                onError = { processError(it) }
            )
            .addTo(compositeDisposable)
    }

    private fun removeMessage(messageId: Int) {
        messagesSender.removeMessage(
            messageId
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                currentViewState = currentViewState.copy(
                    isEmptyLoading = true
                )
            }
            .subscribeBy(
                onSuccess = {
                    currentViewState = currentViewState.copy(
                        messages = chatHandler.deleteMessageById(messageId, currentViewState.messages),
                        isEmptyLoading = false
                    )
                },
                onError = { processError(it) }
            )
            .addTo(compositeDisposable)
    }

    private fun processMessageContextMenuSelect(menuItemId: Int, message: MessageItem) {
        when (menuItemId) {
            R.id.set_message_reaction_item -> viewState.processEffect(
                ChatEffect.ShowEmojiSelector(message.id)
            )

            R.id.copy_massage_text_item -> viewState.processEffect(
                ChatEffect.CopyToClipBoard(message.text)
            )

            R.id.delete_message_item -> removeMessage(message.id)

            R.id.edit_message_item -> {
                processEditMessage(ChatEvent.EditMessage(messageItem = message))
            }

            R.id.change_message_topic_item -> viewState.processEffect(
                ChatEffect.ShowTopicSelector(message)
            )

            else -> viewState.processEffect(
                ChatEffect.ShowError("Not exist handler for this message context menu item")
            )
        }
    }

    private fun removeReaction(emojiUnicode: String, messageId: Int, chatParams: ChatParams) {
        val reaction = ReactionItem(
            typeId = R.layout.emoji_for_select_view,
            userId = chatParams.myUserId,
            emojiUnicode = emojiUnicode,
            emojiName = emojiHelper.getNameByUnicode(emojiUnicode)
        )

        messagesSender.removeReaction(messageId, reaction.emojiName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    currentViewState = currentViewState.copy(
                        messages = chatHandler.removeReactionByMessageID(
                            messageId,
                            reaction,
                            currentViewState.messages
                        )
                    )
                },
                onError = { processError(it) }
            )
            .addTo(compositeDisposable)
    }

    private fun addReaction(emojiUnicode: String, messageId: Int, chatParams: ChatParams) {
        val reaction = ReactionItem(
            typeId = R.layout.emoji_for_select_view,
            userId = chatParams.myUserId,
            emojiUnicode = emojiUnicode,
            emojiName = emojiHelper.getNameByUnicode(emojiUnicode)
        )

        messagesSender.addReaction(messageId, reaction.emojiName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    currentViewState = currentViewState.copy(
                        messages = chatHandler.addReactionByMessageID(
                            messageId,
                            reaction,
                            currentViewState.messages
                        )
                    )

                    if (currentViewState.messages.last().id == messageId)
                        afterInsertEffectsList.add(ChatEffect.ScrollToPosition(messageId, true))

                },
                onError = { processError(it) }
            )
            .addTo(compositeDisposable)
    }

    private fun checkLoadNewPage(
        bottomPos: Int,
        topPos: Int,
        chatParams: ChatParams
    ) {
        // фикс ошибки если удалены все сообщения из топика
        if(currentViewState.messages.isEmpty())
            return

        if (topPos < LOADING_THRESHOLD_START && hasOldMessages)
            loadPreviousPageMessages(currentViewState.messages.first().id, chatParams)
        else if (bottomPos > currentViewState.messages.size - LOADING_THRESHOLD_START)
            loadNextPageMessages(currentViewState.messages.last().id, chatParams)
    }

    private fun processError(error: Throwable) {
        val errorMsg = if (error is HttpException)
            error.response()?.errorBody()?.string() ?: "Load messages http Error!"
        else
            error.localizedMessage ?: "Load messages error!"

        viewState.processEffect(
            ChatEffect.ShowError(errorMsg)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    companion object {
        private const val DEFAULT_COUNT_LOAD_MESSAGES = 50
        private const val DEFAULT_COUNT_LOAD_MESSAGES_PER_PAGE = 20
        private const val MAX_COUNT_LOAD_MESSAGES = 1000

        // get from documentation api
        const val NEWEST_MESSAGE = 10000000000000000L
        const val OLDEST_MESSAGE = 0L

        const val LOADING_THRESHOLD_START = 5
    }
}