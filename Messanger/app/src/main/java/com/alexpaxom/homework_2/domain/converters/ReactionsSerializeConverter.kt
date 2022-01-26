package com.alexpaxom.homework_2.domain.converters

import androidx.room.TypeConverter
import com.alexpaxom.homework_2.domain.entity.DomainReaction
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class ReactionsSerializeConverter {
    @TypeConverter
    fun domainReactionsFromList(reactions: List<DomainReaction>): String {
        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, DomainReaction::class.java)
        val adapter = moshi.adapter<List<DomainReaction>>(type)

        return adapter.toJson(reactions)
    }

    @TypeConverter
    fun domainReactionsFromString(serializedReactions:String): List<DomainReaction>  {
        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, DomainReaction::class.java)
        val adapter = moshi.adapter<List<DomainReaction>>(type)

        return adapter.fromJson(serializedReactions) ?: listOf()
    }
}