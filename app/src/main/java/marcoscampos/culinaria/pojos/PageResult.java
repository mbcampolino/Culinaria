package marcoscampos.culinaria.pojos;

import java.util.List;

/**
 * Created by Marcos on 29/10/2017.
 */

public class PageResult {

    private int id;
    private String name;
    private List<Ingredient> ingredientsList;
    private List<Steps> stepsList;
    private int servings;
    private String image;

    public PageResult(int id, String name, List<Ingredient> ingredientsList, List<Steps> stepsList, int servings, String image) {
        this.id = id;
        this.name = name;
        this.ingredientsList = ingredientsList;
        this.stepsList = stepsList;
        this.servings = servings;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredientsList() {
        return ingredientsList;
    }

    public List<Steps> getStepsList() {
        return stepsList;
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }

    public String getVideoThumbnail() {
        return stepsList.get(0).getThumbnailURL();
    }
}
