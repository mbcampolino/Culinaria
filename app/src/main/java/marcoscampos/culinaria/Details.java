package marcoscampos.culinaria;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

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

    @Extra
    PageResult reciper;

    @AfterViews
    public void afterViews() {
        if(reciper!=null) {
            prepareToolbar(reciper.getName());
            prepareRecyclerView();
        }
    }

    private void prepareToolbar(String reciper) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(reciper);
    }

    private void prepareRecyclerView() {
        recyclerViewIngredient.setHasFixedSize(true);
        recyclerViewStep.setHasFixedSize(true);

        adapterIngredients = new IngredientsAdapter(reciper.getIngredientsList(), this,this);
        recyclerViewIngredient.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewIngredient.setAdapter(adapterIngredients);
        recyclerViewIngredient.setNestedScrollingEnabled(false);

        adapterSteps = new StepsAdapter(reciper.getStepsList(), this,this);
        recyclerViewStep.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewStep.setAdapter(adapterSteps);
        recyclerViewStep.setNestedScrollingEnabled(false);
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
