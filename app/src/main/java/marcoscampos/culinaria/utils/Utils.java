package marcoscampos.culinaria.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import marcoscampos.culinaria.pojos.PageResult;
import marcoscampos.culinaria.pojos.Steps;

/**
 * Created by Marcos on 29/10/2017.
 */

public class Utils {

    public static ArrayList<PageResult> getPageResult() {
        URL url;
        HttpURLConnection conn;
        BufferedReader rd;
        String line;

        StringBuilder result = new StringBuilder();
        ArrayList<PageResult> recipe = new ArrayList<>();

        try {
            url = new URL(Constants.URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();

            Gson gson = new Gson();
            recipe.addAll(gson.<Collection<? extends PageResult>>fromJson(result.toString(), new TypeToken<List<PageResult>>() {
            }.getType()));
            return recipe;


        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getThumbnailFromRecipe(PageResult recipe) {

        if (!recipe.getImage().isEmpty()) { // priority 1
            return recipe.getImage();
        }

        for (Steps steps : new Utils.Reversed<>(recipe.getStepsList())) { // priority 2 inverse because final image
            if (!steps.getThumbnailURL().isEmpty())
                return steps.getThumbnailURL();
        }

        for (Steps steps : new Utils.Reversed<>(recipe.getStepsList())) { // priority 3 inverse because final image
            if (!steps.getVideoURL().isEmpty() && steps.getId() != 0)
                return steps.getVideoURL();
        }

        return null;
    }

    public static void noTitleBar(final TextView titleCollapsed, AppBarLayout appBarLayout) {
        titleCollapsed.setAlpha(0);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float percentage = ((float) Math.abs(verticalOffset) / appBarLayout.getTotalScrollRange());
                titleCollapsed.setAlpha(percentage);
            }
        });
    }

    public static boolean isNetworkAvailable(Context context) {
        int[] networkTypes = new int[]{ConnectivityManager.TYPE_ETHERNET,
                ConnectivityManager.TYPE_MOBILE,
                ConnectivityManager.TYPE_WIFI};
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int networkType : networkTypes) {
                NetworkInfo netInfo = cm.getNetworkInfo(networkType);
                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        public static final int ORIENTATION_LANDSCAPE = 2;
        public static final int ORIENTATION_PORTRAIT = 1;
        Activity c;
        boolean isTablet = false;
        private int space;

        public SpacesItemDecoration(int space, Activity c, boolean isTablet) {
            this.space = space;
            this.c = c;
            this.isTablet = isTablet;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            if (isTablet) {
                if (parent.getChildLayoutPosition(view) == 0 || parent.getChildLayoutPosition(view) == 1 || parent.getChildLayoutPosition(view) == 2) {
                    outRect.top = space;
                }
                return;
            }

            if (c.getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT) {
                if (parent.getChildLayoutPosition(view) == 0) {
                    outRect.top = space;
                }
            } else if (c.getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
                if (parent.getChildLayoutPosition(view) == 0 || parent.getChildLayoutPosition(view) == 1 || parent.getChildLayoutPosition(view) == 2) {
                    outRect.top = space;
                }
            }
        }
    }

    public static class Reversed<T> implements Iterable<T> {
        private final List<T> original;

        public Reversed(List<T> original) {
            this.original = original;
        }

        public Iterator<T> iterator() {
            final ListIterator<T> i = original.listIterator(original.size());

            return new Iterator<T>() {
                public boolean hasNext() {
                    return i.hasPrevious();
                }

                public T next() {
                    return i.previous();
                }

                public void remove() {
                    i.remove();
                }
            };
        }
    }

}
