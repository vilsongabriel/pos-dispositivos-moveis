package dev.onofre.tiptime

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.containsString
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.text.NumberFormat

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule()
    val activity = ActivityScenarioRule(MainActivity::class.java)


    @Test
    fun useAppContext() {
        onView(withId(R.id.cost_of_service))
            .perform(typeText("50"))
            .perform(ViewActions.closeSoftKeyboard())

        onView(withId(R.id.calculate_button))
            .perform(click())

        val expectedTip = NumberFormat.getCurrencyInstance().format(10).toString()

        onView(withId(R.id.tip_result))
            .check(matches(withText(containsString(expectedTip))))
    }
}