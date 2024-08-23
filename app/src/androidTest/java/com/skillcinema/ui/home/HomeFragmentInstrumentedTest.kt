package com.skillcinema.data

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.skillcinema.R
import com.skillcinema.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HomeFragmentInstrumentedTest {

    @get:Rule (order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule (order = 1)
    val activityTestRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun clickHomeTextView() {
        onView(withId(R.id.homeTextViewSkillCinema)).perform(click())
    }
    @Test
    fun checkHomeTextViewText(){
        onView(withId(R.id.homeTextViewSkillCinema)).check(matches(ViewMatchers.withText("Skillcinema")))
    }
    @Test
    fun checkNavViewIsEnabled(){
        onView(withId(R.id.nav_view)).check(matches(isEnabled()))
    }
}
//        activityTestRule.scenario.onActivity{
//            it.supportFragmentManager.beginTransaction().replace(R.id.nav_view, HomeFragment()).commit()
//        }