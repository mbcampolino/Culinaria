package marcoscampos.culinaria.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import marcoscampos.culinaria.pojos.PageResult;

/**
 * Created by Marcos on 29/10/2017.
 */

public class Utils {

    private static final String URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    public static ArrayList<PageResult> getPageResult() {
        URL url;
        HttpURLConnection conn;
        BufferedReader rd;
        String line;

        StringBuilder result = new StringBuilder();
        ArrayList<PageResult> recipe = new ArrayList<>();

        try {
            url = new URL(URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();

            Gson gson = new Gson();
            recipe.addAll(gson.<Collection<? extends PageResult>>fromJson(result.toString(),new TypeToken<List<PageResult>>(){}.getType()));
            return recipe;


        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            if(parent.getChildLayoutPosition(view) == 0){
                outRect.top = space;
            }

            outRect.left = space;
            outRect.right = space;

        }
    }

}
