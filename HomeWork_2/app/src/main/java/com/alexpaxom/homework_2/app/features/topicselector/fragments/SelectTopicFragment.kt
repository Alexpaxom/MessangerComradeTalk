package com.alexpaxom.homework_2.app.features.topicselector.fragments

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.App
import com.alexpaxom.homework_2.app.features.topicselector.adapters.TopicsHolderFactory
import com.alexpaxom.homework_2.app.features.topicselector.adapters.TopicsListAdapter
import com.alexpaxom.homework_2.app.features.baseelements.BaseView
import com.alexpaxom.homework_2.data.models.TopicItem
import com.alexpaxom.homework_2.databinding.FragmentSelectTopicBinding
import moxy.MvpAppCompatDialogFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject
import javax.inject.Provider


class SelectTopicFragment : MvpAppCompatDialogFragment(),
    BaseView<SelectTopicState, SelectTopicEffect> {

    private var _binding: FragmentSelectTopicBinding? = null
    private val binding get() = _binding!!

    private val topicsHolderFactory = TopicsHolderFactory { returnResult(it) }
    private val topicsListAdapter = TopicsListAdapter(topicsHolderFactory)

    private val channelId = lazy {
        arguments?.getInt(ARGUMENT_CHANNEL_ID) ?: error("Need param channel id")
    }

    @Inject
    lateinit var daggerPresenter: Provider<SelectTopicPresenter>

    @InjectPresenter
    lateinit var presenter: SelectTopicPresenter

    @ProvidePresenter
    fun providePresenter(): SelectTopicPresenter = daggerPresenter.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        (requireActivity().application as App).appComponent
            .getScreenComponent()
            .create()
            .inject(this)

        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.Theme_DialogFragment)

        if(savedInstanceState == null)
            presenter.processEvent(SelectTopicEvent.SearchTopics(channelId.value, DEFAULT_SEARCH_STRING))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectTopicBinding.inflate(inflater)

        binding.topicsSelectList.layoutManager = LinearLayoutManager(context)
        binding.topicsSelectList.adapter = topicsListAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.searchEdit.doAfterTextChanged { searchString ->
            presenter.processEvent(SelectTopicEvent.SearchTopics(channelId.value, searchString.toString()))

            binding.newTopicName.text = searchString.toString()
            binding.addNewTopicLayout.isVisible = !searchString.isNullOrEmpty()

        }

        binding.addNewTopicLayout.setOnClickListener {
            val newTopicItem = TopicItem(
                id = 0,
                channelId = channelId.value,
                name = binding.searchEdit.text.toString()
            )

            returnResult(newTopicItem)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun processState(state: SelectTopicState) {
        binding.topicsLoadProgress.isVisible = state.isEmptyLoading
        topicsListAdapter.dataList = state.topics
    }

    override fun processEffect(effect: SelectTopicEffect) {
        when(effect) {
            is SelectTopicEffect.ShowError -> showError(effect.error)
        }
    }

    private fun returnResult(topicItem: TopicItem) {
        val result = Bundle().apply {
            putParcelable(RESULT_PARAM_MESSAGE_ITEM, arguments?.getParcelable(RESULT_PARAM_MESSAGE_ITEM))
            putParcelable(RESULT_PARAM_TOPIC_ITEM, topicItem)
        }

        parentFragmentManager.setFragmentResult(
            FRAGMENT_ID,
            result
        )

        dismiss()
    }

    private fun showError(errorMsg: String) {
        binding.topicsLoadProgress.isVisible = false
        Toast.makeText( context, errorMsg, Toast.LENGTH_LONG).show()
    }

    companion object {
        const val RESULT_PARAM_TOPIC_ITEM = "com.alexpaxom.RESULT_PARAM_TOPIC_ITEM"
        const val RESULT_PARAM_MESSAGE_ITEM = "com.alexpaxom.RESULT_PARAM_MESSAGE_ITEM"

        const val FRAGMENT_ID = "com.alexpaxom.SELECT_TOPIC_FRAGMENT_ID"

        private const val ARGUMENT_CHANNEL_ID = "com.alexpaxom.ARGUMENT_CHANNEL_ID"
        private const val DEFAULT_SEARCH_STRING = ""

        @JvmStatic
        fun newInstance(channelId: Int, messageItem: Parcelable? = null) = SelectTopicFragment().apply {
            arguments = Bundle().apply {
                putInt(ARGUMENT_CHANNEL_ID, channelId)
                putParcelable(RESULT_PARAM_MESSAGE_ITEM, messageItem)
            }
        }
    }
}
