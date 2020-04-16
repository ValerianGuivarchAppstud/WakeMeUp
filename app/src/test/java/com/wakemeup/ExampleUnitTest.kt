package com.wakemeup

import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    fun getS(): String? {
        return null
    }
    @Test
    fun testPrint() {
        val test: String? = null
        var name = getS() ?: "non"
        print(name)
    }
}
