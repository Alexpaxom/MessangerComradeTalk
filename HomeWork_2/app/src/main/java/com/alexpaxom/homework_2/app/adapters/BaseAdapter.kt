package com.alexpaxom.homework_2.app.adapters

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<P>: RecyclerView.Adapter<BaseViewHolder<P>>() {
    var dataList: List<P> = listOf()

    var hasItems = false

    fun updateItems(itemsList: List<P>) {
        dataList = itemsList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: BaseViewHolder<P>, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.count()
    }
}

