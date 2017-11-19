package marcoscampos.culinaria.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Marcos on 19/11/2017.
 */

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetDataProvider(this, intent);
    }
}
