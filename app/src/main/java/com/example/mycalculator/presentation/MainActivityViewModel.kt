package com.example.mycalculator.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivityViewModel : ViewModel() {
    private val _calculationsData = MutableLiveData<String>()
    private val _resultData = MutableLiveData<String>()

    val calculationsData: LiveData<String> get() = _calculationsData
    val resultData: LiveData<String> get() = _resultData

    var calculationError = false
    var containsDot = false;

    init {
        resetCalculator()
    }

    fun onClickButton(tag: String, text: String) {
        if (calculationError) {
//            resetCalculator()
        }

        val isLastNumeric = checkNumeric()
        if (tag != MainActivity.DIGIT_BUTTON)
            containsDot = false

        when (tag) {
            MainActivity.DIGIT_BUTTON -> {
                if (!(text == "." && containsDot)) {
                    if (text == ".") containsDot = true
                    _calculationsData.value = calculationsData.value.plus(text)
                    calculate()
                }
            }

            MainActivity.OPERATOR_BUTTON
            -> {
                if (isLastNumeric)
                    _calculationsData.value = calculationsData.value.plus(text)
                else if (calculationsData.value!!.isEmpty() && resultData.value!!.isNotEmpty())
                    _calculationsData.value = resultData.value.plus(text)
                else
                    _calculationsData.value = calculationsData.value!!.dropLast(1) + text
            }

            MainActivity.BACK_BUTTON -> {
                _calculationsData.value = calculationsData.value?.dropLast(1)
                calculate()
            }

            MainActivity.CLEAR_BUTTON -> {
                calculate()
                _calculationsData.value = ""
            }

            MainActivity.ALL_CLEAR_BUTTON -> {
                resetCalculator()
            }

            MainActivity.EQUAL_BUTTON -> {
                calculate()
                _calculationsData.value = ""
            }

        }

    }

    private fun calculate() {
        try {
            var dataToCalculate = calculationsData.value
            if (!checkNumeric())
                dataToCalculate = dataToCalculate?.dropLast(1)

            if (dataToCalculate.isNullOrEmpty())
                dataToCalculate = "0"

            _resultData.value = ExpressionBuilder(dataToCalculate).build().evaluate().toString()
            calculationError = false
        } catch (e: Exception) {
            var errorMsg = e.localizedMessage
            Log.d("TAG", "calculate: Error: $errorMsg")

            if (errorMsg.isNullOrEmpty() || errorMsg.length > 30)
                errorMsg = "Invalid Input!"

            _resultData.value = "Error: $errorMsg"
            calculationError = true
        }
    }

    private fun resetCalculator() {
        _calculationsData.value = ""
        _resultData.value = ""
    }

    private fun checkNumeric(): Boolean {
        if (calculationsData.value.isNullOrEmpty()) return false
        val lastChar: Char? = calculationsData.value?.last()
        return lastChar!!.isDigit()
    }
}