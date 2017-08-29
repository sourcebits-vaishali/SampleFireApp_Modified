package com.amazon.android.tv.tenfoot.ui.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ClassPresenterSelector;
import android.support.v17.leanback.widget.DetailsOverviewRow;
import android.support.v17.leanback.widget.SparseArrayObjectAdapter;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amazon.android.model.content.Content;
import com.amazon.android.tv.tenfoot.R;
import com.amazon.android.tv.tenfoot.presenter.AiringPresenter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

/**
 * Created by vaishaliarora on 17/08/17.
 */

public class AiringNowFragment extends Fragment {

    private static final String TAG = AiringNowFragment.class.getSimpleName();

    private BackgroundManager mBackgroundManager;
    ArrayObjectAdapter mRowsAdapter;
    private ClassPresenterSelector mPresenterSelector;

    private static final int ACTION_WATCH_NOW = 1;
    private static final int ACTION_MORE_ABOUT = 2;
    private static final int ACTION_BUY = 3;

    private Content mDisplayData;

    private TextView mWatchNow;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareBackgroundManager();
       /* FullWidthDetailsOverviewRowPresenter detailsPresenter =
                new FullWidthDetailsOverviewRowPresenter(new DetailsDescriptionPresenter(),
                        new VideoDetailFragment.MovieDetailsOverviewLogoPresenter());

        detailsPresenter.setBackgroundColor(
                ContextCompat.getColor(getActivity(), R.color.lb_playback_controls_background_light));
        detailsPresenter.setInitialState(FullWidthDetailsOverviewRowPresenter.STATE_HALF);

        mPresenterSelector = new ClassPresenterSelector();
        mPresenterSelector.addClassPresenter(DetailsOverviewRow.class, detailsPresenter);
        mPresenterSelector.addClassPresenter(ListRow.class, new ListRowPresenter());
        mRowsAdapter = new ArrayObjectAdapter(mPresenterSelector);
        setAdapter(mRowsAdapter);
        setupDetailsOverviewRow();*/
//            setupMovieListRow();
//        updateBackground("https://");

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
            final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.airing_now_fragment, container, false);

       /* SparseArrayObjectAdapter adapter = new SparseArrayObjectAdapter();

        adapter.set(1, new Action(1, getResources()
                .getString(R.string.watch_trailer_1),
                getResources().getString(R.string.watch_trailer_2)));
        adapter.set(2, new Action(2, getResources().getString(R.string.rent_1),
                getResources().getString(R.string.rent_2)));
        adapter.set(ACTION_BUY, new Action(ACTION_BUY, getResources().getString(R.string.buy_1),
                getResources().getString(R.string.buy_2)));
//        row.setActionsAdapter(adapter);

        mWatchNow = (TextView)view.findViewById(R.id.watch_now);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Toast.makeText(getActivity(),"Watch Now clicked", Toast.LENGTH_SHORT).show();
            }
        });*/
        return view;
    }

    private void prepareBackgroundManager() {
        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        mBackgroundManager.setColor(ContextCompat.getColor(getActivity(),
                R.color.lb_playback_controls_background_light));
    }

    private void setupDetailsOverviewRow() {
        mDisplayData = new Content();
       /* mDisplayData.setTitle("This is the title");
        mDisplayData.setDuration(10);
        mDisplayData.setDescription("This is the description");*/


        final DetailsOverviewRow row = new DetailsOverviewRow(mDisplayData);


//        HeaderItem header = new HeaderItem(i, menuItems[i]);
        AiringPresenter presenter = new AiringPresenter();
        SparseArrayObjectAdapter gridRowAdapter = new SparseArrayObjectAdapter(presenter);
//        row.setItem(gridRowAdapter);


        Glide.with(this)
             .load("http")
             .asBitmap()
             .dontAnimate()
             .error(R.drawable.app_logo)
             .into(new SimpleTarget<Bitmap>() {
                 @Override
                 public void onResourceReady(final Bitmap resource,
                         GlideAnimation glideAnimation) {
                     row.setImageBitmap(getActivity(), resource);
//                     startEntranceTransition();
                 }
             });

        SparseArrayObjectAdapter adapter = new SparseArrayObjectAdapter();

        adapter.set(ACTION_WATCH_NOW, new Action(ACTION_WATCH_NOW, getResources()
                .getString(R.string.watch_now)));
        adapter.set(ACTION_MORE_ABOUT,
                new Action(ACTION_MORE_ABOUT, getResources().getString(R.string.more_about_heidi)));

        row.setActionsAdapter(adapter);

        mRowsAdapter.add(row);
    }

    private void setOtherView() {
        ArrayObjectAdapter mRowsAdapter;
        mRowsAdapter = new ArrayObjectAdapter(new ClassPresenterSelector());
        Content mData = new Content();
        mData.setTitle("Title");
        mData.setDuration(12);
        mData.setDescription("This is the dummy text...");
        ClassPresenterSelector mPresenterSelector = new ClassPresenterSelector();
        ArrayObjectAdapter adapter = new ArrayObjectAdapter(mPresenterSelector);
        final DetailsOverviewRow row = new DetailsOverviewRow(mData);
//        row.setImageDrawable(getResources().getDrawable(R.drawable.default_background));

        Glide.with(getActivity())
             .load("http://")
             .centerCrop()
             .into(new SimpleTarget<GlideDrawable>(274, 274) {
                 @Override
                 public void onResourceReady(GlideDrawable resource,
                         GlideAnimation<? super GlideDrawable>
                                 glideAnimation) {
                     Log.d(TAG, "details overview card image url ready: " + resource);
                     row.setImageDrawable(resource);
//                     mAdapter.notifyArrayItemRangeChanged(0, mAdapter.size());
                 }
             });

        row.addAction(new android.support.v17.leanback.widget.Action(1, getResources().getString(
                R.string.watch_trailer_1), getResources().getString(R.string.watch_trailer_2)));
        row.addAction(new android.support.v17.leanback.widget.Action(2, getResources().getString(R.string.rent_1),
                getResources().getString(R.string.rent_2)));
        row.addAction(new android.support.v17.leanback.widget.Action(3, getResources().getString(R.string.buy_1),
                getResources().getString(R.string.buy_2)));

        adapter.add(row);

        mRowsAdapter.add(adapter);

    }
}
