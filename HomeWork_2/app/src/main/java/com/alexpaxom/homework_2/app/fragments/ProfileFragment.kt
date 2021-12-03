package com.alexpaxom.homework_2.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.App
import com.alexpaxom.homework_2.databinding.FragmentProfileBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import moxy.MvpAppCompatDialogFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject
import javax.inject.Provider

class ProfileFragment: MvpAppCompatDialogFragment(), BaseView<ProfileViewState, ProfileEffect> {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var daggerPresenter: Provider<ProfilePresenter>

    @InjectPresenter
    lateinit var presenter: ProfilePresenter

    @ProvidePresenter
    fun providePresenter(): ProfilePresenter = daggerPresenter.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        (requireActivity().application as App).appComponent
            .getScreenComponent()
            .create()
            .inject(this)

        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.Theme_DialogFragment)
        arguments?.getBoolean(ARGUMENT_OWNER_PARAMETER)?.let { ownerFlag ->
                showsDialog = !ownerFlag
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

            if(savedInstanceState == null) {
                arguments?.let { bundle ->
                    presenter.processEvent(ProfileEvent.LoadUserInfo(bundle.getInt(ARGUMENT_USER_ID)))
                }
            }

        return binding.root
    }


    override fun processState(state: ProfileViewState) {

        state.user?.let { user ->
            binding.userName.text = user.name

            binding.onlineStatus.text = resources.getString(user.status.localAggregatedStatusId)

            binding.onlineStatus.setTextColor(
                ResourcesCompat.getColor(resources,
                    user.status.colorId,
                    activity?.theme)
            )

            val glide = Glide.with(requireActivity())

            glide.load(user.avatarUrl)
                .transform(CenterCrop(),
                    RoundedCorners(this.resources.getDimensionPixelOffset(R.dimen.profile_avatar_rounded_corners)))
                .into(binding.userAvatar)
        }

        binding.profileLoadProgress.isVisible = state.isEmptyLoading
    }

    override fun processEffect(effect: ProfileEffect) {

        when(effect) {
            is ProfileEffect.ShowError ->
                Toast.makeText( context, effect.error, Toast.LENGTH_LONG).show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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