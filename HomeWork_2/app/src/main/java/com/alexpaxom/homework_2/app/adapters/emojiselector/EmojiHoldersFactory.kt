package com.alexpaxom.homework_2.app.adapters.emojiselector

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseHolderFactory
import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseViewHolder
import com.alexpaxom.homework_2.databinding.EmojiForSelectViewBinding

class EmojiHoldersFactory(
    private val onClickListener: (Int) -> Unit
): BaseHolderFactory() {
    override fun createViewHolder(viewGroup: ViewGroup, viewType: Int): BaseViewHolder<*> {

        val holder = when (viewType) {
            R.layout.emoji_for_select_view -> EmojiHolder(
                EmojiForSelectViewBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
            else -> error("Bad type emoji holder")
        }

        holder.itemView.setOnClickListener {
            onClickListener(holder.adapterPosition)
        }

        return holder
    }
}