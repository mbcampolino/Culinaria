package marcoscampos.culinaria.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Marcos on 10/10/2017.
 */

public class ReciperContract {

    public static final String AUTHORITY = "marcoscampos.culinaria";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_RECIPES = "recipes";

    public static final class ReciperEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPES).build();

        public static final String TABLE_NAME = "recipes";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SERVINGS = "servings";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_INGREDIENTS = "ingredients";
    }
}
