package com.findxain.uberdriver.helper;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.findxain.uberdriver.R;

import androidx.databinding.BindingAdapter;

public final class ImageBinder {
    private ImageBinder() {
    }

    @BindingAdapter("imageUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        Context context = imageView.getContext();
        Glide.with(context).load(url)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.profile_avatar)
                        .error(R.drawable.profile_avatar))
                .into(imageView);
    }
}
