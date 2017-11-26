package marcoscampos.culinaria;

import android.content.Context;
import android.content.Intent;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.IOException;

import marcoscampos.culinaria.utils.TestUtils;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static marcoscampos.culinaria.utils.TestUtils.delay;
import static org.hamcrest.Matchers.allOf;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class DetailActivityTest {

    @Rule
    public ActivityTestRule<DetailsActivity_> mActivityRule =
            new ActivityTestRule<DetailsActivity_>(DetailsActivity_.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = getInstrumentation()
                            .getTargetContext();
                    Intent result = new Intent(targetContext, DetailsActivity_.class);
                    try {
                        result.putExtra("reciper", TestUtils.mockResult(mActivityRule.getActivity()).get(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return result;
                }
            };

    @Test
    public void test_0_check_toolbar_name() throws IOException {
        delay(1000);
        String title = TestUtils.mockResult(mActivityRule.getActivity()).get(0).getName();
        onView(allOf(withId(R.id.title_toolbar), withText(title)));
    }

    @Test
    public void test_1_check_list_ingredients() {
        delay(1000);
        onView(withId(R.id.recycler_view_main_ingredient))
                .check(matches(hasDescendant(withId(R.id.item_text)))).check(matches(isDisplayed()));
    }

    @Test
    public void test_2_check_list_steps() {
        delay(1000);
        onView(withId(android.R.id.content)).perform(ViewActions.swipeUp());
        onView(withId(R.id.recycler_view_main_step))
                .check(matches(hasDescendant(withId(R.id.item_text_step)))).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view_main_step))
                .check(matches(hasDescendant(withId(R.id.item_short_description)))).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view_main_step))
                .check(matches(hasDescendant(withId(R.id.image_step)))).check(matches(isDisplayed()));
    }

    @Test
    public void test_3_addWidget() {
        delay(1000);
        onView(allOf(withId(R.id.btn_add_widget))).check(matches(isDisplayed())).perform(click());
    }

    @Test
    public void test_4_removeWidget() {
        delay(1000);
        onView(allOf(withId(R.id.btn_add_widget))).check(matches(isDisplayed())).perform(click());
    }
}