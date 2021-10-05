package com.alexpaxom.homework_1.app.adapters

import BaseAdapterCallback
import BaseViewHolder
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

abstract class BaseAdapter<P>: RecyclerView.Adapter<BaseViewHolder<P>>() {
    var mDataList: MutableList<P> = ArrayList()
        private set
    private var mCallback: BaseAdapterCallback<P>? = null

    var hasItems = false

    fun attachCallback(callback: BaseAdapterCallback<P>) {
        this.mCallback = callback
    }

    fun detachCallback() {
        this.mCallback = null
    }

    fun setList(dataList: List<P>) {
        mDataList.addAll(dataList)
        hasItems = true
        notifyDataSetChanged()
    }


    fun addItem(newItem: P) {
        mDataList.add(newItem)
        notifyItemInserted(mDataList.size - 1)
    }

    fun addItemToTop(newItem: P) {
        mDataList.add(0, newItem)
        notifyItemInserted(0)
    }

    fun updateItems(itemsList: List<P>) {
        mDataList.clear()
        setList(itemsList)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<P>, position: Int) {
        holder.bind(mDataList[position])

        holder.itemView.setOnClickListener {
            mCallback?.onItemClick(mDataList[position], holder.binding)
        }
    }

    override fun getItemCount(): Int {
        return mDataList.count()
    }
}

