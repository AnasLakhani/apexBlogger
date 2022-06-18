package com.coderlytics.apexblogger.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

    public static void share(Context activity, String subject, String body) {
        /*Create an ACTION_SEND Intent*/

        Activity activity1 = (Activity) activity;

        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        /*This will be the actual content you wish you share.*/
        String shareBody = "Here is the share content body";
        /*The type of the content is text, obviously.*/
        intent.setType("text/plain");
        /*Applying information Subject and Body.*/
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        /*Fire!*/
        activity1.startActivity(Intent.createChooser(intent, "Share Using"));
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

        if (imageid.equals("default")) {
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
