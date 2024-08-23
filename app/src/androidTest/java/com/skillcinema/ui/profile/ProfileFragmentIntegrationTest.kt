package com.skillcinema.ui.profile

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.skillcinema.R
import com.skillcinema.ui.MainActivity
import com.skillcinema.ui.search.SearchFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ProfileFragmentIntegrationTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val activityTestRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun init() {
        hiltRule.inject()
        activityTestRule.scenario.onActivity {
            it.supportFragmentManager.beginTransaction().replace(R.id.nav_view, ProfileFragment())
                .commit()
        }
    }

    @Test
    fun clickHomeTextView() {
        activityTestRule.scenario.onActivity {
            it.supportFragmentManager.beginTransaction().replace(R.id.nav_view, SearchFragment())
                .commit()
        }
        activityTestRule.scenario.onActivity {
            it.supportFragmentManager.popBackStack()
        }
    }
}
//        onData(withId(R.id.concatRecyclerView))
//            .atPosition(1)
//            .onChildView(
//                withId(R.id.collectionTextView)
//            ).check(matches(ViewMatchers.withText("Коллекции")))

//        onData(withId(R.id.collectionTextView)).check(matches(ViewMatchers.withText("Коллекции")))


//        onView(withId(R.id.concatRecyclerView)).perform(RecyclerViewActions.scrollToLastPosition<RecyclerView.ViewHolder>())