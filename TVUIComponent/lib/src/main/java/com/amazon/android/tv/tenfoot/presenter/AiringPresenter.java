package com.amazon.android.tv.tenfoot.presenter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.widget.Presenter;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amazon.android.tv.tenfoot.R;

/**
 * Created by vaishaliarora on 14/08/17.
 */

public class AiringPresenter extends Presenter {
    private static final String TAG = "CardPresenter";

    private static final int CARD_WIDTH = 313;
    private static final int CARD_HEIGHT = 176;
    private static int sSelectedBackgroundColor;
    private static int sDefaultBackgroundColor;
    private Drawable mDefaultCardImage;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        Log.d(TAG, "onCreateViewHolder");

       /* sDefaultBackgroundColor = parent.getResources().getColor(R.color.default_background);
        sSelectedBackgroundColor = parent.getResources().getColor(R.color.selected_background);*/
        mDefaultCardImage = parent.getResources().getDrawable(R.drawable.movie);



        TextView view = new TextView(parent.getContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(1024, 1024));
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setBackgroundColor(ContextCompat.getColor(parent.getContext(),
                R.color.white));
        view.setTextColor(Color.WHITE);
        view.setGravity(Gravity.CENTER);
        view.setText("Vaishali Arora");

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        ((TextView) viewHolder.view).setText((String) item);
        /*LinearLayout layout = (LinearLayout)viewHolder.view;
        layout.setTag(item.toString());*/
//        layout.setBackgroundColor(ContextCompat.getColor(parent.getContext(), R.color.selected_background));
      /*  AiringNowView view = (AiringNowView) viewHolder.view;
        view.setTitle("Vaishali");
        view.setDuration("Duration 3h");
        view.setDescription("This is the dummy description.\nThis is the dummy description.\nThis is the dummy "
                + "description.\nThis is the dummy description.\nThis is the dummy description.");
        view.setBtn1Text("Watch Now");
        view.setBtn2Text("More about Heidi");*/

    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
        Log.d(TAG, "onUnbindViewHolder");
       /* ImageCardView cardView = (ImageCardView) viewHolder.view;
        // Remove references to images so that the garbage collector can free up memory
        cardView.setBadgeImage(null);
        cardView.setMainImage(null);*/
    }

}
