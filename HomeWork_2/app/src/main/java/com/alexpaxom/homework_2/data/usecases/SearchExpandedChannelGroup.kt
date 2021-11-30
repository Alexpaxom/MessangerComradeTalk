package com.alexpaxom.homework_2.data.usecases

import com.alexpaxom.homework_2.data.models.ExpandedChanelGroup
import io.reactivex.Single

interface SearchExpandedChannelGroup {
    fun searchInSubscribedChannelGroups(searchString: String = ""): Single<List<ExpandedChanelGroup>>
    fun searchInAllChannelGroups(searchString: String = ""): Single<List<ExpandedChanelGroup>>
}