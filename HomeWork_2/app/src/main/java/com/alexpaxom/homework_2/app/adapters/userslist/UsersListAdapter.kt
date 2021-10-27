package com.alexpaxom.homework_2.app.adapters.userslist

import com.alexpaxom.homework_2.app.adapters.BaseElements.BaseDiffUtilAdapter
import com.alexpaxom.homework_2.data.models.User

class UsersListAdapter(
    usersListFactoryHolders: UsersListFactoryHolders
): BaseDiffUtilAdapter<User>(usersListFactoryHolders)