package com.alexpaxom.homework_2.data.usecases.zulipapiusecases

import com.alexpaxom.homework_2.di.screen.ScreenScope
import com.alexpaxom.homework_2.domain.entity.TopicDeleteResult
import com.alexpaxom.homework_2.domain.repositories.zulipapirepositories.TopicsZulipDataRepository
import io.reactivex.Single
import javax.inject.Inject

@ScreenScope
class EditTopicsUseCaseZulip @Inject constructor(
    private val topicsRepository: TopicsZulipDataRepository
) {
    fun removeTopic(channelId: Int, topicName: String): Single<TopicDeleteResult> =
        topicsRepository.removeTopic(channelId, topicName)
}