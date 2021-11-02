package com.alexpaxom.homework_2.data.repositories

import com.alexpaxom.homework_2.data.models.ExpandedChanelGroup
import io.reactivex.Single

class UseCaseTestRepository {
    val repository = TestRepositoryImpl()

    fun searchChannels(searchString: String): Single<List<ExpandedChanelGroup>> {
        return repository.getChannels(10, 100)
            .map { val retValues = arrayListOf<ExpandedChanelGroup>()


                it.filter {
                    it.channel.name.contains(searchString, ignoreCase = true) ||
                            it.topics.any { it.name.contains(searchString, ignoreCase = true) }
                }
                    .forEach {
                        retValues.add(
                            ExpandedChanelGroup(
                                typeId = it.typeId,
                                channel = it.channel,
                                topics = it.topics.filter { it.name.contains(searchString, ignoreCase = true) },
                                isExpanded = it.isExpanded
                            )
                        )
                    }

                return@map retValues
            }
    }
}