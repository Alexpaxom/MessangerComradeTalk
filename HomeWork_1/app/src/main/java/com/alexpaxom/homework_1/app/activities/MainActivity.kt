package com.alexpaxom.homework_1.app.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexpaxom.homework_1.R
import com.alexpaxom.homework_1.app.adapters.CalendarEventsAdapter
import com.alexpaxom.homework_1.data.CalendarEventInfo
import com.alexpaxom.homework_1.databinding.ActivityMainBinding

private const val CALENDAR_EVENTS_LIST_STATE = "com.alexpaxom.CALENDAR_EVENTS_LIST_STATE"
private const val CALENDAR_EVENTS_LIST_DATA = "com.alexpaxom.CALENDAR_EVENTS_LIST_DATA"

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var activityResultLauncher: ActivityResultLauncher<Intent>? = null
    private val calendarEventsAdapter = CalendarEventsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)

        initActivityLauncher()
        initRecyclerViewLists()
        initListeners()

        setContentView(binding.root)
    }

    private fun initListeners() {
        binding.btnGetData.setOnClickListener{
            loadCalendarEventsData()
        }
    }


    private fun initRecyclerViewLists() {
        binding.calendarEventsRw.layoutManager = LinearLayoutManager(this)
        binding.calendarEventsRw.adapter = calendarEventsAdapter
    }

    private fun initActivityLauncher() {
        activityResultLauncher =registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val calendarEventsList = result.data
                        ?.getParcelableArrayExtra(CalendarEventsLoadActivity.CALENDAR_EVENTS_ID)
                        ?.map {it as CalendarEventInfo }
                        ?.toList()

                    if(calendarEventsList == null) {
                        displayError(getString(R.string.error_bad_data))
                        return@registerForActivityResult
                    }

                    fillCalendarEventsRw(calendarEventsList)
                }
                else {
                    displayError(getString(R.string.error_cant_load))
                }
            }

    }

    private fun displayError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }


    private fun loadCalendarEventsData() {
        val intent = Intent(this, CalendarEventsLoadActivity::class.java)
        activityResultLauncher?.launch(intent)
    }

    private fun fillCalendarEventsRw(calendarEvents: List<CalendarEventInfo>) {
        calendarEventsAdapter.setList(calendarEvents)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val calendarEventsListState = binding.calendarEventsRw.layoutManager?.onSaveInstanceState()
        outState.putParcelable(CALENDAR_EVENTS_LIST_STATE, calendarEventsListState)

        outState.putParcelableArray(
            CALENDAR_EVENTS_LIST_DATA, calendarEventsAdapter.mDataList.toTypedArray()
        )
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        val calendarEventsListState: Parcelable? = savedInstanceState
            .getParcelable(CALENDAR_EVENTS_LIST_STATE)

        val calendarEventsListDataSaved = savedInstanceState
            .getParcelableArray(CALENDAR_EVENTS_LIST_DATA)
            ?.map {it as CalendarEventInfo }
            ?.toList() ?: listOf()

        if (calendarEventsListState != null && calendarEventsListDataSaved.isNotEmpty()) {
            binding.calendarEventsRw.layoutManager?.onRestoreInstanceState(calendarEventsListState)
            fillCalendarEventsRw(calendarEventsListDataSaved)
        }

        super.onRestoreInstanceState(savedInstanceState)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
