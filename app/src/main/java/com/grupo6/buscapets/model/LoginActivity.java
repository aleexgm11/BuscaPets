package com.grupo6.buscapets.model;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.grupo6.buscapets.database.FirebaseUserController;
import com.grupo6.buscapets.R;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        Button buttonRegister = findViewById(R.id.buttonRegister);

        mAuth = FirebaseAuth.getInstance();

        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            // Comprobación de campos vacíos
            if (email.isEmpty()) {
                editTextEmail.setError("El campo de correo electrónico no puede estar vacío");
                editTextEmail.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                editTextPassword.setError("El campo de contraseña no puede estar vacío");
                editTextPassword.requestFocus();
                return;
            }

            FirebaseUserController.loginUser(email, password, new FirebaseUserController.FirebaseControllerCallback() {
                @Override
                public void onSuccess(String message) {
                    Log.d("LoginActivity", message);

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }

                @Override
                public void onSuccessGetUserData(Usuario usuario, String message) {

                }

                @Override
                public void onFailure(String errorMessage) {
                    Log.e("LoginActivity", errorMessage);

                    // Diferenciar el tipo de error por código
                    switch (errorMessage) {
                        case "ERROR_INVALID_EMAIL":
                            Log.e("LoginActivity", "El correo electrónico no es válido.");
                            editTextEmail.setError("Correo electrónico no válido");
                            break;
                        case "ERROR_WRONG_PASSWORD":
                            Log.e("LoginActivity", "La contraseña es incorrecta.");
                            editTextPassword.setError("Contraseña no válida");
                            break;
                        case "ERROR_USER_NOT_FOUND":
                            Log.e("LoginActivity", "No existe un usuario con ese correo electrónico.");
                            Toast.makeText(LoginActivity.this, "No existe usuario con este correo", Toast.LENGTH_SHORT).show();
                            break;
                        case "ERROR_INVALID_CREDENTIAL":
                            Log.e("LoginActivity", "Credenciales inválidas (correo o contraseña incorrectos).");
                            editTextPassword.setError("Contraseña no válida");
                            Toast.makeText(LoginActivity.this, "Credenciales inválidas", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Log.e("LoginActivity", "Error en el inicio de sesión: " + errorMessage);
                            Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
        });

        buttonRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
        }
    }
}
