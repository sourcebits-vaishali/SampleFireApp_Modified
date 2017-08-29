package com.amazon.android.tv.tenfoot.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v4.content.ContextCompat;

import com.amazon.android.tv.tenfoot.R;

/**
 * Created by vaishaliarora on 17/08/17.
 */

public class ProgramGuideFragment extends Fragment {

    BackgroundManager mBackgroundManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prepareBackgroundManager();

    }

    private void prepareBackgroundManager() {
        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        mBackgroundManager.setColor(ContextCompat.getColor(getActivity(),
                R.color.lb_speech_orb_not_recording));
    }
}
