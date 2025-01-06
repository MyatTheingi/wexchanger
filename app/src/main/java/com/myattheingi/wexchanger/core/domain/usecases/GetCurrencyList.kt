package com.myattheingi.wexchanger.core.domain.usecases

import com.myattheingi.wexchanger.core.domain.models.CurrencyInfo
import com.myattheingi.wexchanger.core.domain.repositories.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrencyList @Inject constructor(
    private val repo: CurrencyRepository
) {
    suspend operator fun invoke():Flow<Result<List<CurrencyInfo>>> {
        return repo.getCurrencyList()
    }
}
