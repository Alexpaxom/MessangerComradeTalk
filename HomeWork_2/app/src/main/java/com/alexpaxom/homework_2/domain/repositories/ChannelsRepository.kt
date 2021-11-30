package com.alexpaxom.homework_2.domain.repositories

import com.alexpaxom.homework_2.data.models.ChannelItem
import io.reactivex.Single

interface ChannelsRepository {
    fun getSubscribedChannels(): Single<List<ChannelItem>>
    fun getAllChannels(): Single<List<ChannelItem>>
}