package com.example.mycalculator.presentation

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.mycalculator.R
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testCalculationsTextView_expectCorrectInputValues() {
        //Arrange
        val input = "5+5"

        //Act
        for (c in input) {
            onView(withText(c.toString())).perform(click())
        }

        //Assert
        onView(withId(R.id.txt_calculations)).check(matches(withText(input)))
    }

    @Test
    fun testResultTextView_expectCorrectResult() {
        //Arrange
        val input = "5+5"
        val output = "10.0"

        //Act
        for (c in input) {
            onView(withText(c.toString())).perform(click())
        }

        //Assert
        onView(withId(R.id.txt_result)).check(matches(withText(output)))
    }

    @Test
    fun testBackBtn_expectClearsCalculationsText() {
        //Arrange
        val input = "5+5"
        val output = "5"

        //Act
        for (c in input) {
            onView(withText(c.toString())).perform(click())
        }

        onView(withId(R.id.back_btn)).apply {
            perform(click())
            perform(click())
        }

        //Assert
        onView(withId(R.id.txt_calculations)).check(matches(withText(output)))
    }

    @Test
    fun testClearBtn_expectClearsCalculationsText() {
        //Arrange
        val input = "5+5"

        //Act
        for (c in input) {
            onView(withText(c.toString())).perform(click())
        }

        onView(withText("C")).perform(click())

        //Assert
        onView(withId(R.id.txt_calculations)).check(matches(withText("")))
    }

    @Test
    fun testAllClearBtn_expectClearsCalculationsText() {
        //Arrange
        val input = "5+5"

        //Act
        for (c in input) {
            onView(withText(c.toString())).perform(click())
        }

        onView(withText("AC")).perform(click())

        //Assert
        onView(withId(R.id.txt_calculations)).check(matches(withText("")))
        onView(withId(R.id.txt_result)).check(matches(withText("")))
    }
}