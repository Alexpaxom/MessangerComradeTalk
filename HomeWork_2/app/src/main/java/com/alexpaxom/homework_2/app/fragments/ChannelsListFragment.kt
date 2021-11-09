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
import com.alexpaxom.homework_2.app.adapters.cannelslist.ChannelsListAdapter
import com.alexpaxom.homework_2.app.adapters.cannelslist.ChannelsListHoldersFactory
import com.alexpaxom.homework_2.data.models.ExpandedChanelGroup
import com.alexpaxom.homework_2.data.usecases.testusecases.SearchExpandedChannelGroupTestImpl
import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.SearchExpandedChannelGroupZulipImpl
import com.alexpaxom.homework_2.databinding.CnannelsListFragmentBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.ReplaySubject
import java.util.concurrent.TimeUnit

class ChannelsListFragment : ViewBindingFragment<CnannelsListFragmentBinding>(),
ChannelsStateMachine {

    override var currentState: ChannelsScreenState = InitialState
    private val compositeDisposable = CompositeDisposable()
    private val channelsListHoldersFactory = ChannelsListHoldersFactory {
        val chatFragment = ChatFragment.newInstance()
        chatFragment.show(parentFragmentManager, ChatFragment.FRAGMENT_ID)
    }
    private val channelsListAdapter = ChannelsListAdapter(channelsListHoldersFactory)

    private val searchChannelsSubject: BehaviorSubject<String> = BehaviorSubject.create()

    private val searchExpandedChannelGroup = SearchExpandedChannelGroupZulipImpl()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        searchChannelsSubject
            .subscribeOn(Schedulers.io())
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { goToState(LoadingState) }
            .observeOn(Schedulers.io())
            .debounce(500, TimeUnit.MILLISECONDS, Schedulers.io())
            .switchMap {
                val subscribedFilterFlag = arguments?.getBoolean(SUBSCRIBED_FILTER_FLAG) ?: false
                if(subscribedFilterFlag)
                    searchExpandedChannelGroup.searchInSubscribedChannelGroups(it).toObservable()
                else
                    searchExpandedChannelGroup.searchInAllChannelGroups(it).toObservable()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { goToState(ResultState(it)) },
                onError = { goToState(ErrorState(it)) }
            )
            .addTo(compositeDisposable)

        // Иннициализация адаптера и восстановление его состояния
        if(savedInstanceState == null) {
            searchChannels(INITIAL_SEARCH_QUERY)
        }
        else
            channelsListAdapter.dataList = savedInstanceState.getParcelableArrayList<ExpandedChanelGroup>(SAVED_BUNDLE_CHANNELS)
                ?.toList() ?: listOf()

        super.onViewCreated(view, savedInstanceState)
    }

    fun searchChannels(searchString: String) {
        searchChannelsSubject.onNext(searchString)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(
            SAVED_BUNDLE_CHANNELS,
            arrayListOf<ExpandedChanelGroup>().apply { addAll(channelsListAdapter.dataList) }
        )

        super.onSaveInstanceState(outState)
    }

    override fun toError(resultState: ErrorState){
        binding.progressChannelsLoading.isVisible = false
        Toast.makeText(context, resultState.error.localizedMessage, Toast.LENGTH_LONG).show()
    }

    override fun toResult(resultState: ResultState){
        binding.progressChannelsLoading.isVisible = false
        channelsListAdapter.dataList = resultState.items
    }

    override fun toLoading(loadingState: LoadingState) {
        binding.progressChannelsLoading.isVisible = true
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }


    companion object {
        private const val SAVED_BUNDLE_CHANNELS = "com.alexpaxom.SAVED_BUNDLE_CHANNELS"
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