package com.alexpaxom.homework_2.app.adapters.userslist

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseDiffUtilAdapter
import com.alexpaxom.homework_2.data.models.UserItem

class UsersListAdapter(
    usersListFactoryHolders: UsersListFactoryHolders
): BaseDiffUtilAdapter<UserItem>(usersListFactoryHolders)
{
    init {
        diffUtil = AsyncListDiffer(this, UsersListAdapterDiffUtil())
    }

    class UsersListAdapterDiffUtil: DiffUtil.ItemCallback<UserItem>(){
        override fun areItemsTheSame(oldItem: UserItem, newItem: UserItem): Boolean
           = oldItem.id == newItem.id


        override fun areContentsTheSame(oldItem: UserItem, newItem: UserItem): Boolean
            = oldItem == newItem

    }

}