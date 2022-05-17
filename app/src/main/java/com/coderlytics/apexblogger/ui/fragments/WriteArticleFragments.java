package com.coderlytics.apexblogger.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.coderlytics.apexblogger.databinding.FragmentsWriteArticlesBinding;
import com.coderlytics.apexblogger.utils.MyUtils;

public class WriteArticleFragments extends Fragment {

    FragmentsWriteArticlesBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentsWriteArticlesBinding.inflate(inflater, container, false);


        binding.addArticles.setOnClickListener(view -> {

            String title = String.valueOf(binding.etTitle.getText());
            String content = String.valueOf(binding.etArticles.getText());

            if (title.isEmpty()) {
                binding.etTitleParent.setError("Enter Title");
                binding.etTitle.requestFocus();
                return;
            }

            binding.etTitleParent.setError(null);

            if (content.isEmpty()) {
                binding.etArticlesParent.setError("Enter Articles");
                binding.etArticles.requestFocus();
                return;
            }

            binding.etArticlesParent.setError(null);

            MyUtils.hideKeyboard(requireActivity());

            Toast.makeText(requireActivity(), "All Done", Toast.LENGTH_SHORT).show();

            addArticles();



        });

        return binding.getRoot();
    }

    private void addArticles() {

        //TODO 1 add article pr kam krna he

    }
}
