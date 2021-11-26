package com.alexpaxom.homework_2.app.adapters.userslist

import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseViewHolder
import com.alexpaxom.homework_2.data.models.UserItem
import com.alexpaxom.homework_2.databinding.UserInfoItemBinding
import com.bumptech.glide.Glide
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.content.ContextCompat
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.data.modelconverters.UserConverter


class UserInfoHolder(val userInfoItemBinding: UserInfoItemBinding): BaseViewHolder<UserItem>(userInfoItemBinding) {

    private val glide = Glide
           .with(userInfoItemBinding.userItemAvatarImg.context)
    val wrappedDrawable = DrawableCompat.wrap(userInfoItemBinding.statusIcon.drawable)


    override fun bind(model: UserItem) {
        userInfoItemBinding.userItemName.text = model.name
        userInfoItemBinding.userItemEmail.text = model.email
        glide.load(model.avatarUrl)
            .circleCrop()
            .into(userInfoItemBinding.userItemAvatarImg)


        DrawableCompat.setTint(
            wrappedDrawable,
            ContextCompat.getColor(userInfoItemBinding.statusIcon.context, model.status.colorId)
        )
    }
}