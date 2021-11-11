package com.alexpaxom.homework_2.app.fragments

import com.alexpaxom.homework_2.data.models.UserItem

sealed interface UsersState {
    fun toInitialState(): Boolean = defaultAction
    fun toResultState(): Boolean = defaultAction
    fun toLoadingState(): Boolean = defaultAction
    fun toErrorState(): Boolean = defaultAction
    fun toStatusRefresh(): Boolean = defaultAction

    object InitialState : UsersState {
        override fun toLoadingState(): Boolean = true
    }

    class ResultState(val users: List<UserItem>): UsersState {
        override fun toResultState(): Boolean = true
        override fun toLoadingState(): Boolean = true
        override fun toErrorState(): Boolean = true
        override fun toStatusRefresh(): Boolean = true
    }

    object LoadingState : UsersState {
        override fun toResultState(): Boolean = true
        override fun toLoadingState(): Boolean = true
        override fun toErrorState(): Boolean = true
    }

    class StatusRefreshState(val users: List<UserItem>) : UsersState {
        override fun toResultState(): Boolean = true
        override fun toLoadingState(): Boolean = true
        override fun toErrorState(): Boolean = true
    }

    class ErrorState(val error: Throwable): UsersState {
        override fun toLoadingState(): Boolean = true
    }

    companion object {
        private const val defaultAction = false
    }
}

interface UsersStateMachine {
    var currentState: UsersState

    fun goToState(newState: UsersState) {
        val canGoToState = when(newState) {
            is UsersState.LoadingState -> {
                if(currentState.toLoadingState()) toLoading(newState)
                currentState.toLoadingState()
            }

            is UsersState.ResultState -> {
                if(currentState.toResultState()) toResult(newState)
                currentState.toResultState()
            }

            is UsersState.ErrorState -> {
                if(currentState.toErrorState()) toError(newState)
                currentState.toErrorState()
            }

            is UsersState.StatusRefreshState -> {
                if(currentState.toStatusRefresh()) toStatusRefresh(newState)
                currentState.toStatusRefresh()
            }

            is UsersState.InitialState -> { currentState.toInitialState() }
        }

        if(canGoToState)
            currentState = newState
        else
            defaultToStateCallback(newState)
    }


    fun toResult(resultState: UsersState.ResultState){}
    fun toLoading(loadingState: UsersState.LoadingState){}
    fun toError(errorState: UsersState.ErrorState){}
    fun toStatusRefresh(refreshState:  UsersState.StatusRefreshState){}

    fun defaultToStateCallback(nextState: UsersState): Boolean {
        error("Try to go from state ${currentState::class.java.name} to ${nextState::class.java.name} this transition not defined")
    }
}