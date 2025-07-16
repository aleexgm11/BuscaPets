package com.grupo6.buscapets.model;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.grupo6.buscapets.R;

import java.util.ArrayList;
import java.util.List;

public class PetDetailActivity extends AppCompatActivity {

    private Pet pet;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_detail);

        TextView textViewPetNameDetail = findViewById(R.id.textViewPetNameDetail);
        TextView textViewPetType = findViewById(R.id.textViewPetType);
        TextView textViewPetBreed = findViewById(R.id.textViewPetBreed); // Añadir esta línea
        TextView textViewPetAge = findViewById(R.id.textViewPetAge);
        TextView textViewPetPersonality = findViewById(R.id.textViewPetPersonality);
        TextView textViewPetWeight = findViewById(R.id.textViewPetWeight);
        TextView textViewPetHeight = findViewById(R.id.textViewPetHeight);
        TextView textViewPetFood = findViewById(R.id.textViewPetFood);
        TextView textViewPetProtectora = findViewById(R.id.textViewPetProtectora);

        Button buttonAdopt = findViewById(R.id.buttonAdopt);

        // Obtener el objeto Pet pasado desde MainActivity
        pet = (Pet) getIntent().getSerializableExtra("pet");

        if (pet != null) {
            textViewPetNameDetail.setText(pet.getName());
            textViewPetType.setText("Tipo: " + pet.getType());
            textViewPetBreed.setText("Raza: " + pet.getBreed()); // Modificar aquí para usar textViewPetBreed
            textViewPetAge.setText("Edad: " + pet.getAge() + " años");
            textViewPetPersonality.setText("Personalidad: " + pet.getPersonality());
            textViewPetWeight.setText("Peso: " + pet.getWeight());
            textViewPetHeight.setText("Altura: " + pet.getHeight());
            textViewPetFood.setText("Comida favorita: " + pet.getFavoriteFood());
            textViewPetProtectora.setText("Protectora: " + pet.getProtectora());

            int count = 1;
            List<Integer> imageList = new ArrayList<>();
            while (true) {
                String resourceName = pet.getImageName() + (count + 1);
                int resId = getResources().getIdentifier(resourceName, "drawable", getPackageName());
                if (resId == 0) {
                    break;
                }
                imageList.add(resId);
                count++;
            }

            if (imageList.isEmpty()) {
                imageList.add(R.drawable.notfound);
            }

            // Configurar ViewPager2 con el adaptador
            ViewPager2 viewPager = findViewById(R.id.viewSlider);
            ImageSliderAdapter adapter = new ImageSliderAdapter(this, imageList);
            viewPager.setAdapter(adapter);
        }

        // Configurar el clic del botón "Adoptar"
        buttonAdopt.setOnClickListener(v -> {
            Intent intent = new Intent(PetDetailActivity.this, com.grupo6.buscapets.AdoptionFormActivity.class);
            intent.putExtra("pet", pet); // Pasar información de la mascota al formulario
            startActivity(intent);
        });
    }
}
