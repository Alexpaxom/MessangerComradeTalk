package com.alexpaxom.homework_1.app.adapters

import BaseViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alexpaxom.homework_1.data.CalendarEventInfo
import com.alexpaxom.homework_1.databinding.CalendarEventItemBinding
import java.text.SimpleDateFormat
import java.util.Date

class CalendarEventsAdapter: BaseAdapter<CalendarEventInfo>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<CalendarEventInfo> {
        return ContactInfoHolder(
            CalendarEventItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class ContactInfoHolder(val itemCardBinding: CalendarEventItemBinding):
        BaseViewHolder<CalendarEventInfo>(itemCardBinding) {
        override fun bind(model: CalendarEventInfo) {
            val format = SimpleDateFormat("dd.MM.yyyy")

            itemCardBinding.calendarEventTitle.text = model.title
            itemCardBinding.calendarEventEmail.text = model.organizer
            itemCardBinding.calendarEventStart.text = format.format(Date(model.dtstart))
            itemCardBinding.calendarEventEnd.text =format.format(Date(model.dtend))
        }

    }
}