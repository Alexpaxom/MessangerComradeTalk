package com.alexpaxom.homework_2.app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.adapters.cannelslist.ChannelsListAdapter
import com.alexpaxom.homework_2.app.adapters.cannelslist.ChannelsListHoldersFactory
import com.alexpaxom.homework_2.data.models.ExpandedChanelGroup
import com.alexpaxom.homework_2.data.repositories.TestMessagesRepository
import com.alexpaxom.homework_2.databinding.FragmentChannelsBinding

class ChannelsFragment : Fragment() {

    private var _binding: FragmentChannelsBinding? = null
    private val binding get() = _binding!!
    private val concatAdapterConfig = ConcatAdapter.Config.Builder()
        .setIsolateViewTypes(false)
        .build()
    private val channelsListConcatAdapter = ConcatAdapter(concatAdapterConfig, arrayListOf<ChannelsListAdapter>())


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChannelsBinding.inflate(inflater, container, false)

        // Иннициализация адаптера и восстановление его состояния
        val channelsListHoldersFactory = ChannelsListHoldersFactory()

        val expandableListChannels = if(savedInstanceState == null)
                TestMessagesRepository().getChannels(10, 100)
            else
                savedInstanceState.getParcelableArrayList<ExpandedChanelGroup>(SAVED_BUNDLE_CHANNELS)
                    ?.toList() ?: listOf()

        for (expandableChannel in expandableListChannels) {
            val adapter = ChannelsListAdapter(channelsListHoldersFactory)
            adapter.addItem(expandableChannel)
            channelsListConcatAdapter.addAdapter(adapter)
        }


        binding.channelsList.layoutManager = LinearLayoutManager(context)
        binding.channelsList.adapter = channelsListConcatAdapter

        // Устанавливаем декоратор
        val channelsDividerItemDecoration = DividerItemDecoration(
            context,
            RecyclerView.VERTICAL
        )

        channelsDividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.channels_list_decoration_divider))

        binding.channelsList.addItemDecoration(channelsDividerItemDecoration)


        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val listForSave = arrayListOf<ExpandedChanelGroup>()
        for(adapter in channelsListConcatAdapter.adapters) {
            when(adapter) {
                is ChannelsListAdapter -> listForSave.add(adapter.dataList.first())
                else -> error("Wrong type adapter expected ChannelsListAdapter")
            }

        }

        outState.putParcelableArrayList(SAVED_BUNDLE_CHANNELS, listForSave)

        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val SAVED_BUNDLE_CHANNELS = "com.alexpaxom.SAVED_BUNDLE_CHANNELS"
        const val FRAGMENT_ID = "com.alexpaxom.CHANNELS_FRAGMENT_ID"

        @JvmStatic
        fun newInstance() = ChannelsFragment()
    }
}