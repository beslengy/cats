package com.molchanov.cats

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

    @get : Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun test_isBottomNavBarOnView() {
        onView(withId(R.id.bottom_navigation)).check(matches(isDisplayed()))
    }
    @Test
    fun test_isNavHostOnView(){
        onView(withId(R.id.nav_host_fragment)).check(matches(isDisplayed()))
    }
    @Test
    fun test_filterAction_visibility() {
        onView(withId(R.id.action_filter)).check(matches(isDisplayed()))
        onView(withId(R.id.favoritesFragment)).perform(click())
        onView(withId(R.id.action_filter)).check(doesNotExist())
        onView(withId(R.id.uploadedFragment)).perform(click())
        onView(withId(R.id.action_filter)).check(doesNotExist())
        onView(withId(R.id.homeFragment)).perform(click())
        onView(withId(R.id.action_filter)).check(matches(isDisplayed()))
    }
    @Test
    fun test_filterDialogOnView() {
        onView(withId(R.id.action_filter)).perform(click())
        onView(withId(R.id.ll_filter)).check(matches(isDisplayed()))
    }
}