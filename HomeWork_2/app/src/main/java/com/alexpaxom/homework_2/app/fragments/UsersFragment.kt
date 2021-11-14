package com.alexpaxom.homework_2.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.adapters.userslist.UsersListAdapter
import com.alexpaxom.homework_2.app.adapters.userslist.UsersListFactoryHolders
import com.alexpaxom.homework_2.data.models.UserItem
import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.SearchUsersZulipApiImpl
import com.alexpaxom.homework_2.data.usecases.zulipapiusecases.UserStatusUseCaseZulipApiImpl
import com.alexpaxom.homework_2.databinding.FragmentUsersBinding
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import retrofit2.HttpException
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class UsersFragment : ViewBindingFragment<FragmentUsersBinding>(), UsersStateMachine {

    override var currentState: UsersState = UsersState.InitialState

    private val usersListFactoryHolders = UsersListFactoryHolders{ onUserClickListener(it) }
    private val usersListAdapter = UsersListAdapter(usersListFactoryHolders)
    private val compositeDisposable = CompositeDisposable()

    private val searchUsersSubject: BehaviorSubject<String> = BehaviorSubject.create()
    private val searchUsers = SearchUsersZulipApiImpl()
    private val userStatusInfo = UserStatusUseCaseZulipApiImpl()

    override fun createBinding(): FragmentUsersBinding =
        FragmentUsersBinding.inflate(layoutInflater)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding.usersList.layoutManager = LinearLayoutManager(context)
        binding.usersList.adapter = usersListAdapter

        val channelsDividerItemDecoration = DividerItemDecoration(
            context,
            RecyclerView.VERTICAL
        )

        ResourcesCompat.getDrawable(
            resources,
            R.drawable.users_list_decoration_divider, activity?.theme
        )?.let {
                channelsDividerItemDecoration.setDrawable(it)
        }


        binding.usersList.addItemDecoration(channelsDividerItemDecoration)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchUsers.searchEdit.doAfterTextChanged {
            searchUsers(it.toString())
        }

        binding.searchUsers.searchBtn.setOnClickListener {
            searchUsers(binding.searchUsers.searchEdit.text.toString())
        }

        if(savedInstanceState==null) {
            searchUsersSubject
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .distinctUntilChanged()
                .debounce(500, TimeUnit.MILLISECONDS, Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { goToState(UsersState.LoadingState) }
                .observeOn(Schedulers.io())
                .switchMapSingle { searchUsers.search(it).flatMap { users -> loadUsersStates(users) } }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = {
                        goToState(UsersState.ResultState(it))
                    },
                    onError = {
                        goToState(UsersState.ErrorState(it))
                    }
                )
                .addTo(compositeDisposable)

            // загружаем первую порцию данных
            searchUsers(INITIAL_SEARCH_QUERY)
        }
        else
            usersListAdapter.dataList = savedInstanceState.getParcelableArrayList<UserItem>(
                SAVED_BUNDLE_USERS
            )?.toList() ?: listOf()
    }

    private fun searchUsers(searchString: String) {
        searchUsersSubject.onNext(searchString)
    }

    private fun loadUsersStates(users: List<UserItem>): Single<List<UserItem>> {

        val usersMap = users.map { it.id to it }.toMap()

        return Observable.fromIterable(users)
            .subscribeOn(Schedulers.io())
            .concatMapSingle {
                userStatusInfo.getStatusForUser(it.id)
            }
            .toList()
            .map { statuses ->
                statuses.map { status ->
                    usersMap[status.userId]!!.copy(status = status.aggregatedStatus)
                }
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val savedListUsers = ArrayList(usersListAdapter.dataList)

        outState.putParcelableArrayList(
            SAVED_BUNDLE_USERS,
            savedListUsers
        )
        super.onSaveInstanceState(outState)
    }

    private fun onUserClickListener(adapterPos: Int) {
        val profileFragment = ProfileFragment.newInstance(usersListAdapter.dataList[adapterPos].id)
        profileFragment.show(childFragmentManager, ProfileFragment.FRAGMENT_ID)
    }

    override fun toResult(resultState: UsersState.ResultState) {
        binding.usersProgress.isVisible = false
        usersListAdapter.dataList = resultState.users
    }

    override fun toLoading(loadingState: UsersState.LoadingState) {
        binding.usersProgress.isVisible = true
    }

    override fun toError(errorState: UsersState.ErrorState) {
        binding.usersProgress.isVisible = false

        var error = errorState.error.localizedMessage

        if(errorState.error is HttpException) {
            error = errorState.error.response()?.errorBody()?.string()
        }

        Toast.makeText( context, error, Toast.LENGTH_LONG).show()
    }

    override fun toStatusRefresh(refreshState: UsersState.StatusRefreshState) {
        loadUsersStates(refreshState.users)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    companion object {
        private const val SAVED_BUNDLE_USERS = "com.alexpaxom.SAVED_BUNDLE_USERS"
        private const val INITIAL_SEARCH_QUERY = ""
        const val FRAGMENT_ID = "com.alexpaxom.USERS_FRAGMENT_ID"


        @JvmStatic
        fun newInstance() = UsersFragment()
    }
}