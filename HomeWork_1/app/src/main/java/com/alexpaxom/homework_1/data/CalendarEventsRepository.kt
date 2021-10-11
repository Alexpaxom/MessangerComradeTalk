package com.alexpaxom.homework_1.data

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract

class CalendarEventsRepository(private val appContext: Context) {
    private val uri: Uri = CalendarContract.Events.CONTENT_URI

    companion object {
        private val EVENT_PROJECTION: Array<String> = arrayOf(
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND,
            CalendarContract.Events.ORGANIZER,
        )

        private const val PROJECTION_TITLE: Int = 0
        private const val PROJECTION_DTSTART: Int = 1
        private const val PROJECTION_DTEND: Int = 2
        private const val PROJECTION_ORGANIZER: Int = 3
    }


    private val cur: Cursor? = appContext.contentResolver.query(
        uri,
        EVENT_PROJECTION,
        null,
        null,
        null
    )


    fun getCalendarEvents(): List<CalendarEventInfo> {
        requireNotNull(cur) { "Can't create cursor for calendar content provider" }

        val ret: ArrayList<CalendarEventInfo> = arrayListOf()

        while (cur.moveToNext()) {
            val calendarEvent = CalendarEventInfo(
                title =  cur.getString(PROJECTION_TITLE)?: "",
                dtstart =  cur.getLong(PROJECTION_DTSTART),
                dtend = cur.getLong(PROJECTION_DTEND),
                organizer = cur.getString(PROJECTION_ORGANIZER)?: ""
            )

            ret.add(calendarEvent)
        }

        return ret
    }

}