package com.example.appcredibanco.domain

import com.example.appcredibanco.data.TransactionRepository
import com.example.appcredibanco.domain.model.Transaction
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetTransactionUseCaseTest {

    @RelaxedMockK
    private lateinit var repository: TransactionRepository

    private lateinit var getTransactionUseCase: GetTransactionUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getTransactionUseCase = GetTransactionUseCase(repository)
    }

    @Test
    fun getAllTransaction() = runBlocking {
        // Given
        val list = listOf(Transaction(1, "134564", "313133", "13343", "131313"))
        coEvery { repository.getAllTransactionFromDataBase() } returns flow { list }

        // When

        val response = getTransactionUseCase.getAllTransaction()

        // Then

        coVerify { getTransactionUseCase.getAllTransaction() }

        assert(response == list)
    }
}
