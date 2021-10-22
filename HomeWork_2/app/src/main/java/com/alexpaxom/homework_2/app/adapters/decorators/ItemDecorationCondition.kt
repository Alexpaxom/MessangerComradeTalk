package com.alexpaxom.homework_2.app.adapters.decorators

interface ItemDecorationCondition<out T> {
    fun isDecorate(itemPosition: Int): Boolean
    fun getDecorateParam(itemPosition: Int): T
}