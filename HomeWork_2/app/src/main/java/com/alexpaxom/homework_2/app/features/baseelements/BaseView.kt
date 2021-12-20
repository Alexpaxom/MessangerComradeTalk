package com.alexpaxom.homework_2.app.features.baseelements

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface BaseView<S, E> : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun processState(state: S)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun processEffect(effect: E)
}