package com.example.searsnotes;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.ActivityTestRule;

import com.example.searsnotes.ui.AddNoteActivity;
import com.example.searsnotes.ui.MainActivity;


import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);
    @Rule
    public IntentsTestRule<AddNoteActivity> intentsTestRule = new IntentsTestRule<>(AddNoteActivity.class);


    @Test
    public void testAddButton() {
        String title = "Title123";
        String text = "Note Text123";
        Espresso.onView(withId(R.id.add_note_btn)).perform(click());
        intended(hasComponent(AddNoteActivity.class.getName()));
        Espresso.onView(withId(R.id.note_title)).perform(typeText(title));
        Espresso.onView(withId(R.id.note_text)).perform(typeText(text));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.save_btn)).perform(click());
        Espresso.onView(withId(R.id.notes_view)).check(matches(isDisplayed()));

    }


}