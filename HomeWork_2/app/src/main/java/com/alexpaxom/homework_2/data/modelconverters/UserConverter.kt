package com.alexpaxom.homework_2.data.modelconverters

import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.data.models.UserItem
import com.alexpaxom.homework_2.domain.entity.User

class UserConverter {
    fun convert(user: User): UserItem {
        return UserItem (
            typeId = R.layout.user_info_item,
            id = user.userId,
            name = user.fullName ?: "",
            email = user.email ?: "",
            avatarUrl = user.avatarUrl ?: ""
        )
    }
}