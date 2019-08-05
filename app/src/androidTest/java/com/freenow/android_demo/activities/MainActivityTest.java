package com.freenow.android_demo.activities;

import android.app.Activity;
import android.os.IBinder;
import android.util.Log;
import androidx.test.espresso.Root;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;
import com.freenow.android_demo.R;
import com.freenow.android_demo.models.Driver;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.Collection;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MainActivityTest {

    private  final String userName= "crazydog335";
    private  final String password = "venture";
//  private final String incorrectUsername = "crazydog5";
//  private  final String  incorrectPassword = "ventu";
    private final String searchField = "sa";
    ViewInteraction edtUsernameView = onView(withId(R.id.edt_username));
    ViewInteraction edtPasswordView = onView(withId(R.id.edt_password));
    ViewInteraction clickLoginBtn = onView(withId(R.id.btn_login));


    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    //Login in to Free Now
    @Test
    public void Test_01loginFreeNow() {
        Log.e("@Test_logs","Performing login");
        edtUsernameView
                .perform(typeText(userName), closeSoftKeyboard()); //Enter username
        Log.e("@Test_logs","username is entered");
        edtPasswordView
                .perform(typeText(password), closeSoftKeyboard()); //Enter password
        Log.e("@Test_logs","password is entered");
        clickLoginBtn.perform(click()); //Click on login button
        Log.e("@Test_logs","Login button is clicked");
    }
    // Calling Driver
    @Test
    public void Test_02callDriver()  {
        Log.e("@Test_logs","Search string entered");
        onView(withId(R.id.textSearch))
                .perform(typeText(searchField), closeSoftKeyboard()); //Enter sa in search field
        checkAdapterItem();//Fetching element of adapterview and click on driver by name
        Log.e("@Test_logs","Desired driver is selected");
        onView(withId(R.id.fab)).perform(click()); //Click on call button
        Log.e("@Test_logs","call button clicked");
    }
    // Click on the Adapter Item, Driver Named 'Sarah Scott'
    public void checkAdapterItem() {

        onData(allOf(instanceOf(Driver.class), withContent("Sarah Scott"))).inRoot(RootMatchers.withDecorView(not(is(getActivityInstance().getWindow().getDecorView())))).perform(ViewActions.click());
    }

    public static Matcher<Object> withContent(final String title) {
        return new BoundedMatcher<Object, Driver>(Driver.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("with title '" + title + "'");
            }

            @Override
            public boolean matchesSafely(Driver myObj) {
                return myObj.getName().equals(title);
            }
        };
    }
    // Getting current activity instance
    public Activity getActivityInstance() {
        final Activity[] currentActivity = {null};
        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                Collection<Activity> resumedActivities =
                        ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
                for (Activity activity : resumedActivities) {
                    Log.d("Your current activity: ", activity.getClass().getName());
                    currentActivity[0] = activity;
                    break;
                }
            }
        });

        return currentActivity[0];
    }
    //Handling Toast
    public class ToastMatcher extends TypeSafeMatcher<Root> {

        @Override
        public boolean matchesSafely(Root root) {
            int type = root.getWindowLayoutParams().get().type;
            if ((type == 1)) {
                IBinder windowToken = root.getDecorView().getWindowToken();
                IBinder appToken = root.getDecorView().getApplicationWindowToken();
                if (windowToken == appToken) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void describeTo(org.hamcrest.Description description) {
        }
    }
    //    @Test
//    public void failureLogin() throws InterruptedException {
//        edtUsernameView
//                .perform(typeText(incorrectUsername));
//        edtPasswordView
//                .perform(typeText(incorrectPassword));
//        clickLoginBtn.perform(click());
//        Thread.sleep(2500);
//        onView(withText("Login failed")).inRoot(new ToastMatcher())
//                .check(matches(isDisplayed()));
//        edtUsernameView.perform(clearText());
//        edtPasswordView.perform(clearText());
//
//    }

}
