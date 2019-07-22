package com.example.searsnotes;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class AddNoteActivityTest {
    @Rule
    public ActivityTestRule<AddNoteActivity> activityTestRule = new ActivityTestRule<AddNoteActivity>(AddNoteActivity.class);

    private String title = "test title";
    private String text = "test Text";

    @Test
    public void testSaveButton() {
        Espresso.onView(withId(R.id.note_title)).perform(typeText(title));
        Espresso.onView(withId(R.id.note_text)).perform(typeText(text));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.save_btn)).perform(click());
        Espresso.onView(withId(R.id.notes_view)).check(matches(isDisplayed()));
    }

    @Test
    public void testDiscardButton(){
        Espresso.onView(withId(R.id.discard_btn)).perform(click());
    }

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }
}