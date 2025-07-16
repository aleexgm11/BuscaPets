package com.grupo6.buscapets.model;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.grupo6.buscapets.database.FirebaseUserController;
import com.grupo6.buscapets.R;

public class RegisterActivity extends AppCompatActivity {

    private final String URL_DATABASE = "https://buscapet-9e045-default-rtdb.europe-west1.firebasedatabase.app/";

    private EditText nameEditText, ageEditText, emailEditText, phoneEditText, addressEditText, passwordEditText;

    private FirebaseAuth auth;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        //Button registerButton = findViewById(R.id.botonRegistro);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializa Firebase Auth y Database
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance(URL_DATABASE)
                .getReference("users");

        // Referencias a las vistas
        nameEditText = findViewById(R.id.campoNombreEditText);
        ageEditText = findViewById(R.id.campoEdadEditText);
        emailEditText = findViewById(R.id.campoCorreoEditText);
        phoneEditText = findViewById(R.id.campoTelefonoEditText);
        addressEditText = findViewById(R.id.campoDireccionEditText);
        passwordEditText = findViewById(R.id.campoContrasenaEditText);
        Button registerButton = findViewById(R.id.botonRegistro);

        registerButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String age = ageEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();
            String address = addressEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (validateInputs(name, age, email, phone, address, password)) {

                FirebaseUserController.registerUser(email, password, name, age, phone, address, new FirebaseUserController.FirebaseControllerCallback() {
                    @Override
                    public void onSuccess(String message) {
                        Log.d("RegisterActivity", message);

                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);

                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent);
                    }

                    @Override
                    public void onSuccessGetUserData(Usuario usuario, String message) {

                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Log.e("RegisterActivity", errorMessage);
                        Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
    }

    private boolean validateInputs(String name, String age, String email, String phone, String address, String password) {
        if (name.isEmpty()) {
            nameEditText.setError("El nombre es obligatorio");
            return false;
        }
        if (age.isEmpty()) {
            ageEditText.setError("La edad es obligatoria");
            return false;
        }
        if (email.isEmpty()) {
            emailEditText.setError("El correo es obligatorio");
            return false;
        }
        if (phone.isEmpty()) {
            phoneEditText.setError("El número de teléfono es obligatorio");
            return false;
        }
        if (address.isEmpty()) {
            addressEditText.setError("La dirección es obligatoria");
            return false;
        }
        if (password.isEmpty() || password.length() < 6) {
            passwordEditText.setError("La contraseña debe tener al menos 6 caracteres");
            return false;
        }
        return true;
    }

    private void registerUser(String name, String age, String email, String phone, String address, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            // Guardar datos adicionales en la base de datos
                            String userId = user.getUid();
                            Usuario userData = new Usuario(name, email, age, phone, address);
                            database.child(userId).setValue(userData)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Log.d("Register", "Usuario registrado exitosamente.");
                                        } else {
                                            Log.e("Register", "Error al guardar datos: " + task1.getException());
                                        }
                                    });
                        }
                    } else {
                        Log.e("Register", "Error al registrar usuario: " + task.getException());
                    }
                });
    }

}