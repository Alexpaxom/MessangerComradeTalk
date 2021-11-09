package com.alexpaxom.homework_2.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.adapters.cannelslist.ChannelsListAdapter
import com.alexpaxom.homework_2.app.adapters.cannelslist.ChannelsListHoldersFactory
import com.alexpaxom.homework_2.data.models.ExpandedChanelGroup
import com.alexpaxom.homework_2.data.repositories.TestMessagesRepository
import com.alexpaxom.homework_2.databinding.CnannelsListFragmentBinding

class ChannelsListFragment : ViewBindingFragment<CnannelsListFragmentBinding>() {

    override var _binding: Lazy<CnannelsListFragmentBinding>? = lazy {
        CnannelsListFragmentBinding.inflate(layoutInflater)
    }

    private val channelsListHoldersFactory = ChannelsListHoldersFactory {

        val chatFragment = ChatFragment.newInstance()
        chatFragment.show(parentFragmentManager, ChatFragment.FRAGMENT_ID)
    }


    private val channelsListAdapter = ChannelsListAdapter(channelsListHoldersFactory)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Иннициализация адаптера и восстановление его состояния
        val expandableListChannels = if(savedInstanceState == null)
                TestMessagesRepository().getChannels(10, 100)
            else
                savedInstanceState.getParcelableArrayList<ExpandedChanelGroup>(SAVED_BUNDLE_CHANNELS)
                    ?.toList() ?: listOf()

        channelsListAdapter.dataList = expandableListChannels
    }

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

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(
            SAVED_BUNDLE_CHANNELS,
            arrayListOf<ExpandedChanelGroup>().apply { addAll(channelsListAdapter.dataList) }
        )

        super.onSaveInstanceState(outState)
    }


    companion object {
        private const val SAVED_BUNDLE_CHANNELS = "com.alexpaxom.SAVED_BUNDLE_CHANNELS"
        const val FRAGMENT_ID = "com.alexpaxom.CHANNELS_LIST_FRAGMENT_ID"

        @JvmStatic
        fun newInstance() = ChannelsListFragment()
    }
}