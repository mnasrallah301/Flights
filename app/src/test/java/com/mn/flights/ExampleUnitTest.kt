package com.mn.flights

import com.mn.flights.data.repositories.formatTime
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun formatTime_isCorrect() {
        val time = formatTime(8,3)
        assertEquals("08:03", time)
    }
}