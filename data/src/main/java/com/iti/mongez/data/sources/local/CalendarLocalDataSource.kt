package com.iti.mongez.data.sources.local

import android.content.ContentResolver
import android.content.Context
import android.provider.CalendarContract
import android.util.Log
import com.iti.mongez.domain.calendar.model.CalendarEvent
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject

class CalendarLocalDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun fetchLocalEvents(): List<CalendarEvent> {
        val events = mutableListOf<CalendarEvent>()
        val contentResolver: ContentResolver = context.contentResolver

        val projection = arrayOf(
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND,
            CalendarContract.Events.EVENT_LOCATION,
            CalendarContract.Events.ALL_DAY
        )

        // Fetch events from 30 days ago to 90 days ahead
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -30)
        val startMillis = calendar.timeInMillis
        
        calendar.add(Calendar.DAY_OF_YEAR, 120) // 30 + 90
        val endMillis = calendar.timeInMillis

        val selection = "(${CalendarContract.Events.DTSTART} >= ?) AND (${CalendarContract.Events.DTSTART} <= ?)"
        val selectionArgs = arrayOf(startMillis.toString(), endMillis.toString())

        try {
            Log.d("CalendarSync", "Fetching events from $startMillis to $endMillis")
            contentResolver.query(
                CalendarContract.Events.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
            )?.use { cursor ->
                val titleIdx = cursor.getColumnIndex(CalendarContract.Events.TITLE)
                val startIdx = cursor.getColumnIndex(CalendarContract.Events.DTSTART)
                val endIdx = cursor.getColumnIndex(CalendarContract.Events.DTEND)
                val locationIdx = cursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION)
                val allDayIdx = cursor.getColumnIndex(CalendarContract.Events.ALL_DAY)

                while (cursor.moveToNext()) {
                    events.add(
                        CalendarEvent(
                            title = cursor.getString(titleIdx) ?: "Untitled Event",
                            startTimeMillis = cursor.getLong(startIdx),
                            endTimeMillis = cursor.getLong(endIdx),
                            location = cursor.getString(locationIdx),
                            isAllDay = cursor.getInt(allDayIdx) == 1
                        )
                    )
                }
            }
        } catch (e: SecurityException) {
            Log.e("CalendarSync", "Permission denied for calendar access", e)
        }

        Log.d("CalendarSync", "Total events found: ${events.size}")
        return events
    }
}
