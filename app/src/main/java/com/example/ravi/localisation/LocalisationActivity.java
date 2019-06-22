package com.example.ravi.localisation;

import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ravi.Authentification.User;
import com.example.ravi.R;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.vikramezhil.droidspeech.DroidSpeech;
import com.vikramezhil.droidspeech.OnDSListener;
import com.vikramezhil.droidspeech.OnDSPermissionsListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_WIFI_STATE;

public class LocalisationActivity extends AppCompatActivity implements OnDSListener, OnDSPermissionsListener {

    int placeNum = 0;
    private DroidSpeech displayDroidSpeech;
    private PlacesClient placesClient;
    List<Place.Field> placeFields;

    private TextView responseView;

    List<String> preferences;
    List<Place> places = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localisation);

        Intent intent = getIntent();
        String data = intent.getStringExtra("preferences");
        String replace = data.replace("[","");
        String replace1 = replace.replace("]","");
        preferences = new ArrayList<>(Arrays.asList(replace1.split(",")));
        preferences.add("ESTABLISHMENT");

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

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);

        displayDroidSpeech = new DroidSpeech(getApplicationContext(), null);
        displayDroidSpeech.setOnDroidSpeechListener(this);
        displayDroidSpeech.setShowRecognitionProgressView(false);
        displayDroidSpeech.startDroidSpeechRecognition();
    }

    // vérification des permissions
    private void findPlacesAround() {
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
            findPlacesAroundWithPermissions();
        }
    }

    // récupération des lieux aux alentours
    @RequiresPermission(allOf = {ACCESS_FINE_LOCATION, ACCESS_WIFI_STATE})
    private void findPlacesAroundWithPermissions() {
        setLoading(true);

        FindCurrentPlaceRequest currentPlaceRequest =
                FindCurrentPlaceRequest.newInstance(placeFields);
        Task<FindCurrentPlaceResponse> currentPlaceTask =
                placesClient.findCurrentPlace(currentPlaceRequest);

        currentPlaceTask.addOnSuccessListener(
                (response) ->
                        matchWithPreferences(response));

        currentPlaceTask.addOnFailureListener(
                (exception) -> {
                    exception.printStackTrace();
                    responseView.setText(exception.getMessage());
                });

        currentPlaceTask.addOnCompleteListener(task -> setLoading(false));
    }

    private void matchWithPreferences(FindCurrentPlaceResponse response) {
        placeNum = 0;
        for(int i = 0; i < response.getPlaceLikelihoods().size(); i++) {
            for(String preference: preferences) {
                if(response.getPlaceLikelihoods().get(i).getPlace().getTypes().toString().contains(preference)) {
                    places.add(response.getPlaceLikelihoods().get(i).getPlace());
                    i++;
                }
            }
        }
        nextPlace();
    }

    private void nextPlace() {
        if(placeNum < places.size()) {
            TextView name = findViewById(R.id.placeName);
            name.setText(places.get(placeNum).getName());
            responseView.setText(places.get(placeNum).getAddress());
            placeNum++;
        }
        else {
            responseView.setText("Nous n'avons plus de lieux à vous proposer, veuillez refaire une recherche.");
        }

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

    @Override
    public void onDroidSpeechSupportedLanguages(String currentSpeechLanguage, List<String> supportedSpeechLanguages) {
        if(supportedSpeechLanguages.contains("fr-FR")) {
            displayDroidSpeech.setPreferredLanguage("fr-FR");
        }
    }

    @Override
    public void onDroidSpeechRmsChanged(float rmsChangedValue) {

    }

    @Override
    public void onDroidSpeechLiveResult(String liveSpeechResult) {

    }

    @Override
    public void onDroidSpeechFinalResult(String finalSpeechResult) {
        // si le mot "chercher" est prononcé, on cherche les lieux aux alentours
        if (finalSpeechResult.equalsIgnoreCase("Chercher") ||
                finalSpeechResult.toLowerCase().contains("chercher")) {
            findPlacesAround();
        }

        // si le mot "suivant" est prononcé, on affiche un autre lieux
        if (finalSpeechResult.equalsIgnoreCase("Suivant") ||
                finalSpeechResult.toLowerCase().contains("suivant")) {
            nextPlace();
        }
    }

    @Override
    public void onDroidSpeechClosedByUser() {

    }

    @Override
    public void onDroidSpeechError(String errorMsg) {
        displayDroidSpeech.closeDroidSpeechOperations();
        responseView.setText("La reconnaissance vocale a rencontré un problème : " + errorMsg);

        displayDroidSpeech.closeDroidSpeechOperations();
    }

    @Override
    public void onDroidSpeechAudioPermissionStatus(boolean audioPermissionGiven, String errorMsgIfAny) {
        if(audioPermissionGiven) {
            displayDroidSpeech.startDroidSpeechRecognition();
        }
        else {
            if(errorMsgIfAny != null)
                responseView.setText("La reconnaissance vocale a rencontré un problème : " + errorMsgIfAny);

            displayDroidSpeech.closeDroidSpeechOperations();
        }
    }
}