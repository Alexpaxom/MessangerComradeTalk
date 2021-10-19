package com.alexpaxom.homework_2.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alexpaxom.homework_2.databinding.EmojiForSelectViewBinding

class EmojiSelectorAdapter: BaseAdapter<String>() {
    private var recyclerWidth: Int = 0

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerWidth = recyclerView.measuredWidth
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<String> {
        return EmojiHolder(
            EmojiForSelectViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    inner class EmojiHolder(val emojiForSelectViewBinding: EmojiForSelectViewBinding):
        BaseViewHolder<String>(emojiForSelectViewBinding) {

        override fun bind(model: String) {
            emojiForSelectViewBinding.emojiItem.text = model
        }

    }
}