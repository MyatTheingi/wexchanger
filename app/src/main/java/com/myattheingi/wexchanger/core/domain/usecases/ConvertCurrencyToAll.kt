package com.myattheingi.wexchanger.core.domain.usecases

import com.myattheingi.wexchanger.core.domain.models.CurrencyExchange
import com.myattheingi.wexchanger.core.domain.repositories.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ConvertCurrencyToAll @Inject constructor(
    private val repo: CurrencyRepository
) {
    suspend operator fun invoke(from: String, amount: Double): Flow<Result<List<CurrencyExchange>>> {
        return repo.convertCurrencyToAll(from, amount)
    }
}