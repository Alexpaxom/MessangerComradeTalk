package com.alexpaxom.homework_2.app.adapters

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView

abstract class BaseDiffUtilAdapter<P>: RecyclerView.Adapter<BaseViewHolder<P>>() {

    val dataList: List<P>
        get() = diffUtil.currentList

    protected val diffUtil: AsyncListDiffer<P> = createDiffUtil()

    abstract fun createDiffUtil(): AsyncListDiffer<P>

    private var mCallback: BaseAdapterCallback<P>? = null

    open fun attachCallback(callback: BaseAdapterCallback<P>) {
        this.mCallback = callback
    }

    open fun detachCallback() {
        this.mCallback = null
    }

    open fun updateItem(pos: Int, newItem: P) {
        val list: MutableList<P> = mutableListOf()
        list.addAll(diffUtil.currentList)
        list[pos] = newItem

        diffUtil.submitList(list)
    }

    open fun addItem(newItem: P) {
        val list: MutableList<P> = mutableListOf()
        list.addAll(diffUtil.currentList)
        list.add(newItem)

        diffUtil.submitList(list)
    }

    open fun updateItems(itemsList: List<P>) {
        diffUtil.submitList(itemsList)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<P>, position: Int) {
        holder.bind(diffUtil.currentList[position])

        holder.itemView.setOnClickListener {
            mCallback?.onItemClick(diffUtil.currentList[position], holder.binding)
        }
        holder.itemView.setOnLongClickListener {
            if (mCallback == null) {
                false
            } else {
                mCallback!!.onLongClick(diffUtil.currentList[position], holder.binding)
            }

        }
    }

    override fun getItemCount(): Int {
        return diffUtil.currentList.count()
    }
}

