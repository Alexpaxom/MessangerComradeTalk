package com.alexpaxom.homework_2.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.activities.MainActivity
import com.alexpaxom.homework_2.app.adapters.cannelslist.ChannelsListAdapter
import com.alexpaxom.homework_2.app.adapters.cannelslist.ChannelsListHoldersFactory
import com.alexpaxom.homework_2.data.models.ChannelItem
import com.alexpaxom.homework_2.data.models.TopicItem
import com.alexpaxom.homework_2.databinding.CnannelsListFragmentBinding
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

class ChannelsListFragment : ViewBindingFragment<CnannelsListFragmentBinding>(),
    BaseView<ChannelsViewState, ChannelsListEffect> {


    private val channelsListHoldersFactory = ChannelsListHoldersFactory(
        onExpandableChannelItemClickListener = {onChannelClick(it)},
        onExpandableTopicItemClickListener = {onTopicClick(it)})


    private val channelsListAdapter = ChannelsListAdapter(channelsListHoldersFactory)

    @InjectPresenter
    lateinit var presenter: ChannelsListPresenter

    @ProvidePresenter
    fun provideDetailsPresenter(): ChannelsListPresenter? {
        return ChannelsListPresenter (
            subscribedFilterFlag = arguments?.getBoolean(ChannelsListFragment.SUBSCRIBED_FILTER_FLAG) ?: false
        )
    }

    override fun createBinding(): CnannelsListFragmentBinding =
        CnannelsListFragmentBinding.inflate(layoutInflater)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding.channelsList.layoutManager = LinearLayoutManager(context)
        binding.channelsList.adapter = channelsListAdapter

        // Устанавливаем декоратор
        val channelsDividerItemDecoration = DividerItemDecoration(
            context,
            RecyclerView.VERTICAL
        )

        channelsDividerItemDecoration.setDrawable(
            ResourcesCompat.getDrawable(resources, R.drawable.channels_list_decoration_divider, activity?.theme) ?:
            error("Not found R.drawable.channels_list_decoration_divider")
        )

        binding.channelsList.addItemDecoration(channelsDividerItemDecoration)

        return binding.root
    }

    fun searchChannels(searchString: String) {
        presenter.processEvent(ChannelsListEvent.SearchInChannelGroup(searchString))
    }

    private fun onChannelClick(channel: ChannelItem) {
        presenter.processEvent(ChannelsListEvent.ExpandedStateChange(channel))
    }

    private fun onTopicClick(topicItem: TopicItem) {

        val chatFragment = ChatFragment.newInstance(
            topicName = topicItem.name,
            streamName = channelsListAdapter
                .dataList.firstOrNull{it.channel.id == topicItem.channelId}
                ?.channel
                ?.name ?: "",
            streamId = topicItem.channelId,
            myUserId = (requireActivity() as MainActivity).ownUserId

        )

        chatFragment.show(parentFragmentManager, ChatFragment.FRAGMENT_ID)
    }

    override fun processState(state: ChannelsViewState) {
        channelsListAdapter.dataList = state.channels
        binding.progressChannelsLoading.isVisible = state.isEmptyLoad
    }

    override fun processEffect(effect: ChannelsListEffect) {
        when(effect) {
            is ChannelsListEffect.ShowError -> Toast.makeText(context, effect.error, Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private const val SUBSCRIBED_FILTER_FLAG = "com.alexpaxom.SUBSCRIBED_FILTER_FLAG"
        const val FRAGMENT_ID = "com.alexpaxom.CHANNELS_LIST_FRAGMENT_ID"
        const val INITIAL_SEARCH_QUERY: String = ""

        @JvmStatic
        fun newInstance(subscribedFilterFlag: Boolean) = ChannelsListFragment().apply {
            arguments = Bundle().apply {
                putBoolean(SUBSCRIBED_FILTER_FLAG, subscribedFilterFlag)
            }
        }
    }
}