package com.alexpaxom.homework_2.app.adapters.decorators

import android.graphics.Canvas
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.adapters.ChatHistoryAdapter
import com.alexpaxom.homework_2.databinding.DateDelimeterBinding

class ChatDateDecorator(recyclerView: RecyclerView) : RecyclerView.ItemDecoration(){
    val drawableLayer = DateDelimeterBinding.inflate(LayoutInflater.from(recyclerView.context), recyclerView, false)
    val widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val adapter = parent.adapter as ChatHistoryAdapter

        outRect.top = view.context.resources.getDimensionPixelSize(R.dimen.chat_delimiter_messages)

        if(adapter.isDecorate(parent.getChildAdapterPosition(view))) {
            drawableLayer.textDate.let {
                it.text =
                    adapter.getItemAt(parent.getChildAdapterPosition(view)).datetime.toString()
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


        for(i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            if(!adapter.isDecorate(parent.getChildAdapterPosition(child)))
                continue


            drawableLayer.textDate.text = adapter.getItemAt(parent.getChildAdapterPosition(child)).datetime.toString()
            val y = child.top.toFloat()-drawableLayer.textDate.height - drawableLayer.textDate.marginBottom
            drawView(c, x-drawableLayer.textDate.width/2, y)
        }
    }

    private fun drawView(c: Canvas, x: Float, y: Float) {
        measureView()
        c.save()
        c.translate(x, y)
        drawableLayer.root.draw(c)
        c.restore()
    }

    private fun measureView() {
        drawableLayer.root.measure(widthSpec, heightSpec)
        drawableLayer.root.layout(0, 0, 0, 0)
    }
}