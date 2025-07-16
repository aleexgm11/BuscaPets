package com.grupo6.buscapets.ui.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.io.BufferedReader;
import 	java.io.InputStream;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.grupo6.buscapets.model.Pet;
import com.grupo6.buscapets.model.PetAdapter;
import com.grupo6.buscapets.R;
import com.grupo6.buscapets.databinding.FragmentHomeBinding;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private final String URL_DATABASE = "https://buscapet-9e045-default-rtdb.europe-west1.firebasedatabase.app/";

    private RecyclerView recyclerViewPets;
    private PetAdapter petAdapter;
    private ArrayList<Pet> petList;

    private SearchView searchView;

    private DatabaseReference mDatabase;

    private FragmentHomeBinding binding;

    // Variables para almacenar los criterios de filtro
    private String filterName = "";
    private String filterAnimalType = "Todos";
    private String filterRace = "";
    private String filterAge = "";
    private String filterPersonality = "Todos";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inicializar vistas
        recyclerViewPets = root.findViewById(R.id.recyclerViewProtectoras);
        searchView = root.findViewById(R.id.searchView);
        ImageButton buttonFilter = root.findViewById(R.id.buttonFilter);

        // Configurar el RecyclerView
        recyclerViewPets.setLayoutManager(new LinearLayoutManager(getContext()));

        mDatabase = FirebaseDatabase.getInstance(URL_DATABASE).getReference("mascotas");
        petList = loadPetsFromTxt();

        // Inicializar el adaptador y asignarlo al RecyclerView
        petAdapter = new PetAdapter(petList);
        recyclerViewPets.setAdapter(petAdapter);

        // Configurar el listener para el SearchView
        setupSearchView();

        // Configurar el listener para el botón de filtro
        buttonFilter.setOnClickListener(v -> openFilterDialog());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Método para cargar los datos de los animales desde pets.txt en res/raw
    private ArrayList<Pet> loadPetsFromTxt() {
        ArrayList<Pet> pets = new ArrayList<>();

        try {
            InputStream is = getResources().openRawResource(R.raw.pets);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 10) {
                    String name = parts[0].trim();
                    String type = parts[1].trim();
                    String breed = parts[2].trim();
                    int age = Integer.parseInt(parts[3].trim());
                    String personality = parts[4].trim();
                    String imageName = parts[5].trim();
                    String weight = parts[6].trim();
                    String height = parts[7].trim();
                    String favoriteFood = parts[8].trim();
                    String protectora = parts[9].trim();

                    Pet pet = new Pet(name, type, breed, age, personality, imageName, weight, height, favoriteFood, protectora);
                    pets.add(pet);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pets;
    }
    
    private ArrayList<Pet> loadPetsFromDatabase() {
        ArrayList<Pet> pets = new ArrayList<>();

        // Leer los datos desde la base de datos
        mDatabase.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                petList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Pet pet = snapshot.getValue(Pet.class);
                    petList.add(pet);
                }
                petAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Error al leer datos", databaseError.toException());
            }
        });

        return pets;
    }

    // Configurar el SearchView para filtrar la lista por texto (nombre)
    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterName = query.trim();
                applyFilters();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterName = newText.trim();
                applyFilters();
                return false;
            }
        });
    }

    // Método para abrir el diálogo de filtro
    private void openFilterDialog() {
        // Inflar el layout del diálogo
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_filter, null);

        // Inicializar los elementos del diálogo
        EditText editTextName = dialogView.findViewById(R.id.editTextFilterName);
        EditText editTextRace = dialogView.findViewById(R.id.editTextFilterRace);
        EditText editTextAge = dialogView.findViewById(R.id.editTextFilterAge);
        Spinner spinnerAnimalType = dialogView.findViewById(R.id.spinnerFilterAnimalType);
        Spinner spinnerPersonality = dialogView.findViewById(R.id.spinnerFilterPersonality);

        // Configurar el Spinner de Tipo de Animal
        ArrayAdapter<CharSequence> animalTypeAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.animal_types, android.R.layout.simple_spinner_item);
        animalTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAnimalType.setAdapter(animalTypeAdapter);

        // Configurar el Spinner de Personalidad
        ArrayAdapter<CharSequence> personalityAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.personality_types, android.R.layout.simple_spinner_item);
        personalityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPersonality.setAdapter(personalityAdapter);

        // Restaurar los valores actuales de los filtros
        editTextName.setText(filterName);
        editTextRace.setText(filterRace);
        editTextAge.setText(filterAge);
        if (!filterPersonality.equals("Todos")) {
            int position = personalityAdapter.getPosition(filterPersonality);
            if (position >= 0) {
                spinnerPersonality.setSelection(position);
            }
        }
        if (!filterAnimalType.equals("Todos")) {
            int position = animalTypeAdapter.getPosition(filterAnimalType);
            if (position >= 0) {
                spinnerAnimalType.setSelection(position);
            }
        }

        // Construir el diálogo con botones "Cancelar" y "Aplicar"
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView)
                .setTitle(getString(R.string.filter_title))
                .setCancelable(false)
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Aplicar", (dialog, which) -> {
                    // Obtener los valores seleccionados
                    filterName = editTextName.getText().toString().trim();
                    filterAnimalType = spinnerAnimalType.getSelectedItem().toString().trim();
                    filterRace = editTextRace.getText().toString().trim();
                    filterAge = editTextAge.getText().toString().trim();
                    filterPersonality = spinnerPersonality.getSelectedItem().toString().trim();

                    // Aplicar los filtros
                    applyFilters();
                });

        // Mostrar el diálogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Método para aplicar los filtros al adaptador
    private void applyFilters() {
        petAdapter.filterAdvanced(filterName, filterAnimalType, filterRace, filterAge, filterPersonality);
    }
}
