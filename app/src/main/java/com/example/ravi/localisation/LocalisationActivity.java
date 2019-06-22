package com.example.ravi.localisation;

import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ravi.R;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_WIFI_STATE;

public class LocalisationActivity extends AppCompatActivity {

    private PlacesClient placesClient;
    List<Place.Field> placeFields;
    private TextView responseView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localisation);

        String apiKey = getString(R.string.places_api_key);

        // initialisation de l'API Places
        Places.initialize(getApplicationContext(), apiKey);
        // géolocalisation
        placesClient = Places.createClient(this);

        // ci-dessous sont listés les champs à ne pas récupérer
        placeFields = getPlaceFields(
                Place.Field.ID,
                Place.Field.LAT_LNG,
                Place.Field.PHOTO_METADATAS,
                Place.Field.PLUS_CODE,
                Place.Field.PRICE_LEVEL,
                Place.Field.USER_RATINGS_TOTAL,
                Place.Field.VIEWPORT,
                Place.Field.ADDRESS_COMPONENTS,
                Place.Field.PHONE_NUMBER,
                Place.Field.WEBSITE_URI,
                Place.Field.OPENING_HOURS
        );

        responseView = findViewById(R.id.response);
        setLoading(false);

        findViewById(R.id.find_current_place_button).setOnClickListener((view) -> findCurrentPlace());
    }

    // vérification des permissions
    private void findCurrentPlace() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(
                    this,
                    "Pour vous proposer des lieux, nous devons avoir accès à votre géolocalisation et à internet",
                    Toast.LENGTH_SHORT)
                    .show();
        }

        if (checkPermission(ACCESS_FINE_LOCATION)) {
            findCurrentPlaceWithPermissions();
        }
    }

    // récupération des lieux aux alentours
    @RequiresPermission(allOf = {ACCESS_FINE_LOCATION, ACCESS_WIFI_STATE})
    private void findCurrentPlaceWithPermissions() {
        setLoading(true);

        FindCurrentPlaceRequest currentPlaceRequest =
                FindCurrentPlaceRequest.newInstance(placeFields);
        Task<FindCurrentPlaceResponse> currentPlaceTask =
                placesClient.findCurrentPlace(currentPlaceRequest);

        currentPlaceTask.addOnSuccessListener(
                (response) ->
                        responseView.setText(StringUtil.stringify(response)));

        currentPlaceTask.addOnFailureListener(
                (exception) -> {
                    exception.printStackTrace();
                    responseView.setText(exception.getMessage());
                });

        currentPlaceTask.addOnCompleteListener(task -> setLoading(false));
    }

    private boolean checkPermission(String permission) {
        boolean hasPermission =
                ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, 0);
        }
        return hasPermission;
    }

    // affichage d'une image de chargement
    private void setLoading(boolean loading) {
        findViewById(R.id.loading).setVisibility(loading ? View.VISIBLE : View.INVISIBLE);
    }

    // récupération des champs attaché à un lieu
    static List<Place.Field> getPlaceFields(Place.Field... placeFieldsToOmit) {

        List<Place.Field> placeFields = new ArrayList<>(Arrays.asList(Place.Field.values()));
        placeFields.removeAll(Arrays.asList(placeFieldsToOmit));

        return placeFields;
    }

}