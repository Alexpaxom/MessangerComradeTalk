package com.alexpaxom.homework_2.data.modelconverters

import com.alexpaxom.homework_2.data.models.UserStatus
import com.alexpaxom.homework_2.domain.entity.UserPresence

class StatusConverter {
    fun convert(userPresence: UserPresence, userId: Int): UserStatus {
        return UserStatus(
            userId = userId,
            aggregatedStatus = userPresence.presence.aggregated.status
        )
    }
}