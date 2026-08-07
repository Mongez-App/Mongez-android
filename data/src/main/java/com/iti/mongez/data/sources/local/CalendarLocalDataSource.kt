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
            CalendarContract.Instances.TITLE,
            CalendarContract.Instances.BEGIN,
            CalendarContract.Instances.END,
            CalendarContract.Instances.EVENT_LOCATION,
            CalendarContract.Instances.ALL_DAY
        )

        // Fetch events from 30 days ago to 90 days ahead
        val rangeStart = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -30) }.timeInMillis
        val rangeEnd = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 90) }.timeInMillis

        val builder = CalendarContract.Instances.CONTENT_URI.buildUpon()
        android.content.ContentUris.appendId(builder, rangeStart)
        android.content.ContentUris.appendId(builder, rangeEnd)

        try {
            Log.d("CalendarSync", "Fetching all instances from $rangeStart to $rangeEnd")
            contentResolver.query(
                builder.build(),
                projection,
                null,
                null,
                "${CalendarContract.Instances.BEGIN} ASC"
            )?.use { cursor ->
                val titleIdx = cursor.getColumnIndex(CalendarContract.Instances.TITLE)
                val startIdx = cursor.getColumnIndex(CalendarContract.Instances.BEGIN)
                val endIdx = cursor.getColumnIndex(CalendarContract.Instances.END)
                val locationIdx = cursor.getColumnIndex(CalendarContract.Instances.EVENT_LOCATION)
                val allDayIdx = cursor.getColumnIndex(CalendarContract.Instances.ALL_DAY)

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
            Log.e("CalendarSync", "Permission denied for calendar instances access", e)
        }

        Log.d("CalendarSync", "Total event instances found: ${events.size}")
        return events
    }
}
