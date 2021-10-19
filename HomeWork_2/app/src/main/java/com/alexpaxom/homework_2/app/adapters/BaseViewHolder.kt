package com.alexpaxom.homework_2.app.adapters

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<T>(val binding: ViewBinding): RecyclerView.ViewHolder(binding.root) {
    abstract fun bind(model: T)
}