package marcoscampos.culinaria;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import marcoscampos.culinaria.adapters.MainAdapter;
import marcoscampos.culinaria.db.ReciperContract;
import marcoscampos.culinaria.interfaces.OnRecyclerClick;
import marcoscampos.culinaria.pojos.Ingredient;
import marcoscampos.culinaria.pojos.PageResult;
import marcoscampos.culinaria.pojos.Steps;
import marcoscampos.culinaria.utils.Utils;

import static marcoscampos.culinaria.db.ReciperContract.ReciperEntry.COLUMN_ID;
import static marcoscampos.culinaria.db.ReciperContract.ReciperEntry.COLUMN_IMAGE;
import static marcoscampos.culinaria.db.ReciperContract.ReciperEntry.COLUMN_INGREDIENTS;
import static marcoscampos.culinaria.db.ReciperContract.ReciperEntry.COLUMN_NAME;
import static marcoscampos.culinaria.db.ReciperContract.ReciperEntry.COLUMN_SERVINGS;
import static marcoscampos.culinaria.db.ReciperContract.ReciperEntry.COLUMN_STEPS;

@SuppressLint("Registered")
@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, OnRecyclerClick {

    @ViewById(R.id.title_toolbar)
    TextView titleToolbar;
    @ViewById(R.id.toolbar)
    Toolbar toolbar;
    @ViewById(R.id.recycler_view_main)
    RecyclerView recyclerView;
    @ViewById(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;
    @ViewById(R.id.refresh)
    SwipeRefreshLayout refreshLayout;
    GridLayoutManager grid;
    List<PageResult> result = new ArrayList<>();
    MainAdapter adapter;
    RecyclerView.ItemDecoration decoration;
    boolean tabletSize;

    @AfterViews
    public void afterViews() {
        prepareToolbar();
        prepareRecyclerView();
        loadRecipes();
    }

    private void prepareToolbar() {
        setSupportActionBar(toolbar);
    }

    private void prepareRecyclerView() {
        int valueInPixels = (int) getResources().getDimension(R.dimen.margin_card);
        tabletSize = getResources().getBoolean(R.bool.isTablet);
        decoration = new Utils.SpacesItemDecoration(valueInPixels, this, tabletSize);
        recyclerView.setHasFixedSize(true);
        if (tabletSize) {
            grid = new GridLayoutManager(this, 3);
        } else {
            grid = new GridLayoutManager(this, 1);
        }

        adapter = new MainAdapter(result, this, this);
        recyclerView.setLayoutManager(grid);
        recyclerView.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(this);
        updateList();
    }

    private void updateList() {
        recyclerView.removeItemDecoration(decoration);
        recyclerView.addItemDecoration(decoration);
    }


    @SuppressLint("StaticFieldLeak")
    public void loadRecipes() {

        adapter.clear();

        if (Utils.isNetworkAvailable(this)) {
            new AsyncTask<String, String, ArrayList<PageResult>>() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    if (!refreshLayout.isRefreshing()) {
                        refreshLayout.setRefreshing(true);
                    }
                }

                @Override
                protected ArrayList<PageResult> doInBackground(String... params) {
                    return Utils.getPageResult();
                }

                @Override
                protected void onPostExecute(ArrayList<PageResult> list) {
                    super.onPostExecute(list);

                    if (refreshLayout.isRefreshing()) {
                        refreshLayout.setRefreshing(false);
                    }

                    if (list != null) {
                        adapter.add(list);
                        clearCache();
                        addCache(list);
                    } else {
                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, getString(R.string.houveerro), Snackbar.LENGTH_INDEFINITE)
                                .setAction(getString(R.string.sim), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        adapter.clear();
                                        loadRecipes();
                                    }
                                });

                        snackbar.show();
                    }
                }
            }.execute();
        } else {
            /// checa se tem cache
            Cursor c = getContentResolver().query(ReciperContract.CONTENT_URI, null, null, null, null);

            ArrayList<PageResult> listCache = new ArrayList<>();
            if (c.moveToFirst()) {
                do {
                    PageResult pageResult = new PageResult();

                    int id = c.getInt(c.getColumnIndex(ReciperContract.ReciperEntry.COLUMN_ID));
                    String image = c.getString(c.getColumnIndex(ReciperContract.ReciperEntry.COLUMN_IMAGE));
                    String ingredients = c.getString(c.getColumnIndex(ReciperContract.ReciperEntry.COLUMN_INGREDIENTS));
                    String name = c.getString(c.getColumnIndex(ReciperContract.ReciperEntry.COLUMN_NAME));
                    int servings = c.getInt(c.getColumnIndex(ReciperContract.ReciperEntry.COLUMN_SERVINGS));
                    String steps = c.getString(c.getColumnIndex(ReciperContract.ReciperEntry.COLUMN_STEPS));
                    int widget = c.getInt(c.getColumnIndex(ReciperContract.ReciperEntry.COLUMN_WIDGET));

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
                    pageResult.setWidget(widget);

                    listCache.add(pageResult);

                } while (c.moveToNext());
                c.close();
            } else {
                c.close();
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, getString(R.string.houveerrosemcache), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
            }

            adapter.add(listCache);
            if (refreshLayout.isRefreshing()) {
                refreshLayout.setRefreshing(false);
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            grid.setSpanCount(3);
            updateList();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (!tabletSize) {
                grid.setSpanCount(1);
            }

            updateList();
        }
    }

    @Override
    public void onRefresh() {
        adapter.clear();
        loadRecipes();
    }

    @Override
    public void onItemClick(PageResult item) {
        Intent intent = new Intent(this, DetailsActivity_.class);
        intent.putExtra("reciper", item);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadRecipes();
        }
    }

    private void addCache(ArrayList<PageResult> list) {
        for (PageResult reciper : list) {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_ID, reciper.getId());
            cv.put(COLUMN_NAME, reciper.getName());
            cv.put(COLUMN_SERVINGS, reciper.getServings());
            cv.put(COLUMN_IMAGE, reciper.getImage());
            String jsonIngredients = new GsonBuilder().setPrettyPrinting().create().toJson(reciper.getIngredientsList());
            String jsonSteps = new GsonBuilder().setPrettyPrinting().create().toJson(reciper.getStepsList());
            cv.put(COLUMN_INGREDIENTS, jsonIngredients);
            cv.put(COLUMN_STEPS, jsonSteps);
            Uri uri = getContentResolver().insert(ReciperContract.CONTENT_URI, cv);
            if (uri != null) {
                Log.v("ADDCACHE", "SUCESS");
            } else {
                Log.v("ADDCACHE", "ERRO" + uri.getPath());
            }
        }
    }

    public void clearCache() {
        Uri uri = ReciperContract.CONTENT_URI;
        getContentResolver().delete(uri, null, null);
    }
}
