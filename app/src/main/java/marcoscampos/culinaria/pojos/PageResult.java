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
    @SerializedName("ingredientsList")
    private ArrayList<Ingredient> ingredientsList;
    @SerializedName("stepsList")
    private ArrayList<Steps> stepsList;
    @SerializedName("servings")
    private int servings;
    @SerializedName("image")
    private String image;

    protected PageResult(Parcel in) {
        id = in.readInt();
        name = in.readString();
        servings = in.readInt();
        image = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(servings);
        parcel.writeString(image);
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
