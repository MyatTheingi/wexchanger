package com.myattheingi.wexchanger.core.domain.usecases

import com.myattheingi.wexchanger.core.domain.models.CurrencyQuotes
import com.myattheingi.wexchanger.core.domain.repositories.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLiveRates @Inject constructor(
    private val repo: CurrencyRepository
) {
    suspend operator fun invoke(): Flow<Result<CurrencyQuotes>> {
        return repo.getLiveRates()
    }
}