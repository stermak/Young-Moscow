package youngdevs.production.youngmoscow

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import youngdevs.production.youngmoscow.presentation.ui.activity.MainActivity


@RunWith(AndroidJUnit4::class)
class RegistrationFragmentTest {
    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun shortPassword_causesError() {
        onView(withId(R.id.registration_button))
            .perform(ViewActions.click())
        onView(withId(R.id.username_field))
            .perform(ViewActions.typeText("mirea"))
        onView(withId(R.id.email_field))
            .perform(ViewActions.typeText("mirea@gmail.com"))
        onView(withId(R.id.password_registration_field))
            .perform(ViewActions.typeText("mirea"))
        onView(withId(R.id.repeat_password_registration_field))
            .perform(ViewActions.typeText("mirea"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.registration))
            .perform(ViewActions.click())
        onView(withId(R.id.password_registration_field))
            .check(ViewAssertions.matches(hasErrorText("Пароль должен состоять из 6 и более символов")))
    }

    @Test
    fun passwordsDoNotMatch_causesError() {
        onView(withId(R.id.registration_button))
            .perform(ViewActions.click())
        onView(withId(R.id.username_field))
            .perform(ViewActions.typeText("mirea"))
        onView(withId(R.id.email_field))
            .perform(ViewActions.typeText("mirea@gmail.com"))
        onView(withId(R.id.password_registration_field))
            .perform(ViewActions.typeText("mirea1"))
        onView(withId(R.id.repeat_password_registration_field))
            .perform(ViewActions.typeText("mirea2"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.registration))
            .perform(ViewActions.click())
        onView(withId(R.id.password_registration_field))
            .check(ViewAssertions.matches(hasErrorText("Пароли не совпадают")))
    }
}