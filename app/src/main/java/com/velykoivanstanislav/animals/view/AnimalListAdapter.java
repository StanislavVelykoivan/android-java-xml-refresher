package com.velykoivanstanislav.animals.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.velykoivanstanislav.animals.R;
import com.velykoivanstanislav.animals.databinding.ItemAnimalBinding;
import com.velykoivanstanislav.animals.model.AnimalModel;

import java.util.ArrayList;
import java.util.List;

public class AnimalListAdapter extends RecyclerView.Adapter<AnimalListAdapter.AnimalViewHolder> implements AnimalClickListener {

    private List<AnimalModel> animalList = new ArrayList<>();

    public void updateAnimalList(List<AnimalModel> newAnimalList) {
        animalList.clear();
        animalList.addAll(newAnimalList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AnimalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemAnimalBinding view = DataBindingUtil.inflate(inflater,R.layout.item_animal, parent,false);
        return new AnimalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimalViewHolder holder, int position) {
        holder.itemView.setAnimal(animalList.get(position));
        holder.itemView.setListener(this);
    }

    @Override
    public void onClick(View view) {
        for (AnimalModel animal : animalList) {
            if (view.getTag().equals(animal.name)){
                NavDirections action = ListFragmentDirections.actionGoToDetails(animal);
                Navigation.findNavController(view).navigate(action);
            }
        }
    }

    @Override
    public int getItemCount() {
        return animalList.size();
    }

    class AnimalViewHolder extends RecyclerView.ViewHolder {
        ItemAnimalBinding itemView;

        public AnimalViewHolder(ItemAnimalBinding view) {
            super(view.getRoot());
            itemView = view;
        }
    }
}
