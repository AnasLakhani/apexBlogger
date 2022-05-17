package com.coderlytics.apexblogger.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.BindingAdapter;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.coderlytics.apexblogger.R;

public class MyUtils {

    private static final String TAG = "MyUtils";

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static AlertDialog getLoadingDialog(Activity activity) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_loading_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        return dialogBuilder.create();
    }

    @BindingAdapter({"avatar"})
    public static void requestOptions(ImageView imageView, String imageid) {
        Context context = imageView.getContext();

        if (imageid == null) {
            return;
        }

        if (!imageid.contains(".")) {
            return;
        }


        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.setColorFilter(0xffff0000, PorterDuff.Mode.MULTIPLY);
        circularProgressDrawable.start();

        if (!imageid.startsWith("http")) {
            Glide.with(context).load(imageid).transition(DrawableTransitionOptions.withCrossFade()).error(R.drawable.ic_perm_).into(imageView);
            Log.d(TAG, "requestOptions: ");
            return;
        }

        Glide.with(context).load(imageid).centerInside().transition(DrawableTransitionOptions.withCrossFade()).error(R.drawable.ic_perm_).into(imageView);


    }

}
