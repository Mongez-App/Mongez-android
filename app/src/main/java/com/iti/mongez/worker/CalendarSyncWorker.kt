package com.iti.mongez.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.iti.mongez.domain.calendar.usecase.SyncCalendarEventsUseCase
import com.iti.mongez.domain.calendar.usecase.UpdateCalendarSyncStatusUseCase
import com.iti.mongez.domain.settings.usecase.GetAppSettingsUseCase
import com.iti.mongez.domain.core.Result
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class CalendarSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val syncCalendarEventsUseCase: SyncCalendarEventsUseCase,
    private val updateCalendarSyncStatusUseCase: UpdateCalendarSyncStatusUseCase,
    private val getAppSettingsUseCase: GetAppSettingsUseCase
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val settings = getAppSettingsUseCase().first()
            if (!settings.isCalendarSyncEnabled) {
                return Result.success()
            }

            val syncResult = syncCalendarEventsUseCase()
            if (syncResult is com.iti.mongez.domain.core.Result.Success) {
                updateCalendarSyncStatusUseCase(connected = true, synced = true)
                Result.success()
            } else {
                Result.retry()
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
