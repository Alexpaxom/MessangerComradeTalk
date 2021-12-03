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
import com.alexpaxom.homework_2.app.App
import com.alexpaxom.homework_2.app.adapters.userslist.UsersListAdapter
import com.alexpaxom.homework_2.app.adapters.userslist.UsersListFactoryHolders
import com.alexpaxom.homework_2.databinding.FragmentUsersBinding
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject
import javax.inject.Provider

class UsersFragment : ViewBindingFragment<FragmentUsersBinding>(), BaseView<UsersViewState, UsersEffect> {

    private val usersListFactoryHolders = UsersListFactoryHolders{ onUserClickListener(it) }
    private val usersListAdapter = UsersListAdapter(usersListFactoryHolders)

    @Inject
    lateinit var daggerPresenter: Provider<UsersPresenter>

    @InjectPresenter
    lateinit var presenter: UsersPresenter

    @ProvidePresenter
    fun providePresenter(): UsersPresenter = daggerPresenter.get()

    override fun createBinding(): FragmentUsersBinding =
        FragmentUsersBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity?.application as App).appComponent
            .getScreenComponent()
            .create()
            .inject(this)
        super.onCreate(savedInstanceState)
    }

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
            presenter.processEvent(
                UsersEvent.SearchUsers(it.toString())
            )
        }

        binding.searchUsers.searchBtn.setOnClickListener {
            presenter.processEvent(
                UsersEvent.SearchUsers(
                    binding.searchUsers.searchEdit.text.toString()
                )
            )
        }
    }

    private fun onUserClickListener(adapterPos: Int) {
        val profileFragment = ProfileFragment.newInstance(usersListAdapter.dataList[adapterPos].id)
        profileFragment.show(childFragmentManager, ProfileFragment.FRAGMENT_ID)
    }

    override fun processState(state: UsersViewState) {

        usersListAdapter.dataList = state.users
        binding.usersProgress.isVisible = state.isEmptyLoad
    }

    override fun processEffect(effect: UsersEffect) {
        when(effect) {
            is UsersEffect.ShowError -> Toast.makeText( context, effect.error, Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        const val FRAGMENT_ID = "com.alexpaxom.USERS_FRAGMENT_ID"


        @JvmStatic
        fun newInstance() = UsersFragment()
    }
}