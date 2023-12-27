package com.example.plannr;

import androidx.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.junit.Rule;


import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasImeAction;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;


import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
//import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4ClassRunner.class)
public class MainTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);
    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule = new IntentsTestRule<>(MainActivity.class);

    @Test
    public void logOutVisible() {
        onView(withId(R.id.menu))
                .perform(click());
        onView(withId(R.id.log)).check(matches(isDisplayed()));
    }

    @Test
    public void adminVisible() {
        onView(withId(R.id.menu))
                .perform(click());
        onView(withId(R.id.admin)).check(matches(isDisplayed()));
    }

    @Test
    public void profileVisible() {
        onView(withId(R.id.menu))
                .perform(click());
        onView(withId(R.id.prof)).check(matches(isDisplayed()));
    }

    @Test
    public void logOutButton() {
        onView(withId(R.id.menu))
                .perform(click());
        onView(withId(R.id.log))
                .perform(click());
        intended(hasComponent(LoginPageActivity.class.getName()));
    }

    @Test
    public void adminButton() {
        onView(withId(R.id.menu))
                .perform(click());
        onView(withId(R.id.admin))
                .perform(click());
        intended(hasComponent(Admin_activity.class.getName()));
    }

    @Test
    public void profileButton() {
        onView(withId(R.id.menu))
                .perform(click());
        onView(withId(R.id.prof))
                .perform(click());
        intended(hasComponent(ProfileActivity.class.getName()));
    }


}

