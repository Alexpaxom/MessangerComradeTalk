package com.alexpaxom.homework_1.app.servises

import android.app.IntentService
import android.content.Intent
import com.alexpaxom.homework_1.data.CalendarEventInfo
import com.alexpaxom.homework_1.data.CalendarEventsRepository
import androidx.localbroadcastmanager.content.LocalBroadcastManager



class GetCalendarDataIntentService : IntentService("GetCalendarDataIntentService") {

    companion object {
        val CALENDAR_DATA_INTENT_ID = "com.alexpaxom.CALENDAR_DATA_INTENT_ID"
        val CALENDAR_EVENTS_ID = "com.alexpaxom.CALENDAR_EVENTS_ID"
    }

    override fun onHandleIntent(intent: Intent?) {
        returnResultToActivity(getEventsFromCalendar())
    }

    private fun getEventsFromCalendar(): Intent {
        val calendarEventsRepository = CalendarEventsRepository(this.applicationContext)
        val calendarEvents: List<CalendarEventInfo> = calendarEventsRepository.getCalendarEvents()

        val intent = Intent(CALENDAR_DATA_INTENT_ID)
        intent.putExtra(CALENDAR_EVENTS_ID, calendarEvents.toTypedArray())

        return intent
    }

    private fun returnResultToActivity(intent: Intent) {
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

}