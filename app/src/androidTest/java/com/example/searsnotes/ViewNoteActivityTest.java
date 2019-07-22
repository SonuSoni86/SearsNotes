package com.example.searsnotes;

import android.content.Intent;
import android.view.View;

import androidx.test.espresso.Espresso;
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
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class ViewNoteActivityTest {


    @Rule
    public ActivityTestRule<MainActivity> activityTestRule  = new ActivityTestRule<>(MainActivity.class,true,false);

   /* @Rule
    public IntentsTestRule<ViewNoteActivity> intentTestRule  = new IntentsTestRule<>(ViewNoteActivity.class);*/


    @Test
    public void checkViewNote(){
        Intent intent = new Intent(activityTestRule.getActivity(), ViewNoteActivity.class);
        intent.putExtra("id",0);
        activityTestRule.launchActivity(intent);
    }



    @Test
    public void testDiscardBtn(){
        Espresso.onView(withId(R.id.discard_btn)).perform(click());
    }

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
    }
}