package marcoscampos.culinaria.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import marcoscampos.culinaria.pojos.Ingredient;
import marcoscampos.culinaria.pojos.PageResult;
import marcoscampos.culinaria.pojos.Steps;

/**
 * Created by Marcos on 29/10/2017.
 */

public class Utils {

    private static final String URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    public static List<PageResult> getPageResult() {
        URL url;
        HttpURLConnection conn;
        BufferedReader rd;
        String line;
        StringBuilder result = new StringBuilder();

        List<PageResult> recipe = new ArrayList<>();
        List<Ingredient> ingredients = new ArrayList<>();
        List<Steps> steps = new ArrayList<>();

        try {
            url = new URL(URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();

            try {
                JSONArray recipeResult = new JSONArray(result.toString());

                for (int i = 0; i < recipeResult.length(); i++) {
                    JSONObject receiveJson = recipeResult.getJSONObject(i);
                    int idRecipe = receiveJson.getInt("id");
                    String nameRecipe = receiveJson.getString("name");
                    int servingsRecipe = receiveJson.getInt("servings");
                    String imageRecipe = receiveJson.getString("image");

                    JSONArray ingredientResult = receiveJson.getJSONArray("ingredients");
                    JSONArray stepsResult = receiveJson.getJSONArray("steps");

//                    for (int x = 0; i < ingredientResult.length(); x++) {
//                        JSONObject ingredientJson = ingredientResult.getJSONObject(x);
//                        int quantityIngredient = ingredientJson.getInt("quantity");
//                        String measureIngredient = ingredientJson.getString("measure");
//                        String ingredientIngredient = ingredientJson.getString("ingredient");
//                        ingredients.add(new Ingredient(quantityIngredient, measureIngredient, ingredientIngredient));
//                    }
//
//                    for (int z = 0; i < stepsResult.length(); z++) {
//                        JSONObject stepJson = stepsResult.getJSONObject(z);
//                        int idStep = stepJson.getInt("id");
//                        String shortDescriptionStep = stepJson.getString("shortDescription");
//                        String descriptionStep = stepJson.getString("description");
//                        String videoURLStep = stepJson.getString("videoURL");
//                        String thumbnailURLStep = stepJson.getString("thumbnailURL");
//                        steps.add(new Steps(idStep, shortDescriptionStep, descriptionStep, videoURLStep, thumbnailURLStep));
//                    }

                    recipe.add(new PageResult(idRecipe, nameRecipe, ingredients, steps, servingsRecipe, imageRecipe));
                }

                return recipe;

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            if(parent.getChildLayoutPosition(view) == 0){
                outRect.top = space;
            }

            outRect.left = space;
            outRect.right = space;

        }
    }

}
