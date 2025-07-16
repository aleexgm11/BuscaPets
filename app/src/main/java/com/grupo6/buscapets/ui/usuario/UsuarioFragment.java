package com.grupo6.buscapets.ui.usuario;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.grupo6.buscapets.database.FirebaseUserController;
import com.grupo6.buscapets.R;
import com.grupo6.buscapets.model.Usuario;
import com.grupo6.buscapets.databinding.FragmentUsuarioBinding;

public class UsuarioFragment extends Fragment {

    private final String URL_DATABASE = "https://buscapet-9e045-default-rtdb.europe-west1.firebasedatabase.app/";

    private EditText editName, editAge, editEmail, editPhone, editAddress, editPassword;

    private String userId;

    private FragmentUsuarioBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        UsuarioViewModel usuarioViewModel =
                new ViewModelProvider(this).get(UsuarioViewModel.class);

        binding = FragmentUsuarioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inicializar vistas
        editName = root.findViewById(R.id.edit_name);
        editAge = root.findViewById(R.id.edit_age);
        editEmail = root.findViewById(R.id.edit_email);
        editPhone = root.findViewById(R.id.edit_phone);
        editAddress = root.findViewById(R.id.edit_address);
        editPassword = root.findViewById(R.id.edit_password);
        Button btnSave = root.findViewById(R.id.btn_save);

        // Inicializar Firebase
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();

            FirebaseUserController.getUserData(userId, new FirebaseUserController.FirebaseControllerCallback() {
                @Override
                public void onSuccess(String message) {

                }

                @Override
                public void onSuccessGetUserData(Usuario usuario, String message) {

                    Log.d("LoadUser", message);

                    String name = usuario.getName();
                    String email = usuario.getEmail();
                    String age = usuario.getAge();
                    String phone = usuario.getPhone();
                    String address = usuario.getAddress();

                    loadUserInfo(name, email, age, phone, address);
                }

                @Override
                public void onFailure(String errorMessage) {
                    Log.e("LoadUser", errorMessage);
                }
            });
        }

        // Guardar cambios
        btnSave.setOnClickListener(v -> {

            String newPhone = editPhone.getText().toString().trim();
            String newAddress = editAddress.getText().toString().trim();
            String newPassword = editPassword.getText().toString().trim();

            FirebaseUserController.updateUserData(userId, newPhone, newAddress, new FirebaseUserController.FirebaseControllerCallback() {
                @Override
                public void onSuccess(String message) {
                    Log.d("UpdateUserData", message);
                    Toast.makeText(root.getContext(), message, Toast.LENGTH_LONG).show();

                    // Tras haber sido exitosa la actualizacion vuelve a la pantalla home
                    NavController navController = Navigation.findNavController(root);
                    navController.navigate(R.id.navigation_home);

                }

                @Override
                public void onSuccessGetUserData(Usuario usuario, String message) {

                }

                @Override
                public void onFailure(String errorMessage) {
                    Log.e("UpdateUserData", errorMessage);
                    Toast.makeText(root.getContext(), errorMessage, Toast.LENGTH_LONG).show();
                }
            });

            FirebaseUserController.updateUserPassword(newPassword, new FirebaseUserController.FirebaseControllerCallback() {
                @Override
                public void onSuccess(String message) {
                    Log.d("UpdatePassword", message);
                    editPassword.setText("");
                    Toast.makeText(root.getContext(), message, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onSuccessGetUserData(Usuario usuario, String message) {

                }

                @Override
                public void onFailure(String errorMessage) {
                    Log.e("UpdatePassword", errorMessage);
                    Toast.makeText(root.getContext(), errorMessage, Toast.LENGTH_LONG).show();
                }
            });
        });

        return root;
    }

    private void loadUserInfo(String name, String email, String age, String phone, String address) {
        editName.setText(name);
        editEmail.setText(email);
        editAge.setText(age);
        editPhone.setText(phone);
        editAddress.setText(address);
    }

}

