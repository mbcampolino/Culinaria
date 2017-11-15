package marcoscampos.culinaria.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import marcoscampos.culinaria.R;

/**
 * Created by Marcos on 15/11/2017.
 */

public class ReciperWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
            for(int appWidgetId : appWidgetIds) {
                RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                        R.layout.layout_widget);
                appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
            }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }
}
