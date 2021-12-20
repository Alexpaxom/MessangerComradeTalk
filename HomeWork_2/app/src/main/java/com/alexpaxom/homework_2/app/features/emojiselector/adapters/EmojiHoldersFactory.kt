package com.alexpaxom.homework_2.app.features.emojiselector.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.features.baseelements.adapters.BaseHolderFactory
import com.alexpaxom.homework_2.app.features.baseelements.adapters.BaseViewHolder
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