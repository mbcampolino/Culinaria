package marcoscampos.culinaria.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import marcoscampos.culinaria.db.ReciperContract;
import marcoscampos.culinaria.pojos.Ingredient;
import marcoscampos.culinaria.pojos.PageResult;
import marcoscampos.culinaria.pojos.Steps;

/**
 * Created by Marcos on 19/11/2017.
 */

public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {
    private static final String TAG = "WidgetDataProvider";

    //List<Ingredient> mCollection = new ArrayList<>();
    PageResult pageResult;
    Context mContext = null;
    Intent intent;

    public WidgetDataProvider(Context mContext, Intent intent) {
        this.mContext = mContext;
        this.intent = intent;
    }

    @Override
    public void onCreate() {
        initData();
    }

    @Override
    public void onDataSetChanged() {
        initData();
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return pageResult.getIngredientsList().size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews view = new RemoteViews(mContext.getPackageName(),
                android.R.layout.simple_list_item_1);
        view.setTextViewText(android.R.id.text1, String.format("%s %s %s", pageResult.getIngredientsList().get(position).getQuantity(), pageResult.getIngredientsList().get(position).getMeasure(), pageResult.getIngredientsList().get(position).getIngredient()));
        Bundle extras = new Bundle();
        extras.putParcelable("reciper", pageResult);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        view.setOnClickFillInIntent(android.R.id.text1, fillInIntent);

        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void initData() {

        pageResult = new PageResult();
        SharedPreferences preferences = mContext.getSharedPreferences("widgetpreferences", Context.MODE_PRIVATE);
        if (preferences.getString("id_widget", null) != null) {
            String idPreference = preferences.getString("id_widget", null);
            Cursor c = mContext.getContentResolver().query(ReciperContract.CONTENT_URI,
                    null,
                    ReciperContract.ReciperEntry.COLUMN_ID + "=" + idPreference,
                    null,
                    null);
            if (c == null) {
                return;
            }
            if (!c.moveToFirst()) {
                c.close();
                return;
            }

            int id = c.getInt(c.getColumnIndex(ReciperContract.ReciperEntry.COLUMN_ID));
            String image = c.getString(c.getColumnIndex(ReciperContract.ReciperEntry.COLUMN_IMAGE));
            String ingredients = c.getString(c.getColumnIndex(ReciperContract.ReciperEntry.COLUMN_INGREDIENTS));
            String name = c.getString(c.getColumnIndex(ReciperContract.ReciperEntry.COLUMN_NAME));
            int servings = c.getInt(c.getColumnIndex(ReciperContract.ReciperEntry.COLUMN_SERVINGS));
            String steps = c.getString(c.getColumnIndex(ReciperContract.ReciperEntry.COLUMN_STEPS));

            pageResult.setId(id);
            pageResult.setImage(image);
            ArrayList<Ingredient> listIngredients = new Gson().fromJson(ingredients, new TypeToken<ArrayList<Ingredient>>() {
            }.getType());
            pageResult.setIngredientsList(listIngredients);
            pageResult.setName(name);
            pageResult.setServings(servings);
            ArrayList<Steps> stepsList = new Gson().fromJson(steps, new TypeToken<ArrayList<Steps>>() {
            }.getType());
            pageResult.setStepsList(stepsList);
        }
    }
}
