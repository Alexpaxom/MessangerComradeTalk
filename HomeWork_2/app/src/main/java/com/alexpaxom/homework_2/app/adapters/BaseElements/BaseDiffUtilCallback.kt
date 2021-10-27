package com.alexpaxom.homework_2.app.adapters.BaseElements

import androidx.recyclerview.widget.DiffUtil

open class BaseDiffUtilCallback<T>: DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = true
}