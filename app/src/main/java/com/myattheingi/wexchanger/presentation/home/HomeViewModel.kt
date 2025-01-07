package com.myattheingi.wexchanger.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myattheingi.wexchanger.core.domain.models.CurrencyExchange
import com.myattheingi.wexchanger.core.domain.models.CurrencyInfo
import com.myattheingi.wexchanger.core.domain.usecases.GetCurrencyList
import com.myattheingi.wexchanger.core.domain.usecases.ConvertCurrencyToAll
import com.myattheingi.wexchanger.core.utils.toNullableDouble
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
class HomeViewModel @Inject constructor(
    private val getCurrencyList: GetCurrencyList,
    private val convertCurrency: ConvertCurrencyToAll,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<Event>(extraBufferCapacity = 1)
    val event: SharedFlow<Event> = _event.asSharedFlow()

    init {
        fetchCurrencyList()
    }

    private fun fetchCurrencyList() {
        viewModelScope.launch {
            getCurrencyList()
                .onStart { updateUiState(isLoading = true) }
                .onCompletion { updateUiState(isLoading = false) }
                .collect { result ->
                    result.onSuccess { data ->
                        updateUiState(
                            currencies = data,
                            fromCurrencyInfo = data.find { it.code == "MMK" },
                        )
                        calculateExchangeRate()
                    }
                    result.onFailure { exception ->
                        _event.emit(Event.Failure(exception))
                    }
                }
        }
    }

    private fun updateUiState(
        currencies: List<CurrencyInfo>? = null,
        fromCurrencyInfo: CurrencyInfo? = null,
        fromAmount: Double? = null,
        exchangeRates: List<CurrencyExchange>? = null,
        isLoading: Boolean? = null,
    ) {

        val isCalculateEnabled =
            (fromAmount ?: this.uiState.value.fromAmount)?.let { it > 0.0 } ?: false

        _uiState.update { currentState ->
            currentState.copy(
                currencies = currencies ?: currentState.currencies,
                fromCurrencyInfo = fromCurrencyInfo ?: currentState.fromCurrencyInfo,
                fromAmount = fromAmount ?: currentState.fromAmount,
                exchangeRates = exchangeRates ?: currentState.exchangeRates,
                isLoading = isLoading ?: currentState.isLoading,
                isCalculateEnabled = isCalculateEnabled,
            )
        }
    }

    fun onFromCurrencyInfoSelected(info: CurrencyInfo) {
        updateUiState(fromCurrencyInfo = info)
    }

    fun onAmountEntered(amount: String?) {
        _uiState.update { it.copy(fromAmount = amount?.toNullableDouble()) }
    }

    fun calculateExchangeRate() {
        val data = uiState.value
        viewModelScope.launch {
            convertCurrency(
                data.fromCurrencyInfo.code,
                data.fromAmount ?: 1.0
            )
                .onStart { updateUiState(isLoading = true) }
                .onCompletion { updateUiState(isLoading = false) }
                .collect { result ->
                    result.onSuccess { exchangeRates ->
                        updateUiState(exchangeRates = exchangeRates)
                    }
                    result.onFailure { exception ->
                        _event.emit(Event.Failure(exception))
                    }
                }
        }
    }

    // UI Events
    sealed class Event {
        data class Failure(val throwable: Throwable) : Event()
        data class Success(val message: String) : Event()
    }
}
