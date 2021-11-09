package com.alexpaxom.homework_2.app.adapters.decorators

import android.graphics.Canvas
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.children
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.adapters.chathistory.ChatHistoryAdapter
import com.alexpaxom.homework_2.databinding.DateDelimeterBinding

class ChatDateDecorator(recyclerView: RecyclerView) : RecyclerView.ItemDecoration(){
    val drawableLayer = DateDelimeterBinding.inflate(LayoutInflater.from(recyclerView.context), recyclerView, false)
    val widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    val spaceBetweenElements = recyclerView.context.resources.getDimensionPixelSize(R.dimen.chat_delimiter_messages)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val adapter = parent.adapter as ChatHistoryAdapter

        outRect.top = spaceBetweenElements

        if(adapter.isDecorate(parent.getChildAdapterPosition(view))) {
            drawableLayer.textDate.let {
                it.text = adapter.getDecorateParam(parent.getChildAdapterPosition(view))
                measureView()
                outRect.top += it.marginTop + it.marginBottom + it.height
            }
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val adapter = parent.adapter as ChatHistoryAdapter

        val left = parent.paddingLeft
        val right = parent.width - parent.paddingLeft
        val x = left + (right-left)/2f

        parent.children.forEach { child ->
            if (adapter.isDecorate(parent.getChildAdapterPosition(child))) {
                drawableLayer.textDate.text = adapter.getDecorateParam(parent.getChildAdapterPosition(child))
                val y = child.top.toFloat()-drawableLayer.textDate.height - drawableLayer.textDate.marginBottom

                // измеряем и отрисовываем TextView
                measureView()
                c.save()
                c.translate(x-drawableLayer.textDate.width/2, y)
                drawableLayer.root.draw(c)
                c.restore()
            }
        }
    }


    private fun measureView() {
        drawableLayer.root.measure(widthSpec, heightSpec)
        drawableLayer.root.layout(0, 0, 0, 0)
    }
}