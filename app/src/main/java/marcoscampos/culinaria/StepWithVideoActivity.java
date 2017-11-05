package marcoscampos.culinaria;

import android.app.Dialog;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

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

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import marcoscampos.culinaria.pojos.PageResult;
import marcoscampos.culinaria.pojos.Steps;

@EActivity(R.layout.video_view_activity)
public class StepWithVideoActivity extends AppCompatActivity {

    @Extra
    int position;
    @Extra
    PageResult reciper;
    @ViewById(R.id.video_view)
    SimpleExoPlayerView videoView;
    @ViewById(R.id.btn_next)
    TextView btnNext;
    @ViewById(R.id.btn_preview)
    TextView btnPreviews;
    @ViewById(R.id.tx_instructions)
    TextView txInstructions;
    @ViewById(R.id.toolbar)
    Toolbar toolbar;
    @ViewById(R.id.title_toolbar)
    TextView titleToolbar;

    SimpleExoPlayer exoPlayerFactory;
    int maxSteps;
    Dialog mFullScreenDialog;
    boolean mExoPlayerFullscreen;
    private DataSource.Factory mediaDataSourceFactory;
    private BandwidthMeter bandwidthMeter;

    @AfterViews
    public void afterViews() {
        videoView.requestLayout();
        maxSteps = reciper.getStepsList().size();
        prepareToolbar(reciper.getName());
        updateViews(position);
        initFullscreenDialog();
    }

    private void prepareToolbar(String reciperTitle) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        titleToolbar.setText(reciperTitle);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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

    private void closeFullscreenDialog() {

        ((ViewGroup) videoView.getParent()).removeView(videoView);
        ((FrameLayout) findViewById(R.id.main_media_frame)).addView(videoView);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
    }


    private void updateViews(int position) {
        if (position != 0) {
            titleToolbar.setText(String.format("Step %s", position));
        } else {
            titleToolbar.setText("Intro");
        }

        btnNext.setEnabled(position != maxSteps - 1);
        btnPreviews.setEnabled(position != 0);
        txInstructions.setText(reciper.getStepsList().get(position).getDescription());
        releasePlayer();
        if (findStepInPosition(position) != null) {
            initializeVideo(findStepInPosition(position));
        } else {
            videoView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (!mExoPlayerFullscreen) {
                openFullscreenDialog();
            }
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (mExoPlayerFullscreen) {
                closeFullscreenDialog();
            }
        }
    }

    @Click(R.id.btn_next)
    void next() {
        updateViews(position += 1);
    }

    @Click(R.id.btn_preview)
    void preview() {
        updateViews(position -= 1);
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void releasePlayer() {
        if (exoPlayerFactory != null) {
            exoPlayerFactory.release();
            exoPlayerFactory = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || exoPlayerFactory == null)) {
            if (findStepInPosition(position) != null) {
                initializeVideo(findStepInPosition(position));
            } else {
                videoView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }
}
