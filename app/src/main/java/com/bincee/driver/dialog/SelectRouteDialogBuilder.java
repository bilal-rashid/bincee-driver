package com.bincee.driver.dialog;

import android.content.Context;

public class SelectRouteDialogBuilder {
    private Context context;
    private SelectRouteDialog.Listner listner;

    public SelectRouteDialogBuilder setContext(Context context) {
        this.context = context;
        return this;
    }

    public SelectRouteDialogBuilder setListner(SelectRouteDialog.Listner listner) {
        this.listner = listner;
        return this;
    }

    public SelectRouteDialog createSelectRouteDialog() {
        return new SelectRouteDialog(context, listner);
    }
}