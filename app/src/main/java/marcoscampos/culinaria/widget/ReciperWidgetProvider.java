package marcoscampos.culinaria.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import marcoscampos.culinaria.R;

/**
 * Created by Marcos on 15/11/2017.
 */

public class ReciperWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.layout_widget);
        views.setRemoteAdapter(R.id.list_item, new Intent(context, WidgetService.class));
        views.setEmptyView(R.id.list_item, R.drawable.recipeplaceholder);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
