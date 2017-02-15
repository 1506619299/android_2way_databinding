package com.example.twowaydatabindingexample.dataBindingAdapter;

import android.content.res.ColorStateList;
import android.content.res.Resources.NotFoundException;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by asus on 2016/11/28.
 */

public class DataBindingAttrAdapter {

    @BindingAdapter("android:src")
    public static void setSrc(ImageView view, Bitmap bitmap) {
        view.setImageBitmap(bitmap);
    }

    @BindingAdapter("android:src")
    public static void setSrc(ImageView view, int resId) {
        view.setImageResource(resId);
    }

    @BindingAdapter("android:textColor")
    public static void setTextColor(TextView view, int resId) {
        try {
            view.setTextColor(view.getResources().getColor(resId));
        }catch (NotFoundException e){
            view.setTextColor(resId);
        }

    }

    @BindingAdapter("android:textColor")
    public static void setTextColor(TextView view, ColorStateList colors) {
        view.setTextColor(colors);
    }
}
