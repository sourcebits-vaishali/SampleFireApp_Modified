package com.amazon.android.tv.tenfoot.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.RowsFragment;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ClassPresenterSelector;
import android.support.v17.leanback.widget.DetailsOverviewRow;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.support.v17.leanback.widget.ItemBridgeAdapter;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v17.leanback.widget.SparseArrayObjectAdapter;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.amazon.android.model.content.Content;
import com.amazon.android.tv.tenfoot.R;
import com.amazon.android.tv.tenfoot.base.BaseActivity;
import com.amazon.android.tv.tenfoot.model.FeaturedData;
import com.amazon.android.tv.tenfoot.presenter.AiringPresenter;
import com.amazon.android.tv.tenfoot.presenter.CardPresenter;
import com.amazon.android.utils.Helpers;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.Timer;
import java.util.TimerTask;

import rx.Subscription;

/**
 * Created by vaishaliarora on 29/08/17.
 */

public class MainActivity extends BaseActivity {

    private final String TAG = MainActivity.class.getSimpleName();
    private static final int BACKGROUND_UPDATE_DELAY = 300;
    private static final int ACTIVITY_ENTER_TRANSITION_FADE_DURATION = 1500;
    private static final int ACTION_WATCH_NOW = 1;
    private static final int ACTION_MORE_ABOUT = 2;

    private BackgroundManager mBackgroundManager;
    private DisplayMetrics mMetrics;
    private int mDefaultBackgroundColor;
    private Timer mBackgroundTimer;
    private String mDefaultImageUrl;

    private Subscription mContentImageLoadSubscription;

    private TextView mTitle;
    private TextView mDescription;

    private RowsFragment mRowsFragment;

    private ClassPresenterSelector mPresenterSelector;
    private ArrayObjectAdapter mAdapter;
    private HorizontalGridView mActionsRow;
    private ItemBridgeAdapter mActionBridgeAdapter;

    private final Handler mHandler = new Handler();


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);

        prepareBackgroundManager();

        mTitle = (TextView) findViewById(R.id.title);
        mDescription = (TextView) findViewById(R.id.description);
        mActionsRow =
                (HorizontalGridView) findViewById(android.support.v17.leanback.R.id.details_overview_actions);
        mActionsRow.setHasOverlappingRendering(false);


        final int fadeLength = 10;
        mActionsRow.setFadingRightEdgeLength(fadeLength);
        mActionsRow.setFadingLeftEdgeLength(fadeLength);

        Helpers.handleActivityEnterFadeTransition(this, ACTIVITY_ENTER_TRANSITION_FADE_DURATION);


        mRowsFragment = (RowsFragment) getFragmentManager().findFragmentById(
                android.support.v17.leanback.R.id.details_rows_dock);
        if (mRowsFragment == null) {
            mRowsFragment = new RowsFragment();
            getFragmentManager().beginTransaction()
                                .replace(R.id.featured_frame_layout, mRowsFragment).commit();
        }
        mPresenterSelector = new ClassPresenterSelector();

        mPresenterSelector.addClassPresenter(ListRow.class, new ListRowPresenter());
        mAdapter = new ArrayObjectAdapter(mPresenterSelector);
        mRowsFragment.setAdapter(mAdapter);

        setUpFeaturedList();
        setAction();
        setUpEventListsner();
    }

    private void setUpEventListsner() {
        mRowsFragment.setOnItemViewSelectedListener(new ItemViewSelectedListener());
        mRowsFragment.setOnItemViewClickedListener(new ItemViewClickListener());
    }

    private void prepareBackgroundManager() {
        mBackgroundManager = BackgroundManager.getInstance(this);
        mBackgroundManager.attach(this.getWindow());
        mDefaultBackgroundColor = ContextCompat.getColor(this, R.color.white);
        mMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    private void updateBackground(String url) {
        int width = mMetrics.widthPixels;
        int height = mMetrics.heightPixels;
        Glide.with(this)
             .load(url)
             .asBitmap()
             .centerCrop()
             .error(R.color.white)
             .into(new SimpleTarget<Bitmap>(width, height) {
                 @Override
                 public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap>
                         glideAnimation) {
                     mBackgroundManager.setBitmap(resource);
                 }
             });
        mBackgroundTimer.cancel();
    }

    private void startBackgroundTimer() {
        if (null != mBackgroundTimer) {
            mBackgroundTimer.cancel();
        }
        mBackgroundTimer = new Timer();
        mBackgroundTimer.schedule(new UpdateBackgroundTask(), BACKGROUND_UPDATE_DELAY);
    }

    private class UpdateBackgroundTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mDefaultImageUrl!=null) {
                        updateBackground(mDefaultImageUrl);
                    }
                }
            });
        }
    }

    private void setAction() {
        Content mSelectedVideo = new Content();
        mSelectedVideo.setTitle("Dummy title");
        mSelectedVideo.setDescription("Dummy description");
        AiringPresenter presenter = new AiringPresenter();
        final DetailsOverviewRow row = new DetailsOverviewRow(presenter);

        SparseArrayObjectAdapter adapter1 = new SparseArrayObjectAdapter();

        adapter1.set(ACTION_WATCH_NOW, new Action(ACTION_WATCH_NOW, getResources()
                .getString(R.string.watch_now)));
        adapter1.set(ACTION_MORE_ABOUT, new Action(ACTION_MORE_ABOUT, getResources()
                .getString(R.string.more_about_heidi)));
        row.setActionsAdapter(adapter1);

        mActionBridgeAdapter = new ItemBridgeAdapter(adapter1);
        mActionsRow.setAdapter(mActionBridgeAdapter);
    }


    private void setUpFeaturedList() {
        CustomFeaturedHeader header = new CustomFeaturedHeader(0, getString(R.string.featured), R.layout.text_layout);

        ArrayObjectAdapter gridRowAdapter = new ArrayObjectAdapter(new CardPresenter());
        for (int i = 0; i < 11; i++) {
            FeaturedData data = new FeaturedData();
            data.setTitle("Video Title :" + (i + 1));
            data.setDescription("This appears to the show description. Make sure to mention\n"
                    + "this in the upcoming tech meetings. This is available (its on\n"
                    + "the TV guide now) however the current site feed does not\n"
                    + "have it. Descriptions include savings/promotional…");
            data.setImageUrl("http://www.pngall.com/wp-content/uploads/2016/05/Teddy-Bear-Download-PNG.png");

            gridRowAdapter.add(data);
        }

        mAdapter.add(new ListRow(header, gridRowAdapter));
    }


    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                RowPresenter.ViewHolder rowViewHolder, Row row) {
            FeaturedData data = (FeaturedData) item;
            if (data != null) {
                mTitle.setText(data.getTitle());
                mDescription.setText(data.getDescription());
                mDefaultImageUrl = data.getImageUrl();
                startBackgroundTimer();
            }
        }
    }

    private final class ItemViewClickListener implements OnItemViewClickedListener {

        @Override
        public void onItemClicked(final Presenter.ViewHolder itemViewHolder, final Object item,
                final RowPresenter.ViewHolder rowViewHolder,
                final Row row) {
            startActivityForResult(new Intent(MainActivity.this, WatchNowActivity.class), 1000);
        }
    }
}
