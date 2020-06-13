/*
 * Copyright 2020 Jorge Ávila
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.jorgeav.laspeliculas.background

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.*
import com.jorgeav.core.interactors.RefreshListUseCase
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

class RefreshListCoroutineWorker @WorkerInject constructor(
    private val refreshListUseCase: RefreshListUseCase,
    @Assisted context: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(context, params) {

    companion object {
        private const val MIN_BACKOFF_MILLIS = 60000L
        private const val LIST_ID_KEY = "LIST_ID_KEY"
        private const val DEFAULT_LIST_ID = 105937

        fun setupBackgroundWork(context: Context) {
            // Constraints
            val constraints = Constraints.Builder()
                    // todo what if not connected?
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .setRequiresCharging(true)
                .build()

            // Input data
            val listIdWorkData = workDataOf(LIST_ID_KEY to DEFAULT_LIST_ID)

            // RequestWork
            val refreshListWorkRequest = OneTimeWorkRequestBuilder<RefreshListCoroutineWorker>()
                .setConstraints(constraints)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS)
                .setInputData(listIdWorkData)
                .build()

            // Schedule Work
            WorkManager.getInstance(context).enqueueUniqueWork(
                "RefreshListCoroutineWorker",
                ExistingWorkPolicy.KEEP,
                refreshListWorkRequest
            )
        }
    }

    override suspend fun doWork(): Result =
        coroutineScope {

            val listID = inputData.getInt(LIST_ID_KEY, -1)

            if (listID != -1) refreshListUseCase(listID)
            else Result.failure()

            Result.success()

            // todo check 'result' and returns Result.failure() OR Result.retry()
        }
}