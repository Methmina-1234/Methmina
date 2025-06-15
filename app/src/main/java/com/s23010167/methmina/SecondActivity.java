package com.s23010167.methmina;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;  // <-- Import Intent for navigation
import android.location.Geocoder;
import android.location.Address;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SecondActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText addressInput;
    private Button showLocationButton;
    private Button nextButton;  // Declare button for Next

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        addressInput = findViewById(R.id.addressInput);
        showLocationButton = findViewById(R.id.showLocationButton);
        nextButton = findViewById(R.id.button2);  // Find the Next button by its ID

        // Setup map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Show location button logic
        showLocationButton.setOnClickListener(view -> showLocation());

        // ** Navigation: On click of Next button, open ThirdActivity **
        nextButton.setOnClickListener(view -> {
            Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Set initial map location (example: Colombo, Sri Lanka)
        LatLng initialLocation = new LatLng(6.9271, 79.8612);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 10f));
    }

    private void showLocation() {
        String addressText = addressInput.getText().toString();
        if (addressText.isEmpty()) {
            Toast.makeText(this, "Please enter an address", Toast.LENGTH_SHORT).show();
            return;
        }

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(addressText, 1);
            if (addresses == null || addresses.isEmpty()) {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
                return;
            }

            Address address = addresses.get(0);
            LatLng location = new LatLng(address.getLatitude(), address.getLongitude());

            mMap.clear(); // Remove old markers
            mMap.addMarker(new MarkerOptions().position(location).title(addressText));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f));

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error finding location", Toast.LENGTH_SHORT).show();
        }
    }
}
