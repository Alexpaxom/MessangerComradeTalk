package com.alexpaxom.homework_2.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.domain.repositories.TestRepositoryImpl
import com.alexpaxom.homework_2.databinding.FragmentProfileBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class ProfileFragment() : ViewBindingFragment<FragmentProfileBinding>() {
    override var _binding: Lazy<FragmentProfileBinding>? = lazy {
        FragmentProfileBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        arguments?.let { bundle ->
            val glide = Glide.with(this)
            val user = TestRepositoryImpl().getUserById(bundle.getInt(ARGUMENT_USER_ID))
            val ownerFlag = bundle.getBoolean(ARGUMENT_OWNER_PARAMETER)


            binding.onlineStatus.isVisible = user.online
            binding.userName.text = user.name
            binding.status.text = user.status
            binding.profileLogoutBtn.isVisible = ownerFlag

            glide.load(user.avatarUrl)
                .transform(CenterCrop(),
                    RoundedCorners(this.resources.getDimensionPixelOffset(R.dimen.profile_avatar_rounded_corners)))
                .into(binding.userAvatar)
        }

        return binding.root
    }

    companion object {
        private const val ARGUMENT_USER_ID = "com.alexpaxom.ARGUMENT_USER_ID"
        private const val ARGUMENT_OWNER_PARAMETER = "com.alexpaxom.ARGUMENT_OWNER_PARAMETER"
        const val FRAGMENT_ID = "com.alexpaxom.PROFILE_FRAGMENT_ID"

        @JvmStatic
        fun newInstance(userId: Int, ownerFlag: Boolean = false) =
            ProfileFragment().apply {

                val params = Bundle()
                params.putInt(ARGUMENT_USER_ID, userId)
                params.putBoolean(ARGUMENT_OWNER_PARAMETER, ownerFlag)

                arguments = params
            }
    }
}