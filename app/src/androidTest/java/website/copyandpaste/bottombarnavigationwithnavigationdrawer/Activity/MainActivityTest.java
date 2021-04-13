package website.copyandpaste.bottombarnavigationwithnavigationdrawer.Activity;

import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.swing.text.View;

import website.copyandpaste.bottombarnavigationwithnavigationdrawer.R;

import static org.junit.Assert.*;

public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule <MainActivity>(MainActivity.class);
    private  MainActivity mainActivity = null;

    @Before
    public void setUp() throws Exception {
        mainActivity = mActivityTestRule.getActivity();
    }

    @Test
    public void testLunch(){
        View view = mainActivity.findViewById(R.id.mainActivityTextView);
        assertNotNull(view);
    }

    @After
    public void tearDown() throws Exception {
        mainActivity = null;
    }
}