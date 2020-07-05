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
import com.jorgeav.core.data.NetworkResponse
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
        private const val DEFAULT_LIST_ID = 105937999
        private const val RESULT_KEY = "RESULT_KEY"

        fun setupBackgroundWork(context: Context) {
            // Constraints
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .setRequiresCharging(true)
                .build()

            // Input data
            val listIdWorkData = workDataOf(LIST_ID_KEY to DEFAULT_LIST_ID)

            // todo change to periodic work
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

        fun getOutputData(outputData : Data) : String? {
            return outputData.getString(RESULT_KEY)
        }
    }

    override suspend fun doWork(): Result =
        coroutineScope {
            val listID = inputData.getInt(LIST_ID_KEY, -1)
            val networkResponse = refreshListUseCase(listID)
            when (networkResponse) {
                is NetworkResponse.Success ->
                    Result.success(workDataOf(RESULT_KEY to "Success"))
                is NetworkResponse.ApiError ->
                    Result.failure(workDataOf(RESULT_KEY to "ApiError"))
                is NetworkResponse.NetworkError ->
                    Result.failure(workDataOf(RESULT_KEY to "NetworkError"))
                is NetworkResponse.UnknownError ->
                    Result.failure(workDataOf(RESULT_KEY to "UnknownError"))
            }
        }
}