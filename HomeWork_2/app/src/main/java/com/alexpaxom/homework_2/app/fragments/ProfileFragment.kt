package com.alexpaxom.homework_2.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.data.modelconverters.UserConverter
import com.alexpaxom.homework_2.data.models.UserItem
import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.UserProfileUseCaseZulipApiImpl
import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.UserStatusUseCaseZulipApiImpl
import com.alexpaxom.homework_2.databinding.FragmentProfileBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class ProfileFragment: DialogFragment(), ProfileStateMachine {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override var currentState: ProfileState = ProfileState.InitialState
    private var loadedUser: UserItem? = null
    private val compositeDisposable = CompositeDisposable()

    private val searchUsers = UserProfileUseCaseZulipApiImpl()
    private val userStatusInfo = UserStatusUseCaseZulipApiImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
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

        arguments?.let { bundle ->
            loadUserData(bundle.getInt(ARGUMENT_USER_ID))
        }

        return binding.root
    }

    private fun loadUserData(userId: Int) {
        searchUsers.getUserByID(userId)
            .zipWith(userStatusInfo.getStatusForUser(userId)) { user, status ->
                user.copy(status = status.aggregatedStatus)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { goToState(ProfileState.LoadingState) }
            .subscribeBy(
                onSuccess = {
                    goToState(ProfileState.ResultState(it))
                    loadedUser = it
                },
                onError = { goToState(ProfileState.ErrorState(it)) }
            )
            .addTo(compositeDisposable)
    }

    override fun toResult(resultState: ProfileState.ResultState) {
        binding.profileLoadProgress.isVisible = false
        binding.userName.text = resultState.user.name
        val userStatus = resultState.user.status
        binding.onlineStatus.text = when(userStatus) {
            UserConverter.OriginalZulipStatus.ONLINE_STATUS -> resources.getString(R.string.profile_user_online_status)
            UserConverter.OriginalZulipStatus.IDLE_STATUS -> resources.getString(R.string.profile_user_idle_status)
            UserConverter.OriginalZulipStatus.OFFLINE_STATUS -> resources.getString(R.string.profile_user_offline_status)
            else -> ""
        }

        val rColor = when(userStatus) {
            UserConverter.OriginalZulipStatus.ONLINE_STATUS -> R.color.profile_online_status_color
            UserConverter.OriginalZulipStatus.IDLE_STATUS -> R.color.profile_idle_status_color
            UserConverter.OriginalZulipStatus.OFFLINE_STATUS -> R.color.profile_offline_status_color
            else -> R.color.white
        }

        binding.onlineStatus.setTextColor(ResourcesCompat.getColor(resources, rColor, activity?.theme))

        val glide = Glide.with(requireActivity())

        glide.load(resultState.user.avatarUrl)
            .transform(CenterCrop(),
                RoundedCorners(this.resources.getDimensionPixelOffset(R.dimen.profile_avatar_rounded_corners)))
            .into(binding.userAvatar)
    }


    override fun toLoading(loadingState: ProfileState.LoadingState) {
        binding.profileLoadProgress.isVisible = true
    }

    override fun toError(errorState: ProfileState.ErrorState) {
        binding.profileLoadProgress.isVisible = false

        var error = errorState.error.localizedMessage

        if(errorState.error is HttpException) {
            error = errorState.error.response()?.errorBody()?.string()
        }

        Toast.makeText( context, error, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    companion object {
        private const val SAVED_USER_BUNDLE_ID = "com.alexpaxom.SAVED_USER_BUNDLE_ID"
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