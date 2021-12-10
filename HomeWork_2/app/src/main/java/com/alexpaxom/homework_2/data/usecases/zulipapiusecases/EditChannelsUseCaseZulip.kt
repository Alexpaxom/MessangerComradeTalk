package com.alexpaxom.homework_2.data.usecases.zulipapiusecases

import com.alexpaxom.homework_2.di.screen.ScreenScope
import com.alexpaxom.homework_2.domain.entity.SubscribeResult
import com.alexpaxom.homework_2.domain.repositories.zulipapirepositories.ChannelsZulipDataRepository
import io.reactivex.Single
import javax.inject.Inject

@ScreenScope
class EditChannelsUseCaseZulip @Inject constructor(
    private val channelsZulipDataRepository: ChannelsZulipDataRepository
) {
    fun subscribeToChannel(channelName: String): Single<SubscribeResult> =
        channelsZulipDataRepository.subscribeToChannel(channelName)

    fun createChannel(channelName: String, channelDescription: String): Single<SubscribeResult> =
        channelsZulipDataRepository.createChannel(channelName, channelDescription)
}