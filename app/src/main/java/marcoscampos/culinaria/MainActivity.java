package marcoscampos.culinaria;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import marcoscampos.culinaria.adapters.MainAdapter;
import marcoscampos.culinaria.db.ReciperContract;
import marcoscampos.culinaria.interfaces.OnRecyclerClick;
import marcoscampos.culinaria.pojos.PageResult;
import marcoscampos.culinaria.utils.Utils;

import static marcoscampos.culinaria.db.ReciperContract.ReciperEntry.COLUMN_ID;
import static marcoscampos.culinaria.db.ReciperContract.ReciperEntry.COLUMN_IMAGE;
import static marcoscampos.culinaria.db.ReciperContract.ReciperEntry.COLUMN_INGREDIENTS;
import static marcoscampos.culinaria.db.ReciperContract.ReciperEntry.COLUMN_NAME;
import static marcoscampos.culinaria.db.ReciperContract.ReciperEntry.COLUMN_SERVINGS;

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


    public void loadRecipes() {

        adapter.clear();

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

    @Override
    public void onFavoriteClick(PageResult item, boolean isFavorite, ImageButton v) {
        if (!isFavorite) {
            addFav(item, v);
        } else {
            removeFav(item.getId(), v);
        }
    }

    private boolean addFav(PageResult reciper, ImageButton v) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, reciper.getId());
        cv.put(COLUMN_NAME, reciper.getName());
        cv.put(COLUMN_SERVINGS, reciper.getServings());
        cv.put(COLUMN_IMAGE, reciper.getImage());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(reciper.getIngredientsList());
        cv.put(COLUMN_INGREDIENTS, json);

        Uri uri = getContentResolver().insert(ReciperContract.ReciperEntry.CONTENT_URI, cv);
        if (uri != null) {
            v.setBackgroundResource(R.drawable.heart);
            Toast.makeText(this, "Favoritado", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;

    }

    public void removeFav(int id, ImageButton v) {
        String stringId = Integer.toString(id);
        Uri uri = ReciperContract.ReciperEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();
        getContentResolver().delete(uri, null, null);
        Toast.makeText(this, "Removido", Toast.LENGTH_SHORT).show();
        v.setBackgroundResource(R.drawable.heart_outline);
    }
}
