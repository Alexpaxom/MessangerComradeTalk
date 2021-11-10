package com.alexpaxom.homework_2.domain.repositories.testrepositories

import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.data.models.UserItem
import com.alexpaxom.homework_2.domain.entity.UserPresence
import com.alexpaxom.homework_2.domain.repositories.UsersRepository
import io.reactivex.Single

class UsersTestDataRepositoryImpl: UsersRepository {

    override fun getUsers(): Single<List<UserItem>> {
        val retUsersList = arrayListOf<UserItem>()
        for(user in NAMES) {
            val userParam = user.split(">")
            retUsersList.add(
                UserItem(
                    typeId = R.layout.user_info_item,
                    id = user.hashCode(),
                    name = userParam[0],
                    email = "test@test.com",
                    avatarUrl = userParam[1],
                    status = "On meeting"
                )
            )
        }

        return Single.just(retUsersList)
    }

    override fun getUserById(userId: Int?): Single<UserItem> {
        var retUser: UserItem? = null

        for(user in NAMES) {
            if(user.hashCode() == userId) {
                val userParam = user.split(">")
                retUser = UserItem(
                    typeId = R.layout.user_info_item,
                    id = user.hashCode(),
                    name = userParam[0],
                    email = "test@test.com",
                    avatarUrl = userParam[1],
                    status = "On meeting"
                )
            }
        }

        if(retUser != null)
            return Single.just(retUser)

        error("cant find user with id $userId")
    }

    override fun getUserPresence(userId: Int): Single<UserPresence> {
        TODO("Not yet implemented")
    }

    companion object {
        val NAMES = listOf(
            "Иванов Сергей>https://ichef.bbci.co.uk/news/640/cpsprodpb/14A82/production/_116301648_gettyimages-1071204136.jpg",
            "Дегтярев Иван>https://image.jimcdn.com/app/cms/image/transf/none/path/s3974dcc17bc4f3da/image/iddd68c3ee51ac5c9/version/1588925960/image.jpg",
            "Сидоврова Светлана>https://www.crushpixel.com/big-static10/preview4/muscular-builder-with-bricks-on-573251.jpg",
            "Горбушкина Мирослва>https://img5.goodfon.ru/wallpaper/nbig/5/fd/sobachka-vzgliad-fon-drug.jpg",
            "Екатерина II>https://stuki-druki.com/aforizms/Ekaterina-Velikaya-01.jpg",
            "Юрий Тинькофф>https://s0.rbk.ru/v6_top_pics/media/img/6/23/755132645878236.jpg",
            "Илон Маск>https://www.ixbt.com/img/n1/news/2021/5/1/4adf85be7536d688480f1f6485034c527e552662_large_large.jpg"
        )
    }
}