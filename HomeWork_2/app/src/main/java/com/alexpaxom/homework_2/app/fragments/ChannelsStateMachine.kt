package com.alexpaxom.homework_2.app.fragments

import com.alexpaxom.homework_2.data.models.ExpandedChanelGroup

sealed interface ChannelsScreenState {
    fun toInitialState(): Boolean = defaultAction
    fun toResultState(): Boolean = defaultAction
    fun toLoadingState(): Boolean = defaultAction
    fun toErrorState(): Boolean = defaultAction

    companion object {
        private const val defaultAction = false
    }
}

object InitialState : ChannelsScreenState {
    override fun toLoadingState(): Boolean = true
}

class ResultState(val items: List<ExpandedChanelGroup>): ChannelsScreenState {
    override fun toResultState(): Boolean = true
    override fun toLoadingState(): Boolean = true
    override fun toErrorState(): Boolean = true
}

object LoadingState : ChannelsScreenState {
    override fun toResultState(): Boolean = true
    override fun toLoadingState(): Boolean = true
    override fun toErrorState(): Boolean = true
}

class ErrorState(val error: Throwable): ChannelsScreenState {
    override fun toLoadingState(): Boolean = true
}


interface ChannelsStateMachine {

    var currentState: ChannelsScreenState

    fun goToState(newState: ChannelsScreenState) {
        val canGoToState = when(newState) {
            is LoadingState -> {
                if(currentState.toLoadingState()) toLoading(newState)
                currentState.toLoadingState()
            }

            is ResultState -> {
                if(currentState.toResultState()) toResult(newState)
                currentState.toResultState()
            }

            is ErrorState -> {
                if(currentState.toErrorState()) toError(newState)
                currentState.toErrorState()
            }

            is InitialState -> {
                if(currentState.toInitialState()) toInitial(newState)
                currentState.toInitialState()
            }
        }

        if(canGoToState)
            currentState = newState
        else
            defaultToStateCallback(newState)
    }


    fun toResult(resultState: ResultState){}
    fun toLoading(loadingState: LoadingState){}
    fun toError(errorState: ErrorState){}
    fun toInitial(initialState: InitialState){}

    fun defaultToStateCallback(nextState: ChannelsScreenState): Boolean {
        error("Try to go from state ${currentState::class.java.name} to ${nextState::class.java.name} this transition not defined")
    }
}


