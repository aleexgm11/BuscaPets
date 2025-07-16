package com.grupo6.buscapets.database;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.grupo6.buscapets.model.Usuario;

public class FirebaseUserController {

    private static final String TAG = "FirebaseHelper";
    private static final String URL_DATABASE = "https://buscapet-9e045-default-rtdb.europe-west1.firebasedatabase.app/";

    // Método para registrar un nuevo usuario
    public static void registerUser(String email, String password, String name, String age, String phone, String address, FirebaseControllerCallback callback) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Crear un objeto de usuario en Firebase Realtime Database
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();
                            saveUserData(userId, name, age, email, phone, address, callback);
                        }
                    } else {
                        Log.e(TAG, "Error en el registro: " + task.getException().getMessage());
                        callback.onFailure("Error en el registro");
                    }
                });
    }

    private static void saveUserData(String userId, String name, String age, String email, String phone, String address, FirebaseControllerCallback callback) {
        DatabaseReference userRef = FirebaseDatabase.getInstance(URL_DATABASE).getReference("users").child(userId);

        // Crear el objeto usuario
        Usuario userData = new Usuario(name, email, age, phone, address);

        // Guardar los datos en Firebase
        userRef.setValue(userData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Usuario registrado y datos guardados correctamente");
                callback.onSuccess("Usuario registrado y datos guardados correctamente");
            } else {
                Log.e(TAG, "Error al guardar los datos del usuario: " + task.getException().getMessage());
                callback.onFailure("Error al guardar los datos del usuario");
            }
        });
    }


    // Método para iniciar sesión de un usuario
    public static void loginUser(String email, String password, FirebaseControllerCallback callback) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                        Log.d(TAG, "Inicio de sesion exitoso");
                        callback.onSuccess("Inicio de sesion exitoso");
                })
                .addOnFailureListener(e ->{
                    // Si hay un error, capturamos el error aquí
                    if (e instanceof FirebaseAuthException) {
                        FirebaseAuthException firebaseAuthException = (FirebaseAuthException) e;
                        String errorCode = firebaseAuthException.getErrorCode();
                        callback.onFailure(errorCode);
                    } else {
                        Log.e(TAG, "Error inesperado: " + e.getMessage());
                    }

                });

    }

    // Método para leer los datos del usuario desde Firebase Realtime Database
    public static void getUserData(String userId, FirebaseControllerCallback callback) {
        DatabaseReference userRef = FirebaseDatabase.getInstance(URL_DATABASE).getReference("users").child(userId);

        // Leer los datos del usuario
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String age = dataSnapshot.child("age").getValue(String.class);
                    String phone = dataSnapshot.child("phone").getValue(String.class);
                    String address = dataSnapshot.child("address").getValue(String.class);

                    Log.d(TAG, "Usuario actualizado correctamente");
                    callback.onSuccessGetUserData(new Usuario(name, email, age, phone, address), "Usuario actualizado correctamente");
                } else {
                    Log.e(TAG, "No se encontraron datos del usuario");
                    callback.onFailure("No se encontraron datos de usuario");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error al leer los datos: " + databaseError.getMessage());
                callback.onFailure("Error al leer los datos: " + databaseError.getMessage());
            }
        });
    }

    // Método para actualizar los datos del usuario en Realtime Database
    public static void updateUserData(String userId, String phone, String address, FirebaseControllerCallback callback) {
        DatabaseReference userRef = FirebaseDatabase.getInstance(URL_DATABASE).getReference("users").child(userId);

        // Comenzamos con un control básico de errores
        if (userId.isEmpty()) {
            callback.onFailure("El ID de usuario no puede ser nulo o vacío.");
            return;
        }

        if (phone == null && address == null) {
            callback.onFailure("No se proporcionaron datos para actualizar.");
            return;
        }

        // Actualizar los campos de manera individual
        if (phone != null && !phone.isEmpty()) {
            userRef.child("phone").setValue(phone).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Telefono actualizado correctamente");
                } else {
                    Log.d(TAG, "Error al actualizar el telefono: " + task.getException().getMessage());
                    callback.onFailure("Error al actualizar el telefono");
                }
            });
        }

        if (address != null && !address.isEmpty()) {
            userRef.child("address").setValue(address).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Direccion actualizada correctamente");
                } else {
                    Log.d(TAG, "Error al actualizar la direccion: " + task.getException().getMessage());
                    callback.onFailure("Error al actualizar la direccion");
                }
            });
        }

        // Si ambos datos fueron actualizados correctamente
        callback.onSuccess("Datos actualizados correctamente");
    }

    // Método para actualizar la contraseña del usuario en Firebase Authentication
    public static void updateUserPassword(String newPassword, FirebaseControllerCallback callback) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            if (!newPassword.isEmpty()) {
                currentUser.updatePassword(newPassword).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Contrasena actualizada correctamente");
                        callback.onSuccess("Contrasena actualizada correctamente");
                    } else {
                        Log.e(TAG, "Error al actualizar la contrasena: " + task.getException().getMessage());
                        callback.onFailure("Error al actualizar la contrasena");
                    }
                });
            }
        } else {
            Log.e(TAG, "Usuario no autenticado");
            callback.onFailure("Usuario no autenticado");
        }
    }

    // Interfaz de callback para manejar los resultados de las operaciones
    public interface FirebaseControllerCallback {
        void onSuccess(String message);
        void onSuccessGetUserData(Usuario usuario, String message);
        void onFailure(String errorMessage);
    }

}