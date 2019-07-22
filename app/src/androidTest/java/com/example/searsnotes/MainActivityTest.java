package com.example.searsnotes;

import android.content.Intent;
import android.view.View;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.init;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.release;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);
    @Rule
    public IntentsTestRule<AddNoteActivity> intentsTestRule = new IntentsTestRule<AddNoteActivity>(AddNoteActivity.class);
    @Rule
    public IntentsTestRule<ViewNoteActivity> intentTestRule  = new IntentsTestRule<>(ViewNoteActivity.class);

    private String title = "Title123";
    private String text = "Note Text123";
   // private int noOfNotes = activityTestRule.getActivity().numberOfNotes;

    @Test
    public void testAddButton(){
        Espresso.onView(withId(R.id.add_note_btn)).perform(click());
        intended(hasComponent(AddNoteActivity.class.getName()));
        Espresso.onView(withId(R.id.note_title)).perform(typeText(title));
        Espresso.onView(withId(R.id.note_text)).perform(typeText(text));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.save_btn)).perform(click());
        Espresso.onView(withId(R.id.notes_view)).check(matches(isDisplayed()));

    }
    @Test
    public void checkViewNote(){
        Espresso.onView(withId(R.id.notes_view)).perform(click());
        intended(hasComponent(ViewNoteActivity.class.getName()));
    }


    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
}