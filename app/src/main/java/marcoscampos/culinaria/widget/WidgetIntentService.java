package marcoscampos.culinaria.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import marcoscampos.culinaria.R;
import marcoscampos.culinaria.db.ReciperContract;
import marcoscampos.culinaria.pojos.Ingredient;

/**
 * Created by Marcos on 15/11/2017.
 */

public class WidgetIntentService extends IntentService {

    public WidgetIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                ReciperWidgetProvider.class));

        Cursor c = getContentResolver().query(ReciperContract.ReciperEntry.CONTENT_URI,
                null,
                ReciperContract.ReciperEntry.COLUMN_ID + ">" + 0,
                null,
                null);
        if (c == null) {
            return;
        }
        if (!c.moveToFirst()) {
            c.close();
            return;
        }

        String jsonIngredients = c.getString(4);
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Ingredient>>() {}.getType();
        List<Ingredient> list = gson.fromJson(jsonIngredients, listType);

        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.layout_widget);
        }
    }
}
