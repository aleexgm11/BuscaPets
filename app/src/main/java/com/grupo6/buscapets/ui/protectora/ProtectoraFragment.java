package com.grupo6.buscapets.ui.protectora;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.grupo6.buscapets.model.Protectora;
import com.grupo6.buscapets.model.ProtectoraAdapter;
import com.grupo6.buscapets.R;
import com.grupo6.buscapets.databinding.FragmentProtectoraBinding;

import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;

public class ProtectoraFragment extends Fragment {

    private final String URL_DATABASE = "https://buscapet-9e045-default-rtdb.europe-west1.firebasedatabase.app/";

    private FragmentProtectoraBinding binding;
    private RecyclerView recyclerViewProtectoras;
    private ProtectoraAdapter protectoraAdapter;
    private ArrayList<Protectora> protectoraList, protectoraListFull;

    private DatabaseReference mDatabase;
    private SearchView searchView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProtectoraBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inicializar el RecyclerView
        recyclerViewProtectoras = root.findViewById(R.id.recyclerViewProtectoras);
        recyclerViewProtectoras.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializar el SearchView
        searchView = root.findViewById(R.id.searchView);
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setHintTextColor(getResources().getColor(android.R.color.black));


        // Cargar los datos de las protectoras, incluyendo las imágenes de la base de datos
        mDatabase = FirebaseDatabase.getInstance(URL_DATABASE).getReference("protectoras");
        protectoraList = new ArrayList<>();
        protectoraListFull = new ArrayList<>();
        protectoraAdapter = new ProtectoraAdapter(protectoraList);
        recyclerViewProtectoras.setAdapter(protectoraAdapter);

        loadProtectorasFromDatabase();

        // Configurar la barra de búsqueda
        setupSearchView();



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loadProtectorasFromDatabase() {
        // Leer los datos desde la base de datos
        mDatabase.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                protectoraList.clear();
                protectoraListFull.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Protectora protectora = snapshot.getValue(Protectora.class);
                    protectoraList.add(protectora);
                    protectoraListFull.add(protectora);
                }
                protectoraAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Error al leer datos", databaseError.toException());
            }
        });
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterProtectoras(query.trim());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterProtectoras(newText.trim());
                return false;
            }
        });
    }

    private void filterProtectoras(String query) {
        protectoraList.clear();
        if (query.isEmpty()) {
            protectoraList.addAll(protectoraListFull);
        } else {
            for (Protectora protectora : protectoraListFull) {
                if (protectora.getName().toLowerCase().contains(query.toLowerCase())) {
                    protectoraList.add(protectora);
                }
            }
        }
        protectoraAdapter.notifyDataSetChanged();
    }
}
