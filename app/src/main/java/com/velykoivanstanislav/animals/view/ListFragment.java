package com.velykoivanstanislav.animals.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.velykoivanstanislav.animals.databinding.FragmentListBinding;
import com.velykoivanstanislav.animals.viewmodel.ListViewModel;


public class ListFragment extends Fragment {

    private ListViewModel viewModel;
    private AnimalListAdapter listAdapter = new AnimalListAdapter();
    private FragmentListBinding binding;

    public ListFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ListViewModel.class);

        viewModel.animals.observe(getViewLifecycleOwner(), list -> {
            if (list != null) {
                binding.animalList.setVisibility(View.VISIBLE);
                listAdapter.updateAnimalList(list);
            }
        });

        viewModel.loading.observe(getViewLifecycleOwner(), loading -> {
            binding.loadingView.setVisibility(loading ? View.VISIBLE : View.GONE);
            if (loading) {
                binding.listError.setVisibility(View.GONE);
                binding.animalList.setVisibility(View.GONE);
            }
        });

        viewModel.loadError.observe(getViewLifecycleOwner(), error -> {
            binding.listError.setVisibility(error ? View.VISIBLE : View.GONE);
            if (error) {
                binding.loadingView.setVisibility(View.GONE);
                binding.animalList.setVisibility(View.GONE);
            }
        });

        viewModel.refresh();

        if (binding.animalList != null) {
            binding.animalList.setLayoutManager(new GridLayoutManager(getContext(), 2));
            binding.animalList.setAdapter(listAdapter);
        }
        binding.animalRefreshLayout.setOnRefreshListener(() -> {
            binding.animalList.setVisibility(View.GONE);
            binding.listError.setVisibility(View.GONE);
            binding.loadingView.setVisibility(View.VISIBLE);
            viewModel.hardRefresh();
            binding.animalRefreshLayout.setRefreshing(false);
        });
    }

    @Override
    public void onDestroyView() {
        binding.animalList.setAdapter(null);
        binding = null;
        super.onDestroyView();
    }
}
