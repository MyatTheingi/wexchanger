package com.myattheingi.wexchanger.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.myattheingi.wexchanger.android.CurrencyRefreshWorker
import com.myattheingi.wexchanger.core.domain.models.CurrencyInfo
import com.myattheingi.wexchanger.core.domain.usecases.ConvertCurrencyToSingle
import com.myattheingi.wexchanger.core.domain.usecases.GetCurrencyList
import com.myattheingi.wexchanger.presentation.exchange.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    workManager: WorkManager,
) : ViewModel() {


    private val _event = MutableSharedFlow<Event>(extraBufferCapacity = 1)
    val event: SharedFlow<Event> = _event.asSharedFlow()

    // TODO : catch the workerManager status flow in later
    init {
        CurrencyRefreshWorker.enqueuePeriodically(workManager)
    }


    // UI Events
    sealed class Event {
        data class Failure(val throwable: Throwable) : Event()
        data class Success(val message: String) : Event()
    }
}
