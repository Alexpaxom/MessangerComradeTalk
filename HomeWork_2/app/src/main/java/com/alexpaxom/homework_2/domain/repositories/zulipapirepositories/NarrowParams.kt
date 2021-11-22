package com.alexpaxom.homework_2.domain.repositories.zulipapirepositories

import org.json.JSONArray
import org.json.JSONObject

class NarrowParams(
    val streamId: Int,
    val topicName: String,
)
{
    fun createFilterForMessages(): String {
        val filter = JSONArray()

        filter.put(
            JSONObject().apply {
                put("operator", "stream")
                put("operand", streamId)
            }
        )

        if(topicName != "")
            filter.put(
                JSONObject().apply {
                    put("operator", "topic")
                    put("operand", "$topicName")
                }
            )

        return filter.toString()
    }
}