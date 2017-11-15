package marcoscampos.culinaria.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import marcoscampos.culinaria.R;

/**
 * Created by Marcos on 15/11/2017.
 */

public class ReciperWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;
        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.layout_widget);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }
}
