package marcoscampos.culinaria;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import marcoscampos.culinaria.pojos.Ingredient;
import marcoscampos.culinaria.pojos.PageResult;
import marcoscampos.culinaria.pojos.Steps;
import marcoscampos.culinaria.utils.TestUtils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class DetailActivityTest {

    @Rule
    public ActivityTestRule<DetailsActivity_> mActivityRule = new ActivityTestRule<>(
            DetailsActivity_.class,true , false);

    @Before
    public void put_extras() {
        Intent intent = new Intent();
        intent.putExtra("reciper", TestUtils.mockResult());
        mActivityRule.launchActivity(intent);
    }

    @Test
    public void check_toolbar_name() {
        String title = TestUtils.mockResult().getName();
        onView(allOf(withId(R.id.title_toolbar), withText(title)));
    }

    @Test
    public void check_list_ingredients() {
        onView(withId(R.id.recycler_view_main_ingredient))
                .check(matches(hasDescendant(withId(R.id.item_text)))).check(matches(isDisplayed()));
    }

    @Test
    public void check_list_steps() {
//        onView(withId(R.id.recycler_view_main_step))
//                .check(matches(hasDescendant(withId(R.id.item_text_step)))).check(matches(isDisplayed()));
//        onView(withId(R.id.recycler_view_main_step))
//                .check(matches(hasDescendant(withId(R.id.item_short_description)))).check(matches(isDisplayed()));
//        onView(withId(R.id.recycler_view_main_step))
//                .check(matches(hasDescendant(withId(R.id.image_step)))).check(matches(isDisplayed()));
    }
}