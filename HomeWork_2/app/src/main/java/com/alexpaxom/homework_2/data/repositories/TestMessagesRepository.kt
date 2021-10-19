package com.alexpaxom.homework_2.data.repositories

import com.alexpaxom.homework_2.data.models.Message
import com.alexpaxom.homework_2.data.models.Reaction
import java.sql.Time
import kotlin.random.Random

class TestMessagesRepository {

    private val names = listOf(
        "Иванов Сергей>https://ichef.bbci.co.uk/news/640/cpsprodpb/14A82/production/_116301648_gettyimages-1071204136.jpg",
        "Дегтярев Иван>https://image.jimcdn.com/app/cms/image/transf/none/path/s3974dcc17bc4f3da/image/iddd68c3ee51ac5c9/version/1588925960/image.jpg",
        "Сидоврова Светлана>https://www.crushpixel.com/big-static10/preview4/muscular-builder-with-bricks-on-573251.jpg",
        "Горбушкина Мирослва>https://img5.goodfon.ru/wallpaper/nbig/5/fd/sobachka-vzgliad-fon-drug.jpg",
        "Екатерина II>https://stuki-druki.com/aforizms/Ekaterina-Velikaya-01.jpg",
        "Юрий Тинькофф>https://s0.rbk.ru/v6_top_pics/media/img/6/23/755132645878236.jpg",
        "Илон Маск>https://www.ixbt.com/img/n1/news/2021/5/1/4adf85be7536d688480f1f6485034c527e552662_large_large.jpg"
    )

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
        "\uD83E\uDD23",
        "\uD83D\uDE07",
        "\uD83E\uDD72",
        "\uD83D\uDE1B",
        "\uD83E\uDD2A",
        "\uD83D\uDE11",
    )

    fun getMessages(count: Int): ArrayList<Message> {
        val ret = arrayListOf<Message>()


        repeat(count) {
            val user = names.random().split(">")



            ret.add (
                Message(
                    id = it,
                    userName = user[0],
                    avatarUrl = user[1],
                    text = messages.random(),
                    reactionList = reactionsList(100)
                )
            )
        }


        return ret

    }

    private fun reactionsList(maxCount: Int): ArrayList<Reaction>{
        val random = Random(100)

        val countReactions = random.nextInt(maxCount)

        val reactionList: ArrayList<Reaction> = arrayListOf()

        for (i in 0..countReactions) {

            reactionList.add(
                Reaction(
                    userId = i,
                    emojiUnicode = reactions.random()
                )
            )

        }

        return reactionList
    }

}