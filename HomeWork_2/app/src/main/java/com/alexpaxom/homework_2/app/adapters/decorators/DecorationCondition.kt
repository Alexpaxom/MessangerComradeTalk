package com.alexpaxom.homework_2.app.adapters.decorators

interface DecorationCondition {
    fun isDecorate(itemPosition: Int): Boolean
}