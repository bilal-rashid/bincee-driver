package com.bincee.driver.helper;

import android.content.Context;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bincee.driver.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

public final class ImageBinder {
    private ImageBinder() {
    }

    @BindingAdapter("imageUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        Context context = imageView.getContext();
        Glide.with(context).load(url)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.profile_avatar)
                        .error(R.drawable.profile_avatar)
                )
                .into(imageView);
    }

    @BindingAdapter("imageRoundedCenterCorpParent")
    public static void roundedCornerCenterCorpParent(ImageView imageView, String url) {
        Context context = imageView.getContext();
        Glide.with(context).load(url)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.profile_avatar)
                        .transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(18)))
                        .error(R.drawable.profile_avatar)
                )

                .into(imageView);
    }

    @BindingAdapter("imageRoundedCenterCorpKid")
    public static void roundedCornerCenterCorpKid(ImageView imageView, String url) {
        Context context = imageView.getContext();
        Glide.with(context).load(url)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.kid_profile)
                        .transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(18)))
                        .error(R.drawable.kid_profile)
                )

                .into(imageView);
    }


}
