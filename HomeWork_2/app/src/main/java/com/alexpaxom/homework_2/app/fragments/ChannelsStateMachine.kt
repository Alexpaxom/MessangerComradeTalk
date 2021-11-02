package com.alexpaxom.homework_2.app.fragments

import com.alexpaxom.homework_2.data.models.ExpandedChanelGroup

sealed interface ChannelsScreenState

object InitialState : ChannelsScreenState
class ResultState(val items: List<ExpandedChanelGroup>): ChannelsScreenState
object LoadingState : ChannelsScreenState
class ErrorState(val error: Throwable): ChannelsScreenState


interface ChannelsStateMachine {

    var currentState: ChannelsScreenState

    fun goToState(newState: ChannelsScreenState) {
        val canGoToState = when(newState) {
            is LoadingState -> toLoading(currentState, newState)
            is ResultState -> toResult(currentState, newState)
            is ErrorState -> toError(currentState, newState)
            is InitialState -> toInitial(currentState, newState)
        }

        if(canGoToState)
            currentState = newState
    }

    fun toResult(currentState: ChannelsScreenState, resultState: ResultState): Boolean = defaultToStateCallback(resultState)

    fun toLoading( currentState: ChannelsScreenState, resultState: LoadingState): Boolean = defaultToStateCallback(resultState)

    fun toError(currentState: ChannelsScreenState, resultState: ErrorState): Boolean = defaultToStateCallback(resultState)

    fun toInitial(currentState: ChannelsScreenState, resultState: InitialState): Boolean = defaultToStateCallback(resultState)

    fun defaultToStateCallback(nextState: ChannelsScreenState): Boolean {
        error("Try to go from state ${currentState::class.java.name} to ${nextState::class.java.name} this transition not defined")
    }
}


