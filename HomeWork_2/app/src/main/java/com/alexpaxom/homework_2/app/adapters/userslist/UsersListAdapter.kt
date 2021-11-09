package com.alexpaxom.homework_2.app.adapters.userslist

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseDiffUtilAdapter
import com.alexpaxom.homework_2.data.models.User

class UsersListAdapter(
    usersListFactoryHolders: UsersListFactoryHolders
): BaseDiffUtilAdapter<User>(usersListFactoryHolders)
{
    init {
        diffUtil = AsyncListDiffer(this, UsersListAdapterDiffUtil())
    }

    class UsersListAdapterDiffUtil: DiffUtil.ItemCallback<User>(){
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean
           = oldItem.id == newItem.id


        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean
            = oldItem == newItem

    }

}