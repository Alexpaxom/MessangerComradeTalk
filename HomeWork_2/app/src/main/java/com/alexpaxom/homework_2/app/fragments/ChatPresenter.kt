package com.alexpaxom.homework_2.app.fragments

import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.data.models.ReactionItem
import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.ChatUseCase
import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.MessageSendUseCaseZulip
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
    private val messagesSender: MessageSendUseCaseZulip,
) : MvpPresenter<BaseView<ChatViewState, ChatEffect>>() {

    private var currentViewState: ChatViewState = ChatViewState()
        set(value) {
            field = value
            viewState.processState(value)
        }

    private val afterInsertEffectsList: ArrayList<ChatEffect> = arrayListOf()

    private val compositeDisposable = CompositeDisposable()

    private val chatHandler: ChatUseCase = ChatUseCase()

    private val emojiHelper = EmojiHelper()

    private var hasOldMessages = true

    fun processEvent(event: ChatEvent) {
        when(event) {
            is ChatEvent.LoadHistory ->
                if(!currentViewState.isEmptyLoading)
                    loadChatHistory(event.chatParams)

            is ChatEvent.ChangedScrollPosition ->
                if(!currentViewState.isEmptyLoading)
                    checkLoadNewPage(event.bottomPos, event.topPos, event.chatParams)

            is ChatEvent.SendMessage -> sendMessage(event.message, event.chatParams)
            is ChatEvent.EmojiStateChange -> {
                if(event.isAdd)
                    addReaction(event.emojiUnicode, event.messageId, event.chatParams)
                else
                    removeReaction(event.emojiUnicode, event.messageId, event.chatParams)
            }
            ChatEvent.MessagesInserted -> afterInsertEffectsList.forEach {
                viewState.processEffect(afterInsertEffectsList.removeAt(0))
            }
        }
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
                    if(!isScrolled && messagesWrap.data.isNotEmpty()) {
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
                    messagesWrap.data.let{
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
                    if(scrollToEnd && messagesWrap.data.size > 0) {
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

    private fun createFilterForMessages(chatParams: ChatParams): NarrowParams
            = NarrowParams(chatParams.streamId, chatParams.topicName)


    private fun sendMessage(message: String, chatParams: ChatParams) {
        messagesSender.sendMessageToStream(
            chatParams.streamId,
            chatParams.topicName,
            message
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

                    if( currentViewState.messages.last().id == messageId)
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
        if(topPos < LOADING_THRESHOLD_START && hasOldMessages)
            loadPreviousPageMessages(currentViewState.messages.first().id, chatParams)
        else if(bottomPos > currentViewState.messages.size-LOADING_THRESHOLD_START)
            loadNextPageMessages(currentViewState.messages.last().id, chatParams)
    }

    private fun processError(error: Throwable) {
        val errorMsg = if(error is HttpException)
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