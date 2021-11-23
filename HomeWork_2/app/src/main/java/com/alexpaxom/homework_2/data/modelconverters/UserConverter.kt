package com.alexpaxom.homework_2.data.modelconverters

import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.data.models.UserItem
import com.alexpaxom.homework_2.domain.entity.User

class UserConverter {
    fun convert(user: User): UserItem {
        return UserItem (
            typeId = R.layout.user_info_item,
            id = user.userId ?: -1,
            name = user.fullName ?: "No name",
            email = user.email ?: "No email",
            avatarUrl = user.avatarUrl ?: "No avatar"
        )
    }

    class OriginalZulipStatus  {
        companion object {
            val ONLINE_STATUS = "active"
            val IDLE_STATUS = "idle"
            val OFFLINE_STATUS =  "offline"
        }
    }
}