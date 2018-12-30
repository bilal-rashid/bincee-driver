package com.findxain.uberdriver.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.core.content.res.ResourcesCompat;

import com.findxain.uberdriver.R;
import com.findxain.uberdriver.base.BDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * The type Select route dialog.
 */
public class SelectRouteDialog extends BDialog {

    /**
     * The Button send.
     */
    @BindView(R.id.buttonSend)
    Button buttonSend;
    /**
     * The Image view cross.
     */
    @BindView(R.id.imageViewCross)
    ImageView imageViewCross;
    /**
     * The Check box suggested.
     */
    @BindView(R.id.checkBoxSuggested)
    RadioButton checkBoxSuggested;
    /**
     * The Check box default.
     */
    @BindView(R.id.checkBoxDefault)
    RadioButton checkBoxDefault;

    public Listner getListner() {
        return listner;
    }

    /**
     * The Listner.
     */
    Listner listner;
    /**
     * The Radio group.
     */
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    private Context context;

    /**
     * Instantiates a new Select route dialog.
     *
     * @param context the context
     */
    public SelectRouteDialog(Context context, Listner listner) {
        super(context);
        setCancelable(true);
        this.listner = listner;
        int layout = R.layout.dialog_select_route;
        View view = getLayoutInflater().inflate(layout, null, false);
        setContentView(view);
        ButterKnife.bind(this, view);

        checkBoxDefault.setTypeface(ResourcesCompat.getFont(getContext(), R.font.gotham_book));
        checkBoxSuggested.setTypeface(ResourcesCompat.getFont(getContext(), R.font.gotham_book));


    }

    /**
     * On button send clicked.
     */
    @OnClick(R.id.buttonSend)
    public void onButtonSendClicked() {
        dismiss();

        listner.routeSelected(radioGroup.getCheckedRadioButtonId() == R.id.checkBoxSuggested ? 1 : 2);

    }

    /**
     * On image view cross clicked.
     */
    @OnClick(R.id.imageViewCross)
    public void onImageViewCrossClicked() {

        dismiss();
    }

    public SelectRouteDialog setContext(Context context) {
        this.context = context;
        return this;
    }

    public SelectRouteDialog setListner(Listner listner) {
        this.listner = listner;
        return this;
    }

    public SelectRouteDialog createSelectRouteDialog() {
        return new SelectRouteDialogBuilder().setContext(context).setListner(listner).createSelectRouteDialog();
    }


    /**
     * The interface Listner.
     */
    public interface Listner {

        /**
         * Route selected.
         *
         * @param i the 1 for suggested and 2 for other
         */
        void routeSelected(int i);
    }

}

