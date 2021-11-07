package com.alexpaxom.homework_2.data.usecases

import com.alexpaxom.homework_2.data.models.UserItem
import io.reactivex.Single

interface SearchUsers {
    fun search(searchString: String = ""): Single<List<UserItem>>
}