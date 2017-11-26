package marcoscampos.culinaria;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity_> mActivityRule = new ActivityTestRule<>(
            MainActivity_.class);

    @Test
    public void check_toolbar_name() {
        String title = mActivityRule.getActivity().getString(R.string.app_name);
        onView(allOf(withId(R.id.title_toolbar), withText(title)));
    }

    @Test
    public void check_first_item() {
        onView(withId(R.id.recycler_view_main))
                .check(matches(hasDescendant(withId(R.id.image_receita)))).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view_main))
                .check(matches(hasDescendant(withId(R.id.txtitle)))).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view_main))
                .check(matches(hasDescendant(withId(R.id.txrendiment)))).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view_main))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }
}