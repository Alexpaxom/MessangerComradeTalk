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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.alexpaxom.homework_1.R
import com.alexpaxom.homework_1.app.servises.GetCalendarDataIntentService
import com.alexpaxom.homework_1.databinding.ActivityCalendarEventsBinding

class CalendarEventsLoadActivity : AppCompatActivity() {
    private var _binding: ActivityCalendarEventsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCalendarEventsBinding.inflate(layoutInflater)

        LocalBroadcastManager.getInstance(this).registerReceiver(object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                setResult(RESULT_OK, intent);
                finish();
            }
        },
            IntentFilter(GetCalendarDataIntentService.CALENDAR_DATA_INTENT_ID)
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
        val intent = Intent(this, GetCalendarDataIntentService::class.java)
        startService(intent)
    }

    private fun errorReturnFromActivity() {
        setResult(RESULT_CANCELED, intent);
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        val CALENDAR_EVENTS_ID = GetCalendarDataIntentService.CALENDAR_EVENTS_ID
        private const val CALENDAR_PERMISSION_READ_ID = 1
    }
}