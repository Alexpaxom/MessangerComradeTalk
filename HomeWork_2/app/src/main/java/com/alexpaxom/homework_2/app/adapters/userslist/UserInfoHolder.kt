package com.alexpaxom.homework_2.app.adapters.userslist

import android.graphics.Color
import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseViewHolder
import com.alexpaxom.homework_2.data.models.UserItem
import com.alexpaxom.homework_2.databinding.UserInfoItemBinding
import com.bumptech.glide.Glide
import androidx.core.graphics.drawable.DrawableCompat

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.alexpaxom.homework_2.R


class UserInfoHolder(val userInfoItemBinding: UserInfoItemBinding): BaseViewHolder<UserItem>(userInfoItemBinding) {
   private val glide = Glide
           .with(userInfoItemBinding.userItemAvatarImg.context)



    override fun bind(model: UserItem) {
        userInfoItemBinding.userItemName.text = model.name
        userInfoItemBinding.userItemEmail.text = model.email
        glide.load(model.avatarUrl)
            .circleCrop()
            .into(userInfoItemBinding.userItemAvatarImg)

        val rColor = when(model.status) {
            "active" -> R.color.profile_online_status_color
            "idle" -> R.color.profile_idle_status_color
            "offline" -> R.color.profile_offline_status_color
            else -> R.color.white
        }

        val wrappedDrawable = DrawableCompat.wrap(userInfoItemBinding.statusIcon.drawable)
        DrawableCompat.setTint(
            wrappedDrawable,
            ContextCompat.getColor(userInfoItemBinding.statusIcon.context, rColor)
        )
    }
}