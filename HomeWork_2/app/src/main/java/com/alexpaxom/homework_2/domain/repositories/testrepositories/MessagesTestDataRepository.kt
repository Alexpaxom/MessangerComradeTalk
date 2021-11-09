package com.alexpaxom.homework_2.domain.repositories.testrepositories

import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.data.models.MessageItem
import com.alexpaxom.homework_2.data.models.ReactionItem
import com.alexpaxom.homework_2.data.models.ReactionsGroup
import com.alexpaxom.homework_2.domain.entity.SendResult
import com.alexpaxom.homework_2.domain.repositories.MessagesRepository
import io.reactivex.Single
import java.util.*
import kotlin.random.Random

class MessagesTestDataRepository: MessagesRepository {

    var countMessages: Int = 30

    private val messages = listOf(
        "У нас в Краснодарском крае мясо фирмы Благояр очень популярно. Оно дешевле, чем курица других марок.",
        "Но, лично я уже не раз попадала с Благояром. Мясо иногда пахнет хлором. Иногда оно странное и водянистое.",
        "Иногда с душком, но, это скорее вопросы к розничным сетям.",
        "Я не сторонник готовых блюд, но иногда использую в приготовлении пищи некоторые приправы/заправки, которые ускоряют процесс или помогают создать необычные блюда.",
        "Сегодня хочу поделиться отзывом на Паста Том Ям Sen Soy / Сэн Сой Tom yum paste для приготовления одноименного супа.",
        "Всем привет.",
        "Люблю ароматный кофе из кофемашины.",
        "Илон Маск"
    )

    private val reactions = listOf(
        "\uD83D\uDE00",
        "\uD83D\uDE05",
        "\uD83E\uDD23",
        "\uD83D\uDE07",
        "\uD83D\uDE1B",
        "\uD83E\uDD2A",
        "\uD83D\uDE11",
    )

    override fun getMessages(
        messageId: Long,
        numBefore: Int,
        numAfter: Int,
        filter: String?
    ): Single<List<MessageItem>> {
        val ret = arrayListOf<MessageItem>()
        val date = Date()
        date.time -= 1000*60*60*12*countMessages

        repeat(countMessages) {
            val user = UsersTestDataRepositoryImpl.NAMES.random().split(">")
            ret.add (
                MessageItem(
                    typeId = R.layout.message_item,
                    id = it,
                    userName = user[0],
                    userId = user[0].hashCode(),
                    avatarUrl = user[1],
                    text = messages.random(),
                    datetime = Date(date.time),
                    reactionsGroup = ReactionsGroup(reactionsList(100), MY_USER_ID)
                )
            )
            date.time += 1000*60*60*6
        }

        return Single.just(ret)
    }

    override fun sendMessageToStream(
        streamId: Int,
        topic: String,
        message: String
    ): Single<SendResult> {
        TODO("Not yet implemented")
    }

    override fun sendMessageToUsers(usersIds: List<Int>, message: String): Single<SendResult> {
        TODO("Not yet implemented")
    }

    private fun reactionsList(maxCount: Int): ArrayList<ReactionItem>{
        val random = Random(100)

        val countReactions = random.nextInt(maxCount)

        val reactionList: ArrayList<ReactionItem> = arrayListOf()

        for (i in 0..countReactions) {

            reactionList.add(
                ReactionItem(
                    userId = i,
                    emojiUnicode = reactions.random()
                )
            )

        }

        return reactionList
    }

    companion object {
        private const val MY_USER_ID = 99999
    }
}