package com.velykoivanstanislav.animals.view;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.velykoivanstanislav.animals.databinding.FragmentDetailBinding;
import com.velykoivanstanislav.animals.model.AnimalModel;
import com.velykoivanstanislav.animals.model.AnimalPalette;
import com.velykoivanstanislav.animals.util.Util;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;


public class DetailFragment extends Fragment {
    private AnimalModel animal;

    private FragmentDetailBinding binding;

    public DetailFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            animal =  DetailFragmentArgs.fromBundle(getArguments()).getAnimalModel();
        }
        if (animal != null) {
            binding.setAnimal(animal);
            setupBackgroundColor(animal.imageUrl);
        }
    }

    private void setupBackgroundColor(String url) {
        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@androidx.annotation.NonNull Bitmap resource, @androidx.annotation.Nullable Transition<? super Bitmap> transition) {
                        Palette.from(resource)
                                .generate(palette -> {
                                    if (binding == null || palette == null) {
                                        return;
                                    }
                                    Palette.Swatch color = palette.getLightMutedSwatch();
                                    if (color != null) {
                                        int intColor = color.getRgb();
                                        AnimalPalette animalPalette = new AnimalPalette(intColor);
                                        binding.setPalette(animalPalette);
                                    }
                                });
                    }

                    @Override
                    public void onLoadCleared(@androidx.annotation.Nullable Drawable placeholder) {

                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

