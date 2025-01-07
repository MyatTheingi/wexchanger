package com.myattheingi.wexchanger.presentation.exchange

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myattheingi.wexchanger.core.domain.models.CurrencyInfo
import com.myattheingi.wexchanger.core.domain.usecases.ConvertCurrencyToSingle
import com.myattheingi.wexchanger.core.domain.usecases.GetCurrencyList
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
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ExchangeViewModel @Inject constructor(
    private val getCurrencyList: GetCurrencyList,
    private val convertCurrency: ConvertCurrencyToSingle,
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
                        updateUiState(currencies = data,
                            fromCurrencyInfo = data.find { it.code == "USD" },
                            toCurrencyInfo = data.find { it.code == "MMK" }
                        )
                        calculateUnitExchangeRate()
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
        toCurrencyInfo: CurrencyInfo? = null,
        fromAmount: Double? = null,
        exchangeRate: Double? = null,
        unitRateFromTo: Double? = null,
        unitRateToFrom: Double? = null,
        isLoading: Boolean? = null,
    ) {

        val isCalculateEnabled =
            (fromAmount ?: this.uiState.value.fromAmount)?.let {
                (it.toDouble()) > 0.0
            } ?: false

        _uiState.update { currentState ->
            currentState.copy(
                currencies = currencies ?: currentState.currencies,
                fromCurrencyInfo = fromCurrencyInfo ?: currentState.fromCurrencyInfo,
                toCurrencyInfo = toCurrencyInfo ?: currentState.toCurrencyInfo,
                fromAmount = fromAmount ?: currentState.fromAmount,
                exchangeRate = exchangeRate ?: currentState.exchangeRate,
                unitRateFromTo = unitRateFromTo ?: currentState.unitRateFromTo,
                unitRateToFrom = unitRateToFrom ?: currentState.unitRateToFrom,
                isLoading = isLoading ?: currentState.isLoading,
                isCalculateEnabled = isCalculateEnabled,
            )
        }
    }

    fun onFromCurrencyInfoSelected(info: CurrencyInfo) {
        updateUiState(fromCurrencyInfo = info)
        calculateUnitExchangeRate()
    }

    fun onToCurrencyInfoSelected(info: CurrencyInfo) {
        updateUiState(toCurrencyInfo = info)
        calculateUnitExchangeRate()
    }

    fun onCurrencyReverseClicked() {
        val currentState = uiState.value
        updateUiState(
            toCurrencyInfo = currentState.fromCurrencyInfo,
            fromCurrencyInfo = currentState.toCurrencyInfo
        )
        calculateUnitExchangeRate()
        calculateExchangeRate()
    }

    fun onAmountEntered(amount: String?) {
        _uiState.update { it.copy(fromAmount = amount?.toNullableDouble(), exchangeRate = 0.0) }
    }

    fun calculateExchangeRate() {
        val data = uiState.value
        viewModelScope.launch {
            convertCurrency(
                data.fromCurrencyInfo.code,
                data.toCurrencyInfo.code,
                data.fromAmount ?: 1.0
            )
                .onStart { updateUiState(isLoading = true) }
                .onCompletion { updateUiState(isLoading = false) }
                .collect { result ->
                    result.onSuccess { exchangeRate ->
                        Timber.d("Exchange Result", exchangeRate)
                        updateUiState(exchangeRate = exchangeRate)
                    }
                    result.onFailure { exception ->
                        _event.emit(Event.Failure(exception))
                    }
                }
        }
    }

    private fun calculateUnitExchangeRate() {
        val data = uiState.value
        viewModelScope.launch {
            convertCurrency(data.fromCurrencyInfo.code, data.toCurrencyInfo.code, 1.0)
                .onStart { updateUiState(isLoading = true) }
                .onCompletion { updateUiState(isLoading = false) }
                .collect { result ->
                    result.onSuccess { rate ->
                        updateUiState(
                            unitRateFromTo = rate,
                            unitRateToFrom = (1 / rate)
                        )
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
