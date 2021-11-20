package com.alexpaxom.homework_2.helpers

import android.content.Context

interface ResourceAccess1 {
    fun getString(resId: Int): String
}

class ResourceAccessImpl(var context: Context): ResourceAccess1 {
    override fun getString(resId: Int): String {
        return context.getString(resId)
    }
}