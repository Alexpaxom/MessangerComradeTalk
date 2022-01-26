package com.alexpaxom.homework_2.app.features.channels.fragments

import android.os.Bundle
import android.os.Parcelable
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
import com.alexpaxom.homework_2.app.features.channels.adapters.ChannelsListAdapter
import com.alexpaxom.homework_2.app.features.channels.adapters.ChannelsListHoldersFactory
import com.alexpaxom.homework_2.app.features.baseelements.BaseView
import com.alexpaxom.homework_2.app.features.baseelements.ViewBindingFragment
import com.alexpaxom.homework_2.app.features.chat.fragments.ChatEvent
import com.alexpaxom.homework_2.app.features.menubottom.fragments.BottomMenuFragment
import com.alexpaxom.homework_2.data.models.ChannelItem
import com.alexpaxom.homework_2.data.models.ListItem
import com.alexpaxom.homework_2.data.models.MessageItem
import com.alexpaxom.homework_2.data.models.TopicItem
import com.alexpaxom.homework_2.databinding.CnannelsListFragmentBinding

abstract class ChannelsListFragment : ViewBindingFragment<CnannelsListFragmentBinding>(),
    BaseView<ChannelsViewState, ChannelsListEffect> {

    private val channelsListHoldersFactory = ChannelsListHoldersFactory(
        onExpandableChannelItemClickListener = { onChannelClick(it) },
        onExpandableTopicItemClickListener = { onTopicClick(it) },
        onExpandChannelListener = { onExpandChannel(it) },
        onExpandableChannelLongClickListener = { onLongChannelClick(it) },
        onExpandableTopicLongClickListener = { onLongTopicClick(it) }
    )

    private val channelsListAdapter = ChannelsListAdapter(channelsListHoldersFactory)

    abstract var presenter: ChannelsListPresenter

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
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.channels_list_decoration_divider,
                activity?.theme
            ) ?: error("Not found R.drawable.channels_list_decoration_divider")
        )

        binding.channelsList.addItemDecoration(channelsDividerItemDecoration)

        childFragmentManager.setFragmentResultListener(
            BottomMenuFragment.FRAGMENT_ID,
            this
        ) { _, resultBundle ->
            val menuItemId = resultBundle.getInt(BottomMenuFragment.RESULT_PARAM_MENU_ITEM_ID)
            resultBundle.getParcelable<Parcelable>(BottomMenuFragment.RESULT_PARAM_CALLBACK_PARAMS)
                ?.let { contextItem ->
                    when (contextItem) {
                        is TopicItem -> presenter.processEvent(
                            ChannelsListEvent.TopicContextMenuSelect(
                                menuItemId,
                                contextItem
                            )
                        )
                        is ChannelItem -> presenter.processEvent(
                            ChannelsListEvent.ChannelContextMenuSelect(
                                menuItemId,
                                contextItem
                            )
                        )
                    }
                }
        }

        return binding.root
    }

    protected fun getChannelById(channelId: Int): ChannelItem {
        return channelsListAdapter
            .dataList.firstOrNull { it.channel.id == channelId }
            ?.channel ?: error("Not found channel with id $channelId")
    }

    fun searchChannels(searchString: String) {
        presenter.processEvent(ChannelsListEvent.SearchInChannelGroup(searchString))
    }

    private fun onExpandChannel(channel: ChannelItem) {
        presenter.processEvent(ChannelsListEvent.ExpandedStateChange(channel))
    }

    override fun processState(state: ChannelsViewState) {
        channelsListAdapter.dataList = state.channels
        binding.progressChannelsLoading.isVisible = state.isEmptyLoad
    }

    override fun processEffect(effect: ChannelsListEffect) {
        when (effect) {
            is ChannelsListEffect.ShowError -> Toast.makeText(
                context,
                effect.error,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun onLongTopicClick(topicItem: TopicItem) {
        val bottomMessageMenuFragment = BottomMenuFragment.newInstance(
            R.menu.topic_bottom_context_menu,
            topicItem,
        )
        bottomMessageMenuFragment.show(childFragmentManager, BottomMenuFragment.FRAGMENT_ID)
    }

    private fun onLongChannelClick(channelItem: ChannelItem) {
        val bottomMessageMenuFragment = BottomMenuFragment.newInstance(
            R.menu.channel_bottom_context_menu,
            channelItem,
        )
        bottomMessageMenuFragment.show(childFragmentManager, BottomMenuFragment.FRAGMENT_ID)
    }

    protected abstract fun onTopicClick(topicItem: TopicItem)
    protected abstract fun onChannelClick(channel: ChannelItem)

}