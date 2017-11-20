package marcoscampos.culinaria.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import marcoscampos.culinaria.db.ReciperContract;
import marcoscampos.culinaria.pojos.Ingredient;

/**
 * Created by Marcos on 19/11/2017.
 */

public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {
    private static final String TAG = "WidgetDataProvider";

    List<Ingredient> mCollection = new ArrayList<>();
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
        return mCollection.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews view = new RemoteViews(mContext.getPackageName(),
                android.R.layout.simple_list_item_1);
        view.setTextViewText(android.R.id.text1, mCollection.get(position).getIngredient());
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
        mCollection.clear();

        Cursor c = mContext.getContentResolver().query(ReciperContract.CONTENT_URI,
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
        Type listType = new TypeToken<List<Ingredient>>() {
        }.getType();
        mCollection = gson.fromJson(jsonIngredients, listType);
    }
}
