package com.alexpaxom.homework_1.app.activities

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.alexpaxom.homework_1.R
import com.alexpaxom.homework_1.app.servises.GetCalendarDataJobIntentService
import com.alexpaxom.homework_1.databinding.ActivityCalendarEventsBinding
import androidx.core.app.JobIntentService




class CalendarEventsLoadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalendarEventsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarEventsBinding.inflate(layoutInflater)

        LocalBroadcastManager.getInstance(this).registerReceiver(object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                setResult(RESULT_OK, intent);
                finish();
            }
        },
            IntentFilter(GetCalendarDataJobIntentService.CALENDAR_DATA_INTENT_ID)
        );


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_CALENDAR
                ) == PackageManager.PERMISSION_GRANTED -> {
                    requestCalendarData()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CALENDAR) -> {
                    Toast.makeText(this, getString(R.string.error_calendar_permission), Toast.LENGTH_LONG).show()
                    finish()
                }
                else -> {
                    requestPermissions( arrayOf(Manifest.permission.READ_CALENDAR), CALENDAR_PERMISSION_READ_ID)
                }
            }
        }

        setContentView(binding.root)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            CALENDAR_PERMISSION_READ_ID -> {
                if (grantResults.isEmpty() || grantResults.first() != PackageManager.PERMISSION_GRANTED) {
                    errorReturnFromActivity()
                }

                requestCalendarData()
            }
            else -> {
                errorReturnFromActivity()
            }
        }


    }

    private fun requestCalendarData() {
        val intent = Intent(this, GetCalendarDataJobIntentService::class.java)
        JobIntentService.enqueueWork(
            this,
            GetCalendarDataJobIntentService::class.java,
            JOB_ID_SERVICE,
            intent
        )
    }

    private fun errorReturnFromActivity() {
        setResult(RESULT_CANCELED, intent);
        finish()
    }

    companion object {
        val CALENDAR_EVENTS_ID = GetCalendarDataJobIntentService.CALENDAR_EVENTS_ID
        private const val CALENDAR_PERMISSION_READ_ID = 1
        private const val JOB_ID_SERVICE = 111
    }
}