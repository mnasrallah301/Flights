package com.mn.flights

import android.content.Context
import com.mn.flights.data.network.AppResult
import com.mn.flights.data.network.NetworkManager
import com.mn.flights.data.network.models.Query
import com.mn.flights.data.network.models.QueryWrapped
import com.mn.flights.data.network.services.SkyScannerApi
import com.mn.flights.data.repositories.SearchRepositoryImpl
import com.mn.flights.domain.repositories.SearchRepository
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class SearchFlightsTest {
    // Mock the dependencies
    private val skyScannerApi: SkyScannerApi = mock(SkyScannerApi::class.java)
    private val context: Context = mock(Context::class.java)
    private val networkManager: NetworkManager = mock(NetworkManager::class.java)

    private lateinit var repository: SearchRepository

    @Before
    fun setup() {
        repository = SearchRepositoryImpl(context, skyScannerApi, networkManager)
    }

    @Test
    fun `getAutoSuggestPlaces returns error when exception thrown`() = runBlocking {
        val queryWrapped = QueryWrapped(Query(locale = "", market = ""))
        // Given
        `when`(networkManager.isOnline(context)).thenReturn(true)
        `when`(skyScannerApi.getAutoSuggestPlace(queryWrapped)).thenThrow(RuntimeException("Test exception"))

        // When
        val result = repository.getAutoSuggestPlaces("searchText").toList()

        // Then
        assertEquals(AppResult.LOADING, result[0])
        assertTrue(result[1] is AppResult.Error)
    }
}