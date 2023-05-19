package youngdevs.production.youngmoscow

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import youngdevs.production.youngmoscow.presentation.ui.activity.MainActivity
import java.util.regex.Pattern.matches


@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testButtonVisibility() {
        onView(withId(R.id.myButton)).check(matches(isDisplayed()))
    }

    @Test
    fun testButtonClick() {
        onView(withId(R.id.myButton)).perform(click())
        onView(withId(R.id.myTextView)).check(matches(withText("Button Clicked")))
    }

    @Test
    fun testEditTextInput() {
        val inputText = "Hello"
        onView(withId(R.id.myEditText)).perform(typeText(inputText), closeSoftKeyboard())
        onView(withId(R.id.myButton)).perform(click())
        onView(withId(R.id.myTextView)).check(matches(withText(inputText)))
    }

    @Test
    fun testRecyclerViewScroll() {
        onView(withId(R.id.myRecyclerView)).perform(scrollToPosition<RecyclerView.ViewHolder>(10))
        onView(withText("Item 10")).check(matches(isDisplayed()))
    }

    // Другие тесты

}
