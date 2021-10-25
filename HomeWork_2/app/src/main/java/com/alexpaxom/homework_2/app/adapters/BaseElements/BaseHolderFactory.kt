package com.alexpaxom.homework_2.app.adapters.BaseElements

import android.view.ViewGroup
import com.alexpaxom.homework_2.data.models.ListItem

abstract class BaseHolderFactory : (ViewGroup, Int) -> BaseViewHolder<ListItem> {

    abstract fun createViewHolder(viewGroup: ViewGroup, viewType: Int): BaseViewHolder<*>

    final override fun invoke(viewGroup: ViewGroup, viewType: Int): BaseViewHolder<ListItem> {
        return createViewHolder(viewGroup, viewType) as BaseViewHolder<ListItem>
    }
}
