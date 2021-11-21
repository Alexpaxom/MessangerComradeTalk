package com.alexpaxom.homework_2.domain.repositories.zulipapirepositories

import com.alexpaxom.homework_2.data.modelconverters.ChannelConverter
import com.alexpaxom.homework_2.data.models.ChannelItem
import com.alexpaxom.homework_2.domain.remote.ChannelsZulipApiRequests
import io.reactivex.Single

class ChannelsZulipDataRepository {

    private val retrofit = GetRetrofitObject.retrofit
    private val channelsZulipApiRequests = retrofit.create(ChannelsZulipApiRequests::class.java)
    private val channelConverter = ChannelConverter()

    fun getSubscribedChannels(): Single<List<ChannelItem>> {
        return channelsZulipApiRequests.getSubscribedStreams()
            .map { channelsWrap ->
                channelsWrap.subscriptions.map {
                    channelConverter.convert(it)
                }
            }
    }

    fun getAllChannels(): Single<List<ChannelItem>> {
        return channelsZulipApiRequests.getAllStreams()
            .map { channelsWrap ->
                channelsWrap.streams.map {
                    channelConverter.convert(it)
                }
            }
    }
}