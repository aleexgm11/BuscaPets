package com.grupo6.buscapets.model;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.grupo6.buscapets.R;

import java.io.IOException;
import java.util.List;

public class ProtectoraDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ImageView imageViewProtectora;
    private TextView textViewProtectoraName, textViewProtectoraDescription;
    private Protectora protectora;
    private MapView mapView;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protectora_detail);

        imageViewProtectora = findViewById(R.id.imageViewProtectoraDetail);
        textViewProtectoraName = findViewById(R.id.textViewProtectoraNameDetail);
        textViewProtectoraDescription = findViewById(R.id.textViewProtectoraDescription);
        // Obtener el objeto Protectora pasado desde el fragmento
        protectora = (Protectora) getIntent().getSerializableExtra("protectora");

        if (protectora != null) {
            textViewProtectoraName.setText(protectora.getName());
            textViewProtectoraDescription.setText("Dirección: " + protectora.getAddress() + "\nTeléfono: " + protectora.getPhone());

            // Cargar la imagen de la protectora
            String imageName = protectora.getImageName().toLowerCase();
            int imageId = getResources().getIdentifier(imageName, "drawable", getPackageName());
            if (imageId != 0) {
                imageViewProtectora.setImageResource(imageId);
            } else {
                imageViewProtectora.setImageResource(R.drawable.ic_launcher_foreground); // Imagen por defecto
            }
        }

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    }

    //Basicamente lo que hace esto es traducir una direccion a coordenadas de Google Maps
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        MapsInitializer.initialize(this);

        if (protectora != null) {

            LatLng location = getLocationFromAddress(protectora.getAddress());

            googleMap.addMarker(new MarkerOptions().position(location).title(protectora.getName()));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 19));

            // Configurar el clic en el mapa para abrir Google Maps
            googleMap.setOnMapClickListener(latLng -> {
                String uri = String.format("geo:%f,%f?q=%s", latLng.latitude, latLng.longitude, protectora.getAddress());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    //Este metodo fue copiado de Google xd
    private LatLng getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(this);
        List<Address> addressList;

        try {
            // Obtener la lista de direcciones que coinciden con la dirección proporcionada
            addressList = coder.getFromLocationName(strAddress, 1); // Solo necesitamos 1 resultado
            // Verificar si se obtuvo alguna dirección
            if (addressList == null || addressList.isEmpty()) {
                return null; // No se encontró ninguna coincidencia
            }
            // Tomar el primer resultado
            Address location = addressList.get(0);
            // Crear y devolver el objeto LatLng con la latitud y longitud del resultado
            return new LatLng(location.getLatitude(), location.getLongitude());
        } catch ( IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
