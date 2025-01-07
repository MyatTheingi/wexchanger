package com.myattheingi.wexchanger.core.domain.usecases

import com.myattheingi.wexchanger.core.domain.repositories.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ConvertCurrencyToSingle @Inject constructor(
    private val repo: CurrencyRepository
) {
    suspend operator fun invoke(from: String, to: String, amount: Double): Flow<Result<Double>> {
        return repo.convertCurrencyToSingle(from, to, amount)
    }
}
