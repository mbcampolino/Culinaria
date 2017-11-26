package marcoscampos.culinaria;

import android.content.Context;
import android.content.Intent;
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
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static marcoscampos.culinaria.utils.TestUtils.delay;
import static org.hamcrest.Matchers.allOf;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class StepWithVideoActivityTest {

    @Rule
    public ActivityTestRule<StepWithVideoActivity_> mActivityRule =
            new ActivityTestRule<StepWithVideoActivity_>(StepWithVideoActivity_.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = getInstrumentation()
                            .getTargetContext();
                    Intent result = new Intent(targetContext, StepWithVideoActivity_.class);
                    try {
                        result.putExtra("reciper", TestUtils.mockResult(mActivityRule.getActivity()).get(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return result;
                }
            };

    @Test
    public void test_0_check_toolbar_name() {
        delay(1000);
        String title = "Intro";
        onView(allOf(withId(R.id.title_toolbar), withText(title)));
    }

    @Test
    public void test_1_check_views() {
        delay(1000);
        onView(withId(R.id.tx_instructions)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_next)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_preview)).check(matches(isDisplayed()));
    }

    @Test
    public void test_1_nexts() throws IOException {

        for (int i = 0; i < TestUtils.mockResult(mActivityRule.getActivity()).get(0).getStepsList().size(); i++) {
            delay(1000);
            onView(withId(R.id.btn_next)).check(matches(isDisplayed())).perform(click());
        }

        onView(withId(R.id.tx_instructions)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_next)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_preview)).check(matches(isDisplayed()));
    }

    @Test
    public void test_2_nexts_e_preview() throws IOException {

        for (int i = 0; i < TestUtils.mockResult(mActivityRule.getActivity()).get(0).getStepsList().size(); i++) {
            delay(1000);
            onView(withId(R.id.btn_next)).check(matches(isDisplayed())).perform(click());
        }

        for (int i = TestUtils.mockResult(mActivityRule.getActivity()).get(0).getStepsList().size(); i > 0; i--) {
            delay(1000);
            onView(withId(R.id.btn_preview)).check(matches(isDisplayed())).perform(click());
        }

        onView(withId(R.id.tx_instructions)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_next)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_preview)).check(matches(isDisplayed()));
    }
}