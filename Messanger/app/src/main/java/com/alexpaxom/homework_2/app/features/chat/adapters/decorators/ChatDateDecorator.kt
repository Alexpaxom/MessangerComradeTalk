package com.alexpaxom.homework_2.app.features.chat.adapters.decorators

import android.graphics.Canvas
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.children
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.app.features.chat.adapters.ChatHistoryAdapter
import com.alexpaxom.homework_2.customview.getHeightWithMargins
import com.alexpaxom.homework_2.databinding.DateDelimeterBinding
import com.alexpaxom.homework_2.databinding.TopicDelimeterBinding

class ChatDateDecorator(recyclerView: RecyclerView) : RecyclerView.ItemDecoration(){
    private val dateDelimiterLayout = DateDelimeterBinding.inflate(LayoutInflater.from(recyclerView.context), recyclerView, false)
    private val topicDelimiterLayout = TopicDelimeterBinding.inflate(LayoutInflater.from(recyclerView.context), recyclerView, false)
    val topicDelimiterMargin = recyclerView.context.resources.getDimensionPixelSize(R.dimen.chat_topic_delimiter_margin)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val adapter = parent.adapter as ChatHistoryAdapter


        parent.getChildAdapterPosition(view).let { adapterPos ->
            if(adapterPos != RecyclerView.NO_POSITION) {

                if(adapter.isDrawDateDecorator(adapterPos)) {
                    dateDelimiterLayout.textDate.text = adapter.getDateDecorateParam(adapterPos)
                    measureView(dateDelimiterLayout, parent)
                    outRect.top += dateDelimiterLayout.textDate.getHeightWithMargins()
                }

                if(adapter.isDrawNextTopicDecorator(adapterPos)) {
                    topicDelimiterLayout.topicName.text = adapter.getTopicDecorateParam(adapterPos)
                    measureView(topicDelimiterLayout, parent)
                    outRect.top += topicDelimiterLayout.topicDelimiter.getHeightWithMargins()+topicDelimiterMargin
                }

                if(adapter.isDrawPrevTopicDecorator(adapterPos)) {
                    topicDelimiterLayout.topicName.text = adapter.getTopicDecorateParam(adapterPos)
                    measureView(topicDelimiterLayout, parent)
                    outRect.bottom += topicDelimiterLayout.topicDelimiter.getHeightWithMargins()+topicDelimiterMargin
                }
            }

        }

    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val adapter = parent.adapter as ChatHistoryAdapter

        // находим центр ресайклера
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingLeft
        val x = left + (right-left)/2f

        parent.children.forEach { child ->

            parent.getChildAdapterPosition(child).let { adapterPos ->
                if (adapterPos != RecyclerView.NO_POSITION) {
                    var y = child.top.toFloat()

                    // отрисовываем разделитель следующего топика
                    if(adapter.isDrawNextTopicDecorator(adapterPos))
                    {
                        topicDelimiterLayout.topicName.text = adapter.getTopicDecorateParam(adapterPos)

                        measureView(topicDelimiterLayout, parent)
                        y -= topicDelimiterLayout.topicDelimiter.getHeightWithMargins()

                        drawDelimiterAtPosition(
                            view = topicDelimiterLayout.root,
                            canvas = c,
                            x = left.toFloat(),
                            y = y,
                        )
                    }

                    // отрисовываем разделитель даты
                    if(adapter.isDrawDateDecorator(adapterPos))
                    {
                        dateDelimiterLayout.textDate.text = adapter.getDateDecorateParam(adapterPos)

                        measureView(dateDelimiterLayout, parent)
                        y -= dateDelimiterLayout.textDate.getHeightWithMargins()+topicDelimiterMargin

                        drawDelimiterAtPosition(
                            view = dateDelimiterLayout.root,
                            canvas = c,
                            x = x - dateDelimiterLayout.textDate.width / 2,
                            y = y,
                        )
                    }

                    // отрисовываем разделитель предидущего топика
                    if(adapter.isDrawPrevTopicDecorator(adapterPos)) {
                        topicDelimiterLayout.topicName.text = adapter.getTopicDecorateParam(adapterPos)

                        measureView(topicDelimiterLayout, parent)
                        y = child.bottom.toFloat() + topicDelimiterLayout.topicDelimiter.marginTop
                        drawDelimiterAtPosition(
                            view = topicDelimiterLayout.root,
                            canvas = c,
                            x = left.toFloat(),
                            y = y,
                        )
                    }

                }
            }
        }
    }

    private fun drawDelimiterAtPosition(view: View, canvas: Canvas, x: Float, y:Float) {
        canvas.save()
        canvas.translate(x, y)
        view.draw(canvas)
        canvas.restore()
    }

    private fun measureView(drawable: ViewBinding, parent: View) {
        drawable.root.measure(
            View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        drawable.root.layout(0, 0, 0, 0)
    }
}