package com.example.mycalculator.data.preferences

import android.content.Context
import android.content.SharedPreferences

class PreferenceProvider(val context: Context) {
    private val applicationContext = context.applicationContext
    private var sharedPreferences: SharedPreferences =
        applicationContext.getSharedPreferences("my_calculator_pref", Context.MODE_PRIVATE)

    fun getSelectedColor(): String? {
        return sharedPreferences.getString(SELECTED_COLOR, null)
    }

    fun saveSelectedColor(color: String) {
        sharedPreferences.edit().putString(SELECTED_COLOR, color).apply()
    }

    companion object {
        private const val SELECTED_COLOR = "selected_color"
    }
}