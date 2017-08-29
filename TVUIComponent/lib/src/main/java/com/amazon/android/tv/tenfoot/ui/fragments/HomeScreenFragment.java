package com.amazon.android.tv.tenfoot.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.BrowseRowsFrameLayout;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.amazon.android.contentbrowser.helper.AuthHelper;
import com.amazon.android.model.content.Content;
import com.amazon.android.tv.tenfoot.R;
import com.amazon.android.tv.tenfoot.constants.PreferencesConstants;
import com.amazon.android.tv.tenfoot.presenter.AiringPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by vaishaliarora on 10/08/17.
 */

public class HomeScreenFragment extends BrowseFragment {

    private static final String TAG = HomeScreenFragment.class.getSimpleName();

    private BackgroundManager mBackgroundManager;
    private ArrayObjectAdapter mMenuAdapter;

    private FrameLayout mRowFrameLayout;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate");
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);

        prepareBackgroundManager();
        setupUIElements();
        setupEventListeners();


        mMenuAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        setAdapter(mMenuAdapter);
        setMenuItems();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        final View view = super.onCreateView(inflater, container, savedInstanceState);

        if (view != null) {
            // Hiding these views because we want to use our own search widget instead
            // of Leanback's SearchOrb that's part of the BrowseFragment.
            View searchOrb = view.findViewById(R.id.search_orb);
            if (searchOrb != null) {
                searchOrb.setVisibility(View.GONE);
            }
            ImageView icon = (ImageView) view.findViewById(R.id.icon);
            if (icon != null) {
                icon.setVisibility(View.GONE);
            }

            mRowFrameLayout = (BrowseRowsFrameLayout) view.findViewById(R.id.browse_container_dock);
        }
        return view;
    }

    private void prepareBackgroundManager() {
        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
    }

    private void setupUIElements() {
        setHeadersState(HEADERS_ENABLED);
        setBadgeDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.company_logo));

        setBrandColor(ContextCompat.getColor(getActivity(), R.color.browse_headers_bar));
        mBackgroundManager.setColor(ContextCompat.getColor(getActivity(),
                R.color.white));

        enableRowScaling(false);

    }


    private void setMenuItems() {
        String[] menuItems = getActivity().getResources().getStringArray(R.array.home_screen_menu_items);
        for (int i = 0; i < PreferencesConstants.MENU_ITEMS_SIZE; i++) {

            HeaderItem header = new HeaderItem(menuItems[i]);
            AiringPresenter presenter = new AiringPresenter();
            ArrayObjectAdapter gridRowAdapter = new ArrayObjectAdapter(presenter);
            mMenuAdapter.add(new ListRow(header, gridRowAdapter));

        }
    }

    /**
     * Event bus listener method to listen for authentication updates from AUthHelper and update
     * the login action status in settings.
     *
     * @param authenticationStatusUpdateEvent Broadcast event for update in authentication status.
     */

    @Subscribe
    public void onAuthenticationStatusUpdateEvent(AuthHelper.AuthenticationStatusUpdateEvent
            authenticationStatusUpdateEvent) {

        if (mMenuAdapter != null) {
            mMenuAdapter.notifyArrayItemRangeChanged(0, mMenuAdapter.size());
        }
    }

    private void setupEventListeners() {
        setOnItemViewSelectedListener(new OnItemViewSelectedListener() {
            @Override
            public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                    RowPresenter.ViewHolder rowViewHolder, Row row) {

                //If row is an instance of ListRow or header menu
                if (row instanceof ListRow) {

                    Fragment fragment = null;
                    String headerName = row.getHeaderItem().getName();
                    if (TextUtils.equals(headerName, getString(R.string.airing_now))) {
                        fragment = new AiringNowFragment();
                    } else if (TextUtils.equals(headerName, getString(R.string.hosts))) {
                        fragment = new HostFragment();
                    } else if (TextUtils.equals(headerName, getString(R.string.collections))) {
                        fragment = new CollectionsFragment();
                    } else if (TextUtils.equals(headerName, getString(R.string.speciality_shows))) {
                        fragment = new SpecialityShowFragment();
                    } else if (TextUtils.equals(headerName, getString(R.string.program_guide))) {
                        fragment = new ProgramGuideFragment();
                    }

                    if (fragment != null) {
                        getFragmentManager().beginTransaction().replace(mRowFrameLayout.getId(),
                                fragment).commit();
                    }

                }

                if (item instanceof Content) {
                    Log.d(TAG, row.getClass().getSimpleName());

                }

            }
        });

        setOnItemViewClickedListener(new ItemViewClickedListener());
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                RowPresenter.ViewHolder rowViewHolder, Row row) {
            Fragment fragment = null;
            String headerName = row.getHeaderItem().getName();
            if (row instanceof ListRow) {
                if (TextUtils.equals(headerName, getString(R.string.airing_now))) {
                 /*   fragment = new VideoDetailFragment();
                    fragment.getView().requestFocus();*/
                }
            }
        }
    }


}