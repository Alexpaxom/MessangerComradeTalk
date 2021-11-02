package com.alexpaxom.homework_2.app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.adapters.cannelslist.ChannelExpandAdapterWrapper
import com.alexpaxom.homework_2.app.adapters.cannelslist.ChannelsListHoldersFactory
import com.alexpaxom.homework_2.data.models.ExpandedChanelGroup
import com.alexpaxom.homework_2.data.repositories.UseCaseTestRepository
import com.alexpaxom.homework_2.databinding.CnannelsListFragmentBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class ChannelsListFragment : Fragment(), ChannelsStateMachine {

    private var _binding: CnannelsListFragmentBinding? = null
    private val binding get() = _binding!!
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override var currentState: ChannelsScreenState = InitialState

    val channelsListHoldersFactory = ChannelsListHoldersFactory { _ ->

        val chatFragment = ChatFragment.newInstance()
        chatFragment.show(parentFragmentManager, ChatFragment.FRAGMENT_ID)
    }

    private val adapterWrapper = ChannelExpandAdapterWrapper(channelsListHoldersFactory)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CnannelsListFragmentBinding.inflate(inflater, container, false)

        // Иннициализация адаптера

        binding.channelsList.layoutManager = LinearLayoutManager(context)
        binding.channelsList.adapter = adapterWrapper.innerAdapter

        // Устанавливаем декоратор
        val channelsDividerItemDecoration = DividerItemDecoration(
            context,
            RecyclerView.VERTICAL
        )

        channelsDividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.channels_list_decoration_divider))

        binding.channelsList.addItemDecoration(channelsDividerItemDecoration)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //загрузка данных или их восстановление из предедущего состояния
        goToState(LoadingState)

        if(savedInstanceState == null)
            searchInChannels("")

        else
            adapterWrapper.dataList = savedInstanceState
                .getParcelableArrayList<ExpandedChanelGroup>(SAVED_BUNDLE_CHANNELS)
                ?.toList() ?: listOf()

    }

    fun searchInChannels(searchString: String) {
        goToState(LoadingState)

        UseCaseTestRepository().searchChannels(searchString)
            .subscribeOn(Schedulers.io())
            .delay(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = { goToState(ErrorState(it)) },
                onSuccess = {
                    goToState(ResultState(it))
                }
            ).addTo(compositeDisposable)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(
            SAVED_BUNDLE_CHANNELS,
            arrayListOf<ExpandedChanelGroup>().apply { addAll(adapterWrapper.dataList) }
        )

        super.onSaveInstanceState(outState)
    }

    override fun toLoading(currentState: ChannelsScreenState, resultState: LoadingState): Boolean {
        binding.progressChannelsLoading.isVisible = true
        return false
    }

    override fun toResult(currentState: ChannelsScreenState, resultState: ResultState): Boolean {
        binding.progressChannelsLoading.isVisible = false
        adapterWrapper.dataList = resultState.items
        return true
    }

    override fun toError(currentState: ChannelsScreenState, resultState: ErrorState): Boolean {
        Toast.makeText(context, "Error can't get data!", Toast.LENGTH_SHORT).show()
        return false
    }


    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.dispose()
        _binding = null
    }

    companion object {
        private const val SAVED_BUNDLE_CHANNELS = "com.alexpaxom.SAVED_BUNDLE_CHANNELS"
        const val FRAGMENT_ID = "com.alexpaxom.CHANNELS_LIST_FRAGMENT_ID"

        @JvmStatic
        fun newInstance() = ChannelsListFragment()
    }
}