package com.alexpaxom.homework_2.app.features.login.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import com.alexpaxom.homework_2.app.App
import com.alexpaxom.homework_2.app.features.baseelements.BaseView
import com.alexpaxom.homework_2.app.features.baseelements.ViewBindingFragment
import com.alexpaxom.homework_2.databinding.FragmentLoginBinding
import com.alexpaxom.homework_2.domain.entity.LoginResult
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject
import javax.inject.Provider

class LoginFragment : ViewBindingFragment<FragmentLoginBinding>(),
    BaseView<LoginState, LoginEffect> {

    override fun createBinding(): FragmentLoginBinding
    = FragmentLoginBinding.inflate(layoutInflater)

    @Inject
    lateinit var daggerPresenter: Provider<LoginPresenter>

    @InjectPresenter
    lateinit var presenter: LoginPresenter

    @ProvidePresenter
    fun providePresenter(): LoginPresenter = daggerPresenter.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        (requireActivity().application as App).appComponent
            .getScreenComponent()
            .create()
            .inject(this)

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.loginBtn.setOnClickListener {
            presenter.processEvent(LoginEvent.LogIn(
                chatUrl = binding.chatUrlEdit.text.toString(),
                login = binding.loginEdit.text.toString(),
                password = binding.passwordEdit.text.toString()
            ))
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun processState(state: LoginState) {
        binding.loginProgress.isVisible = state.isEmptyLoad
        binding.editFieldsGroup.isVisible = !state.isEmptyLoad
    }

    override fun processEffect(effect: LoginEffect) {
        when(effect) {
            is LoginEffect.ShowError -> showError(effect.error)
            is LoginEffect.Logined -> returnResult(effect.loginResult)
        }
    }

    private fun showError(errorMsg: String) {
        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
    }

    private fun returnResult(loginResult: LoginResult) {
        val result = Bundle().apply {
            putParcelable(RESULT_LOGIN_PARAM, loginResult)
        }

        parentFragmentManager.setFragmentResult(FRAGMENT_ID, result)
    }

    companion object {

        const val RESULT_LOGIN_PARAM = "com.alexpaxom.RESULT_LOGIN_PARAM"

        const val FRAGMENT_ID = "com.alexpaxom.LOGIN_FRAGMENT_ID"

        @JvmStatic
        fun newInstance() = LoginFragment()
    }


}