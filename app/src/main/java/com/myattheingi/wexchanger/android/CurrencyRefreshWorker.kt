package com.myattheingi.wexchanger.android

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.myattheingi.wexchanger.core.domain.repositories.CurrencyRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber
import java.util.concurrent.TimeUnit

@HiltWorker
class CurrencyRefreshWorker @AssistedInject constructor(
    private val repo: CurrencyRepository,
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // Fetch and store currency exchange rates
            repo.fetchAndStoreLiveRates()
            Result.success()
        } catch (e: Exception) {
            // Log the exception and request a retry
            Timber.e(e, "Error fetching currency rates")
            Result.retry()
        }
    }

    companion object {
        private const val TAG = "currency_worker"

        fun enqueuePeriodically(workManager: WorkManager) {
            val workConstraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED) // Requires network connectivity
                .build()

            val currencyWorkRequest =
                PeriodicWorkRequestBuilder<CurrencyRefreshWorker>(
                    1440,
                    TimeUnit.MINUTES
                ) // Runs every 1440 minutes (24 hours). Change this value to adjust the frequency.
                    .setConstraints(workConstraints)
                    .setInitialDelay(
                        1,
                        TimeUnit.MINUTES
                    )
                    .build()

            // Enqueue unique work to prevent duplicate tasks
            workManager.enqueueUniquePeriodicWork(
                TAG,
                ExistingPeriodicWorkPolicy.KEEP,
                currencyWorkRequest
            )
        }
    }
}

