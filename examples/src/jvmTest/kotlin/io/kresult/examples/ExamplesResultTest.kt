package io.kresult.core

import org.junit.jupiter.api.Test

class ExamplesResultTest {

    @Test
    fun testExamples() {
        io.kresult.examples.exampleResult01.test()
        io.kresult.examples.exampleResult02.test()
    }
}