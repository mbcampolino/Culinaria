package marcoscampos.culinaria.utils;

import android.content.Context;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import marcoscampos.culinaria.pojos.Ingredient;
import marcoscampos.culinaria.pojos.PageResult;
import marcoscampos.culinaria.pojos.Steps;

/**
 * Created by Marcos on 25/11/2017.
 */

public class TestUtils {

    public static List<PageResult> mockResult(Context c) throws IOException {

        InputStream is = InstrumentationRegistry.getTargetContext().getAssets().open("baking.json");//c.getClassLoader().getResourceAsStream("assets/baking.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        String json = sb.toString();
        List<PageResult> reciper = new Gson().fromJson(json, new TypeToken<List<PageResult>>(){}.getType());
        return reciper;
    }

    public static void delay(int millis) {
        SystemClock.sleep(millis);
    }

}
