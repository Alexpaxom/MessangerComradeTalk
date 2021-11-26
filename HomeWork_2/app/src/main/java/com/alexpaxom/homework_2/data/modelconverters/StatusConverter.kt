package com.alexpaxom.homework_2.data.modelconverters

import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.data.models.UserStatus
import com.alexpaxom.homework_2.domain.entity.UserPresence

class StatusConverter {
    fun convert(userPresence: UserPresence, userId: Int): UserStatus {
        return UserStatus(
            userId = userId,
            localAggregatedStatusId = mappingNewStatusName(userPresence.presence.aggregated.status),
            colorId = mappingColorByStatusName(userPresence.presence.aggregated.status)
        )
    }

    private fun mappingNewStatusName(originalStatus:String): Int {
        return when(originalStatus) {
            OriginalZulipStatus.ONLINE_STATUS -> R.string.profile_user_online_status
            OriginalZulipStatus.IDLE_STATUS -> R.string.profile_user_idle_status
            OriginalZulipStatus.OFFLINE_STATUS -> R.string.profile_user_offline_status
            else -> R.string.empty_string
        }
    }

    private fun mappingColorByStatusName(originalStatus:String): Int {
        return when(originalStatus) {
            OriginalZulipStatus.ONLINE_STATUS -> R.color.profile_online_status_color
            OriginalZulipStatus.IDLE_STATUS -> R.color.profile_idle_status_color
            OriginalZulipStatus.OFFLINE_STATUS -> R.color.profile_offline_status_color
            else -> R.color.white
        }
    }

    class OriginalZulipStatus  {
        companion object {
            val ONLINE_STATUS = "active"
            val IDLE_STATUS = "idle"
            val OFFLINE_STATUS =  "offline"
        }
    }
}