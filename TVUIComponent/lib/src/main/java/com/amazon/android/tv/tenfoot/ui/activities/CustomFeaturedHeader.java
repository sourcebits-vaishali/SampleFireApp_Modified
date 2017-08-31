package com.amazon.android.tv.tenfoot.ui.activities;

import android.support.v17.leanback.widget.HeaderItem;

/**
 * Created by vaishaliarora on 30/08/17.
 */

public class CustomFeaturedHeader extends HeaderItem {

    private int layoutResId;

    public CustomFeaturedHeader(final long id, final String name) {
        super(id, name);
    }

    public CustomFeaturedHeader(final String name) {
        super(name);
    }

    public CustomFeaturedHeader(final long id, final String name, final int layoutResId) {
        super(id, name);
        this.layoutResId = layoutResId;
    }

    public int getResId() {
        return layoutResId;
    }

    public void setResId(int layoutResId) {
        this.layoutResId = layoutResId;
    }
}
