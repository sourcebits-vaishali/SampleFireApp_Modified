package com.amazon.android.tv.tenfoot.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.RowsFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ClassPresenterSelector;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.amazon.android.tv.tenfoot.R;
import com.amazon.android.tv.tenfoot.model.Video;
import com.amazon.android.tv.tenfoot.presenter.CardPresenter;

/**
 * Created by vaishaliarora on 17/08/17.
 */

public class HostFragment extends Fragment {

    BackgroundManager mBackgroundManager;
    private ClassPresenterSelector mPresenterSelector;
    private ArrayObjectAdapter mAdapter;

    private TextView mTitle;
    private TextView mDescription;
    private FrameLayout mRowFrameLayout;
    private RowsFragment mRowsFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prepareBackgroundManager();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.host_fragment,
                container, false);

        mTitle = (TextView)view.findViewById(R.id.title_host);
        mDescription = (TextView)view.findViewById(R.id.title_description);
        mRowFrameLayout = (FrameLayout)view .findViewById(R.id.row_frame_layout);

        mRowsFragment = (RowsFragment) getChildFragmentManager().findFragmentById(
                android.support.v17.leanback.R.id.details_rows_dock);
        if (mRowsFragment == null) {
            mRowsFragment = new RowsFragment();
            getChildFragmentManager().beginTransaction()
                                     .replace(R.id.row_frame_layout, mRowsFragment).commit();
        }
        mPresenterSelector = new ClassPresenterSelector();
        mPresenterSelector.addClassPresenter(ListRow.class, new ListRowPresenter());
        mAdapter = new ArrayObjectAdapter(mPresenterSelector);
        mRowsFragment.setAdapter(mAdapter);

        setUpHostList();

        setupEventListeners();

        return view;
    }

    private void prepareBackgroundManager() {
        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        mBackgroundManager.setColor(ContextCompat.getColor(getActivity(),
                R.color.white));
    }

    @Override
    public void onStart() {
        super.onStart();
//        mRowFrameLayout.setFocusable(true);
        mRowFrameLayout.requestFocus();
    }

    @Override
    public void onStop() {
        super.onStop();
        mRowFrameLayout.clearFocus();
//        mRowFrameLayout.setFocusable(false);
    }

    private void setUpHostList() {
        HeaderItem header = new HeaderItem(0, "");
        ArrayObjectAdapter gridRowAdapter = new ArrayObjectAdapter(new CardPresenter());
        for(int i=0;i<10;i++) {
            Video video = new Video(i, "Category : "+i,"Title :  "+i, "Description :"+i,"Video Url :"+i,"Image : "+i,
                    "Card Image "
                    + ":"+i,"Studio :"+i);
            gridRowAdapter.add(video);
        }
        mAdapter.add(new ListRow(header, gridRowAdapter));
    }

    private void setupEventListeners() {
        mRowsFragment.setOnItemViewClickedListener(new ItemViewClickedListener());
        mRowsFragment.setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                RowPresenter.ViewHolder rowViewHolder, Row row) {
            if (item instanceof Video) {
                Video video = (Video) item;
                mTitle.setText(video.title);
                mDescription.setText(video.description);
            }
        }

    }

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                RowPresenter.ViewHolder rowViewHolder, Row row) {
            float selectedLevel = rowViewHolder.getSelectLevel();
            if(selectedLevel != 0.0) {
                if (item instanceof Video) {
                    Video video = (Video) item;
                    mTitle.setText(video.title);
                    mDescription.setText(video.description);
                }
            }

        }
    }
}
