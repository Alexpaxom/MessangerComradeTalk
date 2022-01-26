package com.alexpaxom.homework_2.app.features.channels.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.App
import com.alexpaxom.homework_2.app.features.baseelements.BaseView
import com.alexpaxom.homework_2.databinding.FragmentChannelEditBinding
import moxy.MvpAppCompatDialogFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject
import javax.inject.Provider

class ChannelEditFragment : MvpAppCompatDialogFragment(),
    BaseView<ChannelEditState, ChannelEditEffect> {

    private var _binding: FragmentChannelEditBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var daggerPresenter: Provider<ChannelEditPresenter>

    @InjectPresenter
    lateinit var presenter: ChannelEditPresenter

    @ProvidePresenter
    fun providePresenter(): ChannelEditPresenter = daggerPresenter.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        (requireActivity().application as App).appComponent
            .getScreenComponent()
            .create()
            .inject(this)

        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.Theme_DialogFragment)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChannelEditBinding.inflate(inflater)

        binding.cancelEditChannelBtn.setOnClickListener {
            dismiss()
        }

        binding.confirmEditChannelBtn.setOnClickListener {
            presenter.processEvent(
                ChannelEditEvent.CreateOrUpdateChannel(
                    null,
                    binding.channelNameEdit.text.toString(),
                    binding.channelDescriptionEdit.text.toString()
                )
            )
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun processState(state: ChannelEditState) {
        if(state.isSuccess) {
            parentFragmentManager.setFragmentResult(FRAGMENT_ID, Bundle())
            dismiss()
        }
    }

    override fun processEffect(effect: ChannelEditEffect) {
        when(effect) {
            is ChannelEditEffect.ShowError ->
                Toast.makeText(context, effect.error, Toast.LENGTH_LONG).show()
        }
    }

    companion object {

        const val FRAGMENT_ID = "com.alexpaxom.CHANNEL_EDIT_FRAGMENT_ID"

        @JvmStatic
        fun newInstance() = ChannelEditFragment()
    }
}