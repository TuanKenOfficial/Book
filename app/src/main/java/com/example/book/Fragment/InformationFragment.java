package com.example.book.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.book.Activity.MusicActivity;
import com.example.book.R;
import com.example.book.databinding.FragmentInformationBinding;


public class InformationFragment extends Fragment {



    private FragmentInformationBinding binding;
    public InformationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentInformationBinding.inflate(LayoutInflater.from(getContext()),container,false);
        binding.btnMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MusicActivity.class));
            }
        });
        return binding.getRoot();
    }
}