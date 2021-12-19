package com.alexpaxom.homework_2.app.fragments

import android.os.Bundle
import com.alexpaxom.homework_2.app.App
import com.alexpaxom.homework_2.data.models.ChannelItem
import com.alexpaxom.homework_2.data.models.TopicItem
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject
import javax.inject.Provider

class ChannelsListSubscribedFragment: ChannelsListFragment() {

    @Inject
    lateinit var daggerPresenter: Provider<ChannelsListSubscribedPresenter>

    @InjectPresenter
    override lateinit var presenter: ChannelsListPresenter

    @ProvidePresenter
    fun providePresenter(): ChannelsListPresenter = daggerPresenter.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        (requireActivity().application as App).appComponent
            .getScreenComponent()
            .create()
            .inject(this)

        super.onCreate(savedInstanceState)
    }

    override fun onChannelClick(channel: ChannelItem) {
        val chatFragment = ChatFragment.newInstance(
            topicName = null,
            channelName = channel.name,
            channelId = channel.id,
            myUserId = arguments?.getInt(PARAM_OWNER_USER_ID) ?: error("Required userId for chat!")

        )

        chatFragment.show(parentFragmentManager, ChatFragment.FRAGMENT_ID)
    }

    override fun onTopicClick(topicItem: TopicItem) {
        val topicChannel = getChannelById(topicItem.channelId)

        val chatFragment = ChatFragment.newInstance(
            topicName = topicItem.name,
            channelName = topicChannel.name,
            channelId = topicChannel.id,
            myUserId = arguments?.getInt(PARAM_OWNER_USER_ID) ?: error("Required userId for chat!")

        )

        chatFragment.show(parentFragmentManager, ChatFragment.FRAGMENT_ID)
    }

    companion object {
        private const val PARAM_OWNER_USER_ID = "com.alexpaxom.USER_ID_PARAM"
        const val FRAGMENT_ID = "com.alexpaxom.CHANNELS_LIST_SUBSCRIBED_FRAGMENT_ID"

        @JvmStatic
        fun newInstance(
            ownerUserId: Int
        ) = ChannelsListSubscribedFragment().apply {
            arguments = Bundle().apply {
                putInt(PARAM_OWNER_USER_ID, ownerUserId)
            }
        }
    }
}