package com.alexpaxom.homework_2.domain.repositories.zulipapirepositories

import org.json.JSONArray
import org.json.JSONObject

class ChannelParamsFormatter {
    companion object {
        fun format(name: String, description: String = ""): String {
            return JSONArray()
                .put(JSONObject().apply {
                    put("name", name)
                    put("description", description)
                }
            ).toString()
        }
    }
}