package com.amazon.android.tv.tenfoot.ui.activities;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.amazon.android.tv.tenfoot.R;
import com.amazon.android.tv.tenfoot.base.BaseActivity;
import com.amazon.android.tv.tenfoot.ui.fragments.ContentBrowseFragment;
import com.amazon.android.utils.Helpers;

/**
 * Created by vaishaliarora on 29/08/17.
 */

public class MainActivity extends BaseActivity implements ContentBrowseFragment
        .OnBrowseRowListener {

    private final String TAG = MainActivity.class.getSimpleName();

    private static final int CONTENT_IMAGE_CROSS_FADE_DURATION = 1000;
    private static final int ACTIVITY_ENTER_TRANSITION_FADE_DURATION = 1500;
    private static final int UI_UPDATE_DELAY_IN_MS = 0;

    private LinearLayout mFeaturedLayout;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);

        mFeaturedLayout = (LinearLayout) findViewById(R.id.featured_layout);

        Helpers.handleActivityEnterFadeTransition(this, ACTIVITY_ENTER_TRANSITION_FADE_DURATION);

        int CARD_WIDTH_PX = 280;
        int mCardWidthDp = Helpers.convertPixelToDp(this, CARD_WIDTH_PX);
        mFeaturedLayout.setMinimumHeight(mCardWidthDp);

    }

    @Override
    public void onItemSelected(final Object item) {

    }
}
