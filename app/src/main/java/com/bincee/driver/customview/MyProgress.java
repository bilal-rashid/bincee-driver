package com.bincee.driver.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.bincee.driver.R;

public class MyProgress extends FrameLayout {

    public MyProgress(Context context) {
        super(context);
        init();
    }

    public MyProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public MyProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.progress_layout, this, false);
        addView(view);
    }
}
