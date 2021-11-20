package com.alexpaxom.homework_2.app.fragments

import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.data.models.ReactionItem
import com.alexpaxom.homework_2.data.usecases.MessageSendUseCase
import com.alexpaxom.homework_2.data.usecases.MessagesLoadUseCase
import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.ChatUseCase
import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.MessageSendUseCaseZulipApiImpl
import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.MessagesLoadUseCaseZulipApiImpl
import com.alexpaxom.homework_2.helpers.EmojiHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import moxy.MvpPresenter
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.HttpException
import java.util.*

class ChatPresenter(
    var topicName: String,
    var streamName: String,
    var streamId: Int,
    var myUserId: Int
) : MvpPresenter<BaseView<ChatViewState, ChatEffect>>() {

    private var currentViewState: ChatViewState = ChatViewState()
        set(value) {
            field = value
            viewState.processState(value)
        }

    private val afterInsertEffectsList: ArrayList<ChatEffect> = arrayListOf()

    private var messagesLoader: Lazy<MessagesLoadUseCase> = lazy { MessagesLoadUseCaseZulipApiImpl(myUserId) }
    private val messagesSender: MessageSendUseCase = MessageSendUseCaseZulipApiImpl()

    private val compositeDisposable = CompositeDisposable()

    private val chatHandler: ChatUseCase = ChatUseCase()

    private val emojiHelper = EmojiHelper()

    private var hasOldMessages = true

    fun processEvent(event: ChatEvent) {
        when(event) {
            ChatEvent.LoadHistory -> loadChatHistory()
            is ChatEvent.ChangedScrollPosition ->
                if(!currentViewState.isEmptyLoading)
                    checkLoadNewPage(event.bottomPos, event.topPos)
            is ChatEvent.SendMessage -> sendMessage(event.message)
            is ChatEvent.EmojiStateChange -> {
                if(event.isAdd)
                    addReaction(event.emojiUnicode, event.messageId)
                else
                    removeReaction(event.emojiUnicode, event.messageId)
            }
            ChatEvent.MessagesInserted -> afterInsertEffectsList.forEach {
                viewState.processEffect(afterInsertEffectsList.removeAt(0))
            }
        }
    }

    private fun loadChatHistory() {
        messagesLoader.value.getMessages(
            messageId = NEWEST_MESSAGE,
            numBefore = DEFAULT_COUNT_LOAD_MESSAGES,
            numAfter = 0,
            filter = createFilterForMessages()
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                currentViewState = ChatViewState(
                    messages = listOf(),
                    isEmptyLoading = true
                )
            }
            .subscribeBy(
                onSuccess = {
                    currentViewState = ChatViewState(
                        messages = chatHandler.addMessagesToPosition(0, it, currentViewState.messages)
                    )
                    if(it.isNotEmpty())
                        afterInsertEffectsList.add(ChatEffect.ScrollToPosition(it.last().id))
                },
                onError = { processError(it) }
            )
            .addTo(compositeDisposable)
    }

    private fun loadPreviousPageMessages(lastMessageId: Int) {
        messagesLoader.value.getMessages(
            messageId = lastMessageId.toLong(),
            numBefore = DEFAULT_COUNT_LOAD_MESSAGES_PER_PAGE,
            numAfter = 0,
            filter = createFilterForMessages()
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
                    if(it.size == 1 && it.first().id == lastMessageId)
                        hasOldMessages = false

                    currentViewState = currentViewState.copy(
                        messages = chatHandler.addMessagesToPosition(
                            position = 0,
                            newMessages = it.subList(0, it.size-1),
                            messages = currentViewState.messages
                        ),
                        isEmptyLoading = false
                    )
                },
                onError = { processError(it) }
            )
            .addTo(compositeDisposable)
    }

    private fun loadNextPageMessages(lastMessageId: Int) {
        messagesLoader.value.getMessages(
            messageId = lastMessageId.toLong(),
            numBefore = 0,
            numAfter = MAX_COUNT_LOAD_MESSAGES,
            filter = createFilterForMessages()
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
                        messages = chatHandler.addMessagesToPosition(
                            position = currentViewState.messages.size,
                            newMessages = it.subList(1, it.size),
                            messages = currentViewState.messages
                        ),
                        isEmptyLoading = false
                    )
                },
                onError = { processError(it) }
            )
            .addTo(compositeDisposable)
    }

    private fun createFilterForMessages(): String {
        val filter = JSONArray()

        filter.put(
            JSONObject().apply {
                put("operator", "stream")
                put("operand", streamId)
            }
        )

        if(topicName != "")
            filter.put(
                JSONObject().apply {
                    put("operator", "topic")
                    put("operand", "${topicName}")
                }
            )

        return filter.toString()
    }

    private fun sendMessage(message: String) {
        messagesSender.sendMessageToStream(streamId, topicName, message)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                currentViewState = currentViewState.copy(
                    isEmptyLoading = true
                )
            }
            .subscribeBy(
                onSuccess = {
                    afterInsertEffectsList.add(
                        ChatEffect.ScrollToPosition(it.id, true)
                    )

                    loadNextPageMessages(
                        lastMessageId = currentViewState.messages.last().id,
                    )
                },
                onError = { processError(it) }
            )
            .addTo(compositeDisposable)
    }

    private fun removeReaction(emojiUnicode: String, messageId: Int) {
        val reaction = ReactionItem(
            typeId = R.layout.emoji_for_select_view,
            userId = myUserId,
            emojiUnicode = emojiUnicode,
            emojiName = emojiHelper.getNameByUnicode(emojiUnicode)
        )

        messagesSender.removeReaction(messageId, reaction.emojiName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    currentViewState = currentViewState.copy(
                        messages = chatHandler.removeReactionByMessageID (
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

    private fun addReaction(emojiUnicode: String, messageId: Int) {
        val reaction = ReactionItem(
            typeId = R.layout.emoji_for_select_view,
            userId = myUserId,
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

                    if( currentViewState.messages.last().id == messageId)
                        afterInsertEffectsList.add(ChatEffect.ScrollToPosition(messageId, true))

                },
                onError = { processError(it) }
            )
            .addTo(compositeDisposable)
    }

    private fun checkLoadNewPage(bottomPos: Int, topPos: Int) {
        if(topPos < LOADING_THRESHOLD_START && hasOldMessages)
            loadPreviousPageMessages(currentViewState.messages.first().id)
        else if(bottomPos > currentViewState.messages.size-LOADING_THRESHOLD_START)
            loadNextPageMessages(currentViewState.messages.last().id)
    }

    private fun processError(error: Throwable) {
        val errorMsg = if(error is HttpException)
            error.response()?.errorBody()?.string() ?: "Http Error!"
        else
            error.localizedMessage ?: "Error!"

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