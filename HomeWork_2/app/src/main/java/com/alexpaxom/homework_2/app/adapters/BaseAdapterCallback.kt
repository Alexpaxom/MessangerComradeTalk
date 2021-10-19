package com.alexpaxom.homework_2.app.adapters

import androidx.viewbinding.ViewBinding

interface BaseAdapterCallback<T> {
    fun onItemClick(model: T, view: ViewBinding)
    fun onLongClick(model: T, view: ViewBinding): Boolean
}