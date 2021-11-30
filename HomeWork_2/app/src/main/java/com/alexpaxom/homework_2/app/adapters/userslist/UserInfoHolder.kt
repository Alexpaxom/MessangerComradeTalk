package com.alexpaxom.homework_2.app.adapters.userslist

import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseViewHolder
import com.alexpaxom.homework_2.data.models.UserItem
import com.alexpaxom.homework_2.databinding.UserInfoItemBinding
import com.bumptech.glide.Glide

class UserInfoHolder(val userInfoItemBinding: UserInfoItemBinding): BaseViewHolder<UserItem>(userInfoItemBinding) {
   private val glide = Glide
           .with(userInfoItemBinding.userItemAvatarImg.context)

    override fun bind(model: UserItem) {
        userInfoItemBinding.userItemName.text = model.name
        userInfoItemBinding.userItemEmail.text = model.email
        glide.load(model.avatarUrl)
            .circleCrop()
            .into(userInfoItemBinding.userItemAvatarImg)
    }
}