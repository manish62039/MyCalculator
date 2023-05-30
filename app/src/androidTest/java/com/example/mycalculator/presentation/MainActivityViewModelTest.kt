package com.example.mycalculator.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainActivityViewModelTest {
    private lateinit var viewModel: MainActivityViewModel

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        viewModel = MainActivityViewModel()
    }

    @Test
    fun testEquations() {
        val equationsMap = hashMapOf(
            "5+5/5" to "6.0",
            "2+3/0" to "Error: Division by zero!",
            "0/5-15+11" to "-4.0",
            "0.01/5" to "0.002",
            "50+0.01-1500%100+200" to "250.01"
        )

        equationsMap.forEach {
            val equation = it.key
            val expectedRes = it.value
            println("Testing $equation -> $expectedRes")

            for (c in equation) {
                val type = if (c.isDigit()) MainActivity.DIGIT_BUTTON
                else
                    MainActivity.OPERATOR_BUTTON

                viewModel.onClickButton(type, c.toString())
            }

            val res = viewModel.resultData.value.toString()
            viewModel.onClickButton(MainActivity.ALL_CLEAR_BUTTON, "AC")

            assertEquals(res, expectedRes)
        }

    }

}