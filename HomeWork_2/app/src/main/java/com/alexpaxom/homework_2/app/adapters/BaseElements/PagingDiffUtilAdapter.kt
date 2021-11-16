package com.alexpaxom.homework_2.app.adapters.BaseElements

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PagingRecyclerUtil(
    recyclerView: RecyclerView
) {

    abstract fun loadNewData(params: PageLoadingParams)

    abstract fun checkLoadData(bottomPos: Int, topPos: Int): PageLoadingParams

    init {
        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as? LinearLayoutManager

                if(layoutManager != null) {
                    checkLoadData(
                        bottomPos = layoutManager.findLastVisibleItemPosition(),
                        topPos = layoutManager.findFirstVisibleItemPosition()
                    ).let {
                        if(it.loadNextPage)
                            loadNewData(it)
                    }
                }
                else
                    error("Paging adapter can work only with LinearLayoutManager!")
            }
        })
    }
}

open class PageLoadingParams (
    val loadNextPage: Boolean = true
) {
    companion object {
        val noLoadNextPage = object : PageLoadingParams(false){}
    }
}