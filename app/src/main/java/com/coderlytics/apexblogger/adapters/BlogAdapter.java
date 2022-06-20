package com.coderlytics.apexblogger.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coderlytics.apexblogger.databinding.LayoutBlogsBinding;
import com.coderlytics.apexblogger.model.BlogsResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class BlogAdapter extends FirestoreAdapter<BlogAdapter.MyRecyclerViewHolder> {

    private final OnItemClickListener listener;

    private boolean isProfile = false;

    public interface OnItemClickListener {

        void onShareClick(DocumentSnapshot documentSnapshot);

        void onEditClick(DocumentSnapshot documentSnapshot);

        void onDeleteClick(DocumentSnapshot documentSnapshot);

        void onItemClick(DocumentSnapshot documentSnapshot);

    }

    protected BlogAdapter(Query query, OnItemClickListener listener,boolean isProfile) {
        super(query);
        this.listener = listener;
        this.isProfile = isProfile;
    }

    @NonNull
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BlogAdapter.MyRecyclerViewHolder(LayoutBlogsBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewHolder holder, int position) {
        holder.bind(getSnapshot(position), listener,isProfile);

    }

    static class MyRecyclerViewHolder extends RecyclerView.ViewHolder {

        LayoutBlogsBinding binding;

        private static final String TAG = "MyRecyclerViewHolder";

        MyRecyclerViewHolder(@NonNull LayoutBlogsBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

        }

        void bind(final DocumentSnapshot snapshot,
                  final OnItemClickListener listener,boolean isProfile) {

            BlogsResponse model = snapshot.toObject(BlogsResponse.class);
            binding.setModel(model);
            binding.setSnapshot(snapshot);
            binding.setOnClick(listener);
            binding.setIsProfile(isProfile);
            long update_date = model.getUpdated_at().toDate().getTime();
            String updated = DateUtils.getRelativeTimeSpanString(update_date).toString();
            binding.setUpdated(updated);


            StorageReference mImageRef =
                    FirebaseStorage.getInstance().getReference("users/" + model.getUpdated_by());
            final long ONE_MEGABYTE = 1024 * 1024;
            mImageRef.getBytes(ONE_MEGABYTE)
                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            DisplayMetrics dm = new DisplayMetrics();
                            Activity activity = (Activity)  binding.getRoot().getContext();
                            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

                            binding.profileImage.setMinimumHeight(dm.heightPixels);
                            binding.profileImage.setMinimumWidth(dm.widthPixels);
                            binding.profileImage.setImageBitmap(bm);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });
//            binding.setImage(image);


            Log.d(TAG, "bind: ");


        }
    }


}