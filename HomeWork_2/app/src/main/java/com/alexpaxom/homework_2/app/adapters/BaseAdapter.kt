package com.alexpaxom.homework_2.app.adapters

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<P>: RecyclerView.Adapter<BaseViewHolder<P>>() {
    var dataList: MutableList<P> = mutableListOf()
    private var mCallback: BaseAdapterCallback<P>? = null

    var hasItems = false

    fun attachCallback(callback: BaseAdapterCallback<P>) {
        this.mCallback = callback
    }

    fun detachCallback() {
        this.mCallback = null
    }

    fun addItem(newItem: P) {
        dataList.add(newItem)
        notifyItemInserted(dataList.size - 1)
    }

    fun updateItems(itemsList: List<P>) {
        dataList.clear()
        dataList.addAll(itemsList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: BaseViewHolder<P>, position: Int) {
        holder.bind(dataList[position])

        holder.itemView.setOnClickListener {
            mCallback?.onItemClick(dataList[position], holder.binding)
        }
        holder.itemView.setOnLongClickListener {
            if (mCallback == null) {
                false
            } else {
                mCallback!!.onLongClick(dataList[position], holder.binding)
            }

        }
    }

    override fun getItemCount(): Int {
        return dataList.count()
    }
}

