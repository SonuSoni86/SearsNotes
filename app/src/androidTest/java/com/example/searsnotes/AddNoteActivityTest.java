package com.example.searsnotes;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;

import com.example.searsnotes.ui.AddNoteActivity;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class AddNoteActivityTest {
    @Rule
    public ActivityTestRule<AddNoteActivity> activityTestRule = new ActivityTestRule<>(AddNoteActivity.class);



    @Test
    public void testSaveButton() {
        String title = "test title";
         String text = "test Text";
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

}