package marcoscampos.culinaria.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marcos on 29/10/2017.
 */

public class PageResult implements Parcelable {

    public static final Creator<PageResult> CREATOR = new Creator<PageResult>() {
        @Override
        public PageResult createFromParcel(Parcel in) {
            return new PageResult(in);
        }

        @Override
        public PageResult[] newArray(int size) {
            return new PageResult[size];
        }
    };
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("ingredients")
    private ArrayList<Ingredient> ingredientsList;
    @SerializedName("steps")
    private ArrayList<Steps> stepsList;
    @SerializedName("servings")
    private int servings;
    @SerializedName("image")
    private String image;

    private int widget;

    public PageResult() {

    }

    protected PageResult(Parcel in) {
        id = in.readInt();
        name = in.readString();
        ingredientsList = in.createTypedArrayList(Ingredient.CREATOR);
        stepsList = in.createTypedArrayList(Steps.CREATOR);
        servings = in.readInt();
        image = in.readString();
        widget = in.readInt();
    }

    public boolean isWidget() {
        return this.widget == 1;
    }

    public void setWidget(int i) {
        this.widget = i;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredientsList() {
        return ingredientsList;
    }

    public void setIngredientsList(ArrayList<Ingredient> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    public List<Steps> getStepsList() {
        return stepsList;
    }

    public void setStepsList(ArrayList<Steps> stepsList) {
        this.stepsList = stepsList;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideoThumbnail() {
        return stepsList.get(0).getVideoURL();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeTypedList(ingredientsList);
        parcel.writeTypedList(stepsList);
        parcel.writeInt(servings);
        parcel.writeString(image);
        parcel.writeInt(widget);
    }
}
