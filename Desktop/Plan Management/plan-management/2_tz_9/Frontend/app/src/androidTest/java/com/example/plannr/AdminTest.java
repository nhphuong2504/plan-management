package com.example.plannr;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.test.core.app.ActivityScenario;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;


import androidx.lifecycle.Lifecycle;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
//import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.StringEndsWith.endsWith;

import static org.junit.Assert.*;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

@RunWith(AndroidJUnit4ClassRunner.class)
public class AdminTest {

    @Rule
    public ActivityScenarioRule<Admin_activity> activityRule = new ActivityScenarioRule<>(Admin_activity.class);

    private final String validEmail = "admin@example.com";
    private final String validPassword = "iwanttobeanadmin123";

    @Test
    public void checkInput() {
        String pswd = "iwanttobeanadmin123";
        String email = "admin@example.com";
        onView(withId(R.id.adminID))
                .perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.adminPassword))
                .perform(typeText(pswd), closeSoftKeyboard());

        onView(withId(R.id.adminPassword))
                .check(matches(withText(pswd)));
        onView(withId(R.id.adminID))
                .check(matches(withText(email)));

    }

    @Test
    public void backButton(){
        onView(withId(R.id.retreat)).perform(click());
        onView(withId(R.id.menu)).check(matches(isDisplayed()));
    }

    @Test
    public void testValidAdminVerification() {
        String pswd = "iwanttobeanadmin123";
        String email = "zrapoza@example.com";

        ActivityScenario.launch(Admin_activity.class);

        // Enter valid email and password
        onView(withId(R.id.adminID))
                .perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.adminPassword))
                .perform(typeText(pswd), closeSoftKeyboard());

        // Perform login action
        onView(withId(R.id.sleeper)).perform(click());

        // Wait for login response
        try {
            Thread.sleep(5000); // wait for 5 seconds for login response
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.adminPassword))
                .check(matches(withText(pswd)));
        onView(withId(R.id.adminID))
                .check(matches(withText(email)));

       //scenario = ActivityScenario.launch(Admin_activity.class);
        onView(withId(R.id.menu)).check(matches(isDisplayed()));
    }
}
