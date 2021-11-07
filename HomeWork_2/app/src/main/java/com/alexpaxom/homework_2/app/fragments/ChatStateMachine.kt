package com.alexpaxom.homework_2.app.fragments

import com.alexpaxom.homework_2.data.models.MessageItem


sealed interface ChatState {
    fun toInitialState(): Boolean = defaultAction
    fun toResultState(): Boolean = defaultAction
    fun toLoadingState(): Boolean = defaultAction
    fun toErrorState(): Boolean = defaultAction

    object InitialState : ChatState {
        override fun toLoadingState(): Boolean = true
        override fun toResultState(): Boolean = true
    }

    class ResultState(val messages: List<MessageItem>): ChatState {
        override fun toResultState(): Boolean = true
        override fun toLoadingState(): Boolean = true
        override fun toErrorState(): Boolean = true
    }

    object LoadingState : ChatState {
        override fun toResultState(): Boolean = true
        override fun toLoadingState(): Boolean = true
        override fun toErrorState(): Boolean = true
    }

    class ErrorState(val error: Throwable): ChatState {
        override fun toLoadingState(): Boolean = true
    }

    companion object {
        private const val defaultAction = false
    }
}

interface ChatStateMachine {
    var currentState: ChatState

    fun goToState(newState: ChatState) {
        val canGoToState = when(newState) {
            is ChatState.LoadingState -> {
                if(currentState.toLoadingState()) toLoading(newState)
                currentState.toLoadingState()
            }

            is ChatState.ResultState -> {
                if(currentState.toResultState()) toResult(newState)
                currentState.toResultState()
            }

            is ChatState.ErrorState -> {
                if(currentState.toErrorState()) toError(newState)
                currentState.toErrorState()
            }

            is ChatState.InitialState -> { currentState.toInitialState() }
        }

        if(canGoToState)
            currentState = newState
        else
            defaultToStateCallback(newState)
    }


    fun toResult(resultState: ChatState.ResultState){}
    fun toLoading(loadingState: ChatState.LoadingState){}
    fun toError(errorState: ChatState.ErrorState){}

    fun defaultToStateCallback(nextState: ChatState): Boolean {
        error("Try to go from state ${currentState::class.java.name} to ${nextState::class.java.name} this transition not defined")
    }
}