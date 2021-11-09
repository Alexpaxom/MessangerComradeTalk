package com.alexpaxom.homework_2.app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.adapters.userslist.UsersListAdapter
import com.alexpaxom.homework_2.app.adapters.userslist.UsersListFactoryHolders
import com.alexpaxom.homework_2.data.models.User
import com.alexpaxom.homework_2.data.repositories.TestMessagesRepository
import com.alexpaxom.homework_2.databinding.FragmentUsersBinding

class UsersFragment : ViewBindingFragment<FragmentUsersBinding>() {
    override var _binding: Lazy<FragmentUsersBinding>? = lazy {
        FragmentUsersBinding.inflate(layoutInflater)
    }
    private val usersListFactoryHolders = UsersListFactoryHolders{ onUserClickListener(it) }
    private val usersListAdapter = UsersListAdapter(usersListFactoryHolders)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализация списка
        usersListAdapter.dataList = if(savedInstanceState == null)
            TestMessagesRepository().getUsers()
        else
            savedInstanceState.getParcelableArrayList<User>(SAVED_BUNDLE_USERS)?.toList() ?: listOf()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding.usersList.layoutManager = LinearLayoutManager(context)
        binding.usersList.adapter = usersListAdapter

        // Устанавливаем декоратор
        val channelsDividerItemDecoration = DividerItemDecoration(
            context,
            RecyclerView.VERTICAL
        )
        channelsDividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.users_list_decoration_divider))

        binding.usersList.addItemDecoration(channelsDividerItemDecoration)

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val savedListUsers = ArrayList(usersListAdapter.dataList)

        outState.putParcelableArrayList(
            SAVED_BUNDLE_USERS,
            savedListUsers
        )
        super.onSaveInstanceState(outState)
    }

    fun onUserClickListener(adapterPos: Int) {

        parentFragmentManager.beginTransaction().replace(
            R.id.wrap_container,
            ProfileFragment.newInstance(usersListAdapter.dataList[adapterPos].id),
            ProfileFragment.FRAGMENT_ID
        )
            .addToBackStack(ProfileFragment.FRAGMENT_ID)
            .commit()
    }


    companion object {
        private const val SAVED_BUNDLE_USERS = "com.alexpaxom.SAVED_BUNDLE_USERS"
        const val FRAGMENT_ID = "com.alexpaxom.USERS_FRAGMENT_ID"

        @JvmStatic
        fun newInstance() = UsersFragment()
    }
}