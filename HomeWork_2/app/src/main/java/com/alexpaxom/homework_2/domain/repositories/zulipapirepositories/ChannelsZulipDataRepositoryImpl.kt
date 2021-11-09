package com.alexpaxom.homework_2.domain.repositories.zulipapirepositories

import com.alexpaxom.homework_2.data.modelconverters.ChannelConverter
import com.alexpaxom.homework_2.data.models.ChannelItem
import com.alexpaxom.homework_2.domain.remote.ChannelsZulipApiRequests
import com.alexpaxom.homework_2.domain.repositories.ChannelsRepository
import io.reactivex.Single

class ChannelsZulipDataRepositoryImpl: ChannelsRepository {

    private val retrofit = GetRetrofitObject.retrofit
    private val channelsZulipApiRequests = retrofit.create(ChannelsZulipApiRequests::class.java)
    private val channelConverter = ChannelConverter()

    override fun getSubscribedChannels(): Single<List<ChannelItem>> {
        return channelsZulipApiRequests.getSubscribedStreams()
            .map { channelsWrap ->
                channelsWrap.subscriptions.map {
                    channelConverter.convert(it)
                }
            }
    }

    override fun getAllChannels(): Single<List<ChannelItem>> {
        return channelsZulipApiRequests.getAllStreams()
            .map { channelsWrap ->
                channelsWrap.subscriptions.map {
                    channelConverter.convert(it)
                }
            }
    }
}