package com.example.searsnotes;

import android.content.Intent;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.ActivityTestRule;

import com.example.searsnotes.ui.EditNoteActivity;
import com.example.searsnotes.ui.ViewNoteActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;


import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class EditNoteActivityTest {

    @Rule
    public ActivityTestRule<EditNoteActivity> activityTestRule = new ActivityTestRule<>(EditNoteActivity.class,true,false);
    @Rule
    public IntentsTestRule<ViewNoteActivity> intentsTestRule = new IntentsTestRule<>(ViewNoteActivity.class);

    @Test
    public void testDiscardbtn(){
        Espresso.onView(withId(R.id.discard_btn)).perform(click());
    }

    @Test
    public void testSaveBtn(){
        Espresso.onView(withId(R.id.note_title)).perform().check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.note_text)).perform().check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.note_image)).perform().check(matches(isDisplayed()));
        String testTitle ="Title123";
        String testText ="Text123";
        Espresso.onView(withId(R.id.note_text)).perform(typeText(testText));
        Espresso.onView(withId(R.id.note_title)).perform(typeText(testTitle));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.save_btn)).perform(click());

    }

    @Before
    public void setUp() throws Exception {
        Intent intent = new Intent();
        intent.putExtra("id",1);
        activityTestRule.launchActivity(intent);
    }

}