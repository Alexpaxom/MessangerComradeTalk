package com.alexpaxom.homework_2.app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.data.models.User
import com.alexpaxom.homework_2.databinding.FragmentProfileBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)


        arguments?.let { bundle ->
            val glide = Glide.with(this)
            bundle.getParcelable<User>(ARGUMENT_USER_PARAMETER)?.let { user ->
                binding.userName.text = user.name
                glide.load(user.avatarUrl)
                    .transform(CenterCrop(),
                        RoundedCorners(this.resources.getDimensionPixelOffset(R.dimen.profile_avatar_rounded_corners)))
                    .into(binding.userAvatar)
                binding.status.text = user.status
                binding.onlineStatus.isVisible = user.online
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val ARGUMENT_USER_PARAMETER = "com.alexpaxom.ARGUMENT_USER_PARAMETER"
        private const val ARGUMENT_OWNER_PARAMETER = "com.alexpaxom.ARGUMENT_OWNER_PARAMETER"
        const val FRAGMENT_ID = "com.alexpaxom.PROFILE_FRAGMENT_ID"

        @JvmStatic
        fun newInstance(user: User, ownerFlag: Boolean = false) =
            ProfileFragment().apply {

                val params = Bundle()
                params.putParcelable(ARGUMENT_USER_PARAMETER, user)
                params.putBoolean(ARGUMENT_OWNER_PARAMETER, ownerFlag)

                arguments = params
            }
    }
}