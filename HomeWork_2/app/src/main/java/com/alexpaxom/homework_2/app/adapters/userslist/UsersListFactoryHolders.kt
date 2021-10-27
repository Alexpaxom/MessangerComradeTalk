package com.alexpaxom.homework_2.app.adapters.userslist

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseHolderFactory
import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseViewHolder
import com.alexpaxom.homework_2.databinding.UserInfoItemBinding

class UsersListFactoryHolders: BaseHolderFactory() {
    override fun createViewHolder(viewGroup: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when(viewType) {
            R.layout.user_info_item -> UserInfoHolder(
                UserInfoItemBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
            else -> error("Bad type users list holder")
        }
    }
}