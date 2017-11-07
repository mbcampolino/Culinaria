package marcoscampos.culinaria;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import marcoscampos.culinaria.adapters.MainAdapter;
import marcoscampos.culinaria.interfaces.OnRecyclerClick;
import marcoscampos.culinaria.pojos.PageResult;
import marcoscampos.culinaria.utils.Utils;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, OnRecyclerClick {

    private static final String URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

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
        decoration = new Utils.SpacesItemDecoration(valueInPixels,this,tabletSize);
        recyclerView.setHasFixedSize(true);
        if(tabletSize) {
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
            if(!tabletSize) {
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
        startActivity(intent);
    }

    @Override
    public void onFavoriteClick(PageResult item, boolean isFavorite) {
        if(isFavorite) {
            Toast.makeText(this, "Favoritado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Removido", Toast.LENGTH_SHORT).show();
        }

    }
}
