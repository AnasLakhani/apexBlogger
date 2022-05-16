package com.coderlytics.apexblogger.interfaces;

import android.view.View;

import com.google.firebase.firestore.DocumentSnapshot;

public interface OnItemClickListener {
    void onReadClick(DocumentSnapshot documentSnapshot);

    void onItemClick(DocumentSnapshot documentSnapshot, View view);
}