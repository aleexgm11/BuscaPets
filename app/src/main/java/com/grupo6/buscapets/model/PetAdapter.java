package com.grupo6.buscapets.model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.grupo6.buscapets.R;

import java.util.ArrayList;
import java.util.Locale;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.ViewHolder> {

    private ArrayList<Pet> petList;
    private ArrayList<Pet> petListFull; // Copia para restaurar al filtrar

    public PetAdapter(ArrayList<Pet> petList) {
        this.petList = petList;
        this.petListFull = new ArrayList<>(petList);
    }

    @NonNull
    @Override
    public PetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pet, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PetAdapter.ViewHolder holder, int position) {
        Pet pet = petList.get(position);
        holder.textViewPetName.setText(pet.getName());
        holder.textViewPetType.setText("Tipo: " + pet.getType());
        holder.textViewPetBreed.setText("Raza: " + pet.getBreed());
        holder.textViewPetAge.setText("Edad: " + pet.getAge() + " años");
        holder.textViewPetWeight.setText("Peso: " + pet.getWeight());
        holder.textViewPetHeight.setText("Altura: " + pet.getHeight());
        holder.textViewPetFood.setText("Comida favorita: " + pet.getFavoriteFood());
        holder.textViewPetPersonality.setText("Personalidad: " + pet.getPersonality());
        holder.textViewPetProtectora.setText("Protectora: " + pet.getProtectora()); // Add protectora

        // Cargar la imagen
        Context context = holder.itemView.getContext();
        String imageName = pet.getImageName().toLowerCase();
        int imageId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        if (imageId != 0) {
            holder.imageViewPet.setImageResource(imageId);
        } else {
            holder.imageViewPet.setImageResource(R.drawable.notfound); // Imagen por defecto
        }

        // Manejar el clic en el item para abrir el detalle del animal
        holder.itemView.setOnClickListener(v -> openPetDetailActivity(context, pet));
    }

    private void openPetDetailActivity(Context context, Pet pet) {
        Intent intent = new Intent(context, PetDetailActivity.class);
        intent.putExtra("pet", pet);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    /**
     * Método para filtrar la lista basada en múltiples criterios
     */
    public void filterAdvanced(String name, String animalType, String race, String age, String personality) {
        petList.clear();

        for (Pet pet : petListFull) {
            boolean matches = true;

            // Filtrar por nombre
            if (!name.isEmpty()) {
                if (!pet.getName().toLowerCase(Locale.getDefault()).contains(name.toLowerCase(Locale.getDefault()))) {
                    matches = false;
                }
            }

            // Filtrar por tipo de animal
            if (!animalType.isEmpty() && !animalType.equalsIgnoreCase("Todos")) {
                if (!pet.getType().equalsIgnoreCase(animalType)) {
                    matches = false;
                }
            }

            // Filtrar por raza
            if (!race.isEmpty() && !race.equalsIgnoreCase("Todos")) {
                if (!pet.getBreed().equalsIgnoreCase(race)) {
                    matches = false;
                }
            }

            // Filtrar por edad
            if (!age.isEmpty()) {
                try {
                    int ageFilter = Integer.parseInt(age);
                    if (pet.getAge() != ageFilter) {
                        matches = false;
                    }
                } catch (NumberFormatException e) {
                    // Si la edad no es válida, ignorar el filtro
                }
            }

            // Filtrar por personalidad
            if (!personality.isEmpty() && !personality.equalsIgnoreCase("Todos")) {
                if (!pet.getPersonality().equalsIgnoreCase(personality)) {
                    matches = false;
                }
            }

            if (matches) {
                petList.add(pet);
            }
        }

        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageViewPet;
        public TextView textViewPetName;
        public TextView textViewPetType;
        public TextView textViewPetBreed;
        public TextView textViewPetAge;
        public TextView textViewPetWeight;
        public TextView textViewPetHeight;
        public TextView textViewPetFood;
        public TextView textViewPetPersonality;
        public TextView textViewPetProtectora;

        public ViewHolder(View itemView) {
            super(itemView);
            imageViewPet = itemView.findViewById(R.id.imageViewPet);
            textViewPetName = itemView.findViewById(R.id.textViewPetName);
            textViewPetType = itemView.findViewById(R.id.textViewPetType);
            textViewPetBreed = itemView.findViewById(R.id.textViewPetBreed);
            textViewPetAge = itemView.findViewById(R.id.textViewPetAge);
            textViewPetWeight = itemView.findViewById(R.id.textViewPetWeight);
            textViewPetHeight = itemView.findViewById(R.id.textViewPetHeight);
            textViewPetFood = itemView.findViewById(R.id.textViewPetFood);
            textViewPetPersonality = itemView.findViewById(R.id.textViewPetPersonality);
            textViewPetProtectora = itemView.findViewById(R.id.textViewPetProtectora);
        }
    }

}
