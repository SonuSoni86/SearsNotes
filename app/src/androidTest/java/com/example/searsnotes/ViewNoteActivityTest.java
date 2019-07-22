package com.example.searsnotes;

import android.content.Intent;
import android.view.View;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class ViewNoteActivityTest {


    @Rule
    public ActivityTestRule<ViewNoteActivity> activityTestRule = new ActivityTestRule<>(ViewNoteActivity.class, true, false);

    @Rule
    public IntentsTestRule<EditNoteActivity> intentsTestRule  = new IntentsTestRule<>(EditNoteActivity.class);

    @Test
    public void checkViewNote() {
        Espresso.onView(withId(R.id.note_title)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.note_text)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.note_image)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.discard_btn)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.delete_btn)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.edit_btn)).check(matches(isDisplayed()));
    }


    @Test
    public void testDiscardBtn() {
        Espresso.onView(withId(R.id.discard_btn)).perform(click());
    }

    @Test
    public void testEditBtn()
    {
        Espresso.onView(withId(R.id.edit_btn)).perform(click());
        intended(hasComponent(EditNoteActivity.class.getName()));

    }

    @Before
    public void setUp() throws Exception {
        Intent intent = new Intent();
        intent.putExtra("id", 1);
        activityTestRule.launchActivity(intent);

    }

    @After
    public void tearDown() throws Exception {
    }
}