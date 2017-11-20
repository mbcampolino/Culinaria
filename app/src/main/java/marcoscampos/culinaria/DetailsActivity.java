package marcoscampos.culinaria;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import marcoscampos.culinaria.adapters.IngredientsAdapter;
import marcoscampos.culinaria.adapters.StepsAdapter;
import marcoscampos.culinaria.db.ReciperContract;
import marcoscampos.culinaria.interfaces.OnIngredientClick;
import marcoscampos.culinaria.interfaces.OnStepClick;
import marcoscampos.culinaria.pojos.Ingredient;
import marcoscampos.culinaria.pojos.PageResult;
import marcoscampos.culinaria.pojos.Steps;

import static marcoscampos.culinaria.db.ReciperContract.ReciperEntry.COLUMN_ID;
import static marcoscampos.culinaria.db.ReciperContract.ReciperEntry.COLUMN_IMAGE;
import static marcoscampos.culinaria.db.ReciperContract.ReciperEntry.COLUMN_INGREDIENTS;
import static marcoscampos.culinaria.db.ReciperContract.ReciperEntry.COLUMN_NAME;
import static marcoscampos.culinaria.db.ReciperContract.ReciperEntry.COLUMN_SERVINGS;
import static marcoscampos.culinaria.db.ReciperContract.ReciperEntry.COLUMN_STEPS;
import static marcoscampos.culinaria.utils.Utils.getThumbnailFromRecipe;
import static marcoscampos.culinaria.utils.Utils.noTitleBar;

@SuppressLint("Registered")
@EActivity(R.layout.details)
public class DetailsActivity extends AppCompatActivity implements OnIngredientClick, OnStepClick {

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
    boolean tabletSize;
    @ViewById(R.id.tx_instructions)
    TextView txInstructions;
    @ViewById(R.id.video_view)
    SimpleExoPlayerView videoView;
    SimpleExoPlayer exoPlayerFactory;
    Dialog mFullScreenDialog;
    boolean mExoPlayerFullscreen;
    int position = 0;
    private DataSource.Factory mediaDataSourceFactory;
    private BandwidthMeter bandwidthMeter;

    @AfterViews
    public void afterViews() {
        tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (reciper != null) {
            prepareToolbar(reciper.getName());
            prepareRecyclerView();
            if (tabletSize) {
                //videoView.requestLayout();
                updateViews(position);
                initFullscreenDialog();
            }
        }
    }

    private void prepareToolbar(String reciperTitle) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        titleToolbar.setText(reciperTitle);
        titleCollapsed.setText(reciperTitle);
        noTitleBar(titleToolbar, appBarLayout);
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

    private void initializeVideo(Steps stepInPosition) {
        videoView.setVisibility(View.VISIBLE);
        bandwidthMeter = new DefaultBandwidthMeter();
        mediaDataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "culinaire"), (TransferListener<? super DataSource>) bandwidthMeter);
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);

        DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        exoPlayerFactory = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        videoView.setPlayer(exoPlayerFactory);

        exoPlayerFactory.setPlayWhenReady(false);
        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(stepInPosition.getVideoURL()),
                mediaDataSourceFactory, extractorsFactory, null, null);

        exoPlayerFactory.prepare(mediaSource);
    }

    private Steps findStepInPosition(int position) {
        if (!reciper.getStepsList().get(position).getVideoURL().isEmpty()) {
            return reciper.getStepsList().get(position);
        } else {
            return null;
        }
    }

    private void initFullscreenDialog() {

        mFullScreenDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen)
                    finish();
                super.onBackPressed();
            }
        };

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            openFullscreenDialog();
        }
    }

    private void openFullscreenDialog() {

        ((ViewGroup) videoView.getParent()).removeView(videoView);
        mFullScreenDialog.addContentView(videoView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onIngredientClick(Ingredient item) {
        return;
    }

    @Override
    public void onStepClick(int position) {

        if (!tabletSize) {
            Intent intent = new Intent(this, StepWithVideoActivity_.class);
            intent.putExtra("position", position);
            intent.putExtra("reciper", reciper);
            startActivity(intent);
        } else {
            updateViews(position);
        }
    }

    private void updateViews(int position) {
        this.position = position;
        txInstructions.setText(reciper.getStepsList().get(position).getDescription());

        releasePlayer();
        if (findStepInPosition(position) != null) {
            initializeVideo(findStepInPosition(position));
        } else {
            videoView.setVisibility(View.GONE);
        }
    }

    private void releasePlayer() {

        if (exoPlayerFactory != null && tabletSize) {
            exoPlayerFactory.release();
            exoPlayerFactory = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (tabletSize) {
            if (Util.SDK_INT <= 23 || exoPlayerFactory == null) {
                if (findStepInPosition(position) != null) {
                    initializeVideo(findStepInPosition(position));
                } else {
                    videoView.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23 && tabletSize) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23 && tabletSize) {
            releasePlayer();
        }
    }
}
