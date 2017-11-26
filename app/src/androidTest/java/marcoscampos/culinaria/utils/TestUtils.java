package marcoscampos.culinaria.utils;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;

import org.hamcrest.Matcher;

import java.util.ArrayList;

import marcoscampos.culinaria.pojos.Ingredient;
import marcoscampos.culinaria.pojos.PageResult;
import marcoscampos.culinaria.pojos.Steps;

import static android.support.test.espresso.matcher.ViewMatchers.isRoot;

/**
 * Created by Marcos on 25/11/2017.
 */

public class TestUtils {

    public static ViewAction waitFor(final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "Wait for " + millis + " milliseconds.";
            }

            @Override
            public void perform(UiController uiController, final View view) {
                uiController.loopMainThreadForAtLeast(millis);
            }
        };
    }

    public static PageResult mockResult() {
        PageResult reciper = new PageResult();
        reciper.setId(0);
        reciper.setName("TESTE");
        reciper.setImage("http://teste.com");
        reciper.setServings(8);

        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("150","G","INGREDIENT 1"));
        ingredients.add(new Ingredient("1","CUP","INGREDIENT 2"));
        ingredients.add(new Ingredient("5","CUP","INGREDIENT 3"));
        ingredients.add(new Ingredient("200","G","INGREDIENT 4"));
        reciper.setIngredientsList(ingredients);

        ArrayList<Steps> steps = new ArrayList<>();
        steps.add(new Steps(0,"short description 1","description 1","",""));
        steps.add(new Steps(1,"short description 2","description 2","teste","teste"));
        steps.add(new Steps(2,"short description 3","description 3","",""));
        steps.add(new Steps(3,"short description 4","description 4","teste",""));
        reciper.setStepsList(steps);

        return reciper;
    }

}
