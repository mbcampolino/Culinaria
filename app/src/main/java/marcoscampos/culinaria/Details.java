package marcoscampos.culinaria;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import marcoscampos.culinaria.adapters.IngredientsAdapter;
import marcoscampos.culinaria.adapters.StepsAdapter;
import marcoscampos.culinaria.interfaces.OnIngredientClick;
import marcoscampos.culinaria.interfaces.OnStepClick;
import marcoscampos.culinaria.pojos.Ingredient;
import marcoscampos.culinaria.pojos.PageResult;
import marcoscampos.culinaria.pojos.Steps;

import static marcoscampos.culinaria.utils.Utils.getThumbnailFromRecipe;
import static marcoscampos.culinaria.utils.Utils.noTitleBar;

@EActivity(R.layout.details)
public class Details extends AppCompatActivity implements OnIngredientClick, OnStepClick {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;
    @ViewById(R.id.recycler_view_main_ingredient)
    RecyclerView recyclerViewIngredient;
    @ViewById(R.id.recycler_view_main_step)
    RecyclerView recyclerViewStep;
    @ViewById(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;
    IngredientsAdapter adapterIngredients;
    StepsAdapter adapterSteps;
    @ViewById(R.id.title_toolbar)
    TextView titleToolbar;
    @ViewById(R.id.title_collapsed)
    TextView titleCollapsed;
    @ViewById(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsed;
    @ViewById(R.id.appbar)
    AppBarLayout appBarLayout;
    @Extra
    PageResult reciper;

    @ViewById(R.id.image_collapsed)
    ImageView imageTop;

    boolean favoriteRecipe;

    @AfterViews
    public void afterViews() {
        if (reciper != null) {
            prepareToolbar(reciper.getName());
            prepareRecyclerView();
        }
    }

    private void prepareToolbar(String reciperTitle) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        titleToolbar.setText(reciperTitle);
        titleCollapsed.setText(reciperTitle);
        noTitleBar(titleToolbar,appBarLayout);
        Glide.with(this).load(getThumbnailFromRecipe(reciper)).thumbnail(0.1f).into(imageTop);
    }

    private void prepareRecyclerView() {
        recyclerViewIngredient.setHasFixedSize(true);
        recyclerViewStep.setHasFixedSize(true);

        adapterIngredients = new IngredientsAdapter(reciper.getIngredientsList(), this, this);
        recyclerViewIngredient.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewIngredient.setAdapter(adapterIngredients);
        recyclerViewIngredient.setNestedScrollingEnabled(false);

        adapterSteps = new StepsAdapter(reciper.getStepsList(), this, this);
        recyclerViewStep.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewStep.setAdapter(adapterSteps);
        recyclerViewStep.setNestedScrollingEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.likert_button_menu) {
            favoriteRecipe(item);
        }

        return super.onOptionsItemSelected(item);
    }

    private void favoriteRecipe(MenuItem item) {
        // fake
        favoriteRecipe = !favoriteRecipe;
        if (favoriteRecipe) {
            item.setIcon(ContextCompat.getDrawable(this, R.drawable.heart));
        } else {
            item.setIcon(ContextCompat.getDrawable(this, R.drawable.heart_outline));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onIngredientClick(Ingredient item) {
        Toast.makeText(this, item.getIngredient(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStepClick(Steps item) {
        /// abre tela video com decricao
    }
}
