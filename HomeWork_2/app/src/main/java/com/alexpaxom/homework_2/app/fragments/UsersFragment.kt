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

class UsersFragment : Fragment() {

    private var _binding: FragmentUsersBinding? = null
    private val binding get() = _binding!!
    private val usersListFactoryHolders = UsersListFactoryHolders()
    private val usersListAdapter = UsersListAdapter(usersListFactoryHolders)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsersBinding.inflate(inflater, container, false)

        // Инициализация списка
        usersListAdapter.dataList = if(savedInstanceState == null)
            TestMessagesRepository().getUsers()
        else
            savedInstanceState.getParcelableArrayList<User>(SAVED_BUNDLE_USERS)?.toList() ?: listOf()


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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val SAVED_BUNDLE_USERS = "com.alexpaxom.SAVED_BUNDLE_USERS"
        const val FRAGMENT_ID = "com.alexpaxom.USERS_FRAGMENT_ID"

        @JvmStatic
        fun newInstance() = UsersFragment()
    }
}