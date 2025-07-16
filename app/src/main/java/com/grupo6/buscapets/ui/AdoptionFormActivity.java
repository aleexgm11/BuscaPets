package com.grupo6.buscapets;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.grupo6.buscapets.model.MainActivity;

public class AdoptionFormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adoption_form);

        // Referencias a los campos del formulario
        EditText editTextActivityPreference = findViewById(R.id.editTextActivityPreference);
        EditText editTextPersonalityPreference = findViewById(R.id.editTextPersonalityPreference);
        EditText editTextExperience = findViewById(R.id.editTextExperience);
        EditText editTextSpace = findViewById(R.id.editTextSpace);
        Button buttonSubmitForm = findViewById(R.id.buttonSubmitForm);

        // Configurar el botón de enviar
        buttonSubmitForm.setOnClickListener(v -> {
            String activityPreference = editTextActivityPreference.getText().toString().trim();
            String personalityPreference = editTextPersonalityPreference.getText().toString().trim();
            String experience = editTextExperience.getText().toString().trim();
            String space = editTextSpace.getText().toString().trim();

            // Validar los campos
            if (activityPreference.isEmpty() || personalityPreference.isEmpty() ||
                    experience.isEmpty() || space.isEmpty()) {
                Toast.makeText(AdoptionFormActivity.this, "Por favor, completa todas las preguntas.", Toast.LENGTH_LONG).show();
            } else {
                // Mostrar un mensaje de confirmación
                Toast.makeText(AdoptionFormActivity.this, "¡Solicitud enviada! Gracias por completar el formulario.", Toast.LENGTH_LONG).show();

                // Redirigir a la pantalla principal (Home)
                Intent intent = new Intent(AdoptionFormActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Limpia las actividades anteriores
                startActivity(intent);
                finish(); // Cierra la actividad actual
            }
        });
    }
}
