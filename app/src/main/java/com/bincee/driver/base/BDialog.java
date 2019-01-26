package com.bincee.driver.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

public class BDialog extends Dialog {
    public BDialog(Context context) {
        super(context);
    }

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        View decorView = window.getDecorView();
        decorView.setBackgroundColor(Color.TRANSPARENT);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
