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
import static android.Manifest.permission.RECORD_AUDIO;

public class LocalisationActivity extends AppCompatActivity implements OnDSListener, OnDSPermissionsListener {

    // reconnaissance vocale
    private DroidSpeech displayDroidSpeech;

    // API Places
    private PlacesClient placesClient;
    List<Place> places = new ArrayList<>();
    int placeNum = 0;

    // affichage
    private TextView responseView;
    private TextView name;

    // préférences utilisateur
    List<String> preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localisation);

        // récupération de l'affichage
        responseView = findViewById(R.id.response);
        name = findViewById(R.id.placeName);
        setLoading(false);

        // récupération des préférences
        getPreferences();

        // initialisation de l'API Places
        Places.initialize(getApplicationContext(), getString(R.string.places_api_key));
        placesClient = Places.createClient(this);

        ActivityCompat.requestPermissions(this, new String[]{RECORD_AUDIO}, 1);

        // initialisation de la reconnaissance vocale
        displayDroidSpeech = new DroidSpeech(getApplicationContext(), null);
        displayDroidSpeech.setOnDroidSpeechListener(this);
        displayDroidSpeech.setShowRecognitionProgressView(false);
        displayDroidSpeech.startDroidSpeechRecognition();
    }

    // récupération des préférences
    private void getPreferences() {
        Intent intent = getIntent();
        String data = intent.getStringExtra("preferences");
        String replace = data.replace("[","");
        String replace1 = replace.replace("]","");
        preferences = new ArrayList<>(Arrays.asList(replace1.split(",")));
        preferences.add("ESTABLISHMENT");
    }

    // lorsque l'activité passe en pause on arrête la reconnaissance vocale
    @Override
    protected void onPause() {
        super.onPause();
        displayDroidSpeech.closeDroidSpeechOperations();
    }

    // lorsque l'activité est de nouveau active on reprend la reconnaissance vocale
    @Override
    protected void onResume() {
        super.onResume();
        displayDroidSpeech.startDroidSpeechRecognition();
    }

    // vérification des permissions pour accéder à l'API Places
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
        placeNum = 0;
        setLoading(true);

        FindCurrentPlaceRequest currentPlaceRequest =
                FindCurrentPlaceRequest.newInstance(getPlaceFields());
        Task<FindCurrentPlaceResponse> currentPlaceTask =
                placesClient.findCurrentPlace(currentPlaceRequest);

        currentPlaceTask.addOnSuccessListener(
                (response) -> {
                    matchWithPreferences(response);
                    nextPlace();
                });

        currentPlaceTask.addOnFailureListener(
                (exception) -> {
                    exception.printStackTrace();
                    responseView.setText(exception.getMessage());
                });

        currentPlaceTask.addOnCompleteListener(task -> setLoading(false));
    }

    // filtrage des lieux qui correspondent aux préférences de l'utilisateur
    private void matchWithPreferences(FindCurrentPlaceResponse response) {
        places = new ArrayList<>();
        for(int i = 0; i < response.getPlaceLikelihoods().size(); i++) {
            for(String preference: preferences) {
                if(response.getPlaceLikelihoods().get(i).getPlace().getTypes().toString().contains(preference)) {
                    places.add(response.getPlaceLikelihoods().get(i).getPlace());
                    i++;
                }
            }
        }
    }

    // affichage d'un lieu en suivant l'ordre de la liste des lieux qui pourrait intéresser l'utilisateur
    private void nextPlace() {
        if(placeNum < places.size()) {

            name.setText(places.get(placeNum).getName());
            responseView.setText(places.get(placeNum).getAddress());
            placeNum++;
        }
        else {
            name.setText("Oups...");
            responseView.setText("Nous n'avons plus de lieux à vous proposer, veuillez refaire une recherche.");
        }

    }

    // demande de permission
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

    // déclaration des champs que l'on souhaite récupérer avec l'API Places
    static List<Place.Field> getPlaceFields() {

        List<Place.Field> placeFields = new ArrayList<>(Arrays.asList(Place.Field.values()));
        placeFields.removeAll(Arrays.asList(
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
        ));

        return placeFields;
    }


    // langue pour la reconnaissance vocale
    @Override
    public void onDroidSpeechSupportedLanguages(String currentSpeechLanguage, List<String> supportedSpeechLanguages) {
        if(supportedSpeechLanguages.contains("fr-FR")) {
            displayDroidSpeech.setPreferredLanguage("fr-FR");
        }
    }

    @Override
    public void onDroidSpeechRmsChanged(float rmsChangedValue) { }

    @Override
    public void onDroidSpeechLiveResult(String liveSpeechResult) { }

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
    public void onDroidSpeechClosedByUser() { }

    // en cas d'erreur de la reconnaissance vocale, on affiche un message
    @Override
    public void onDroidSpeechError(String errorMsg) {
        displayDroidSpeech.closeDroidSpeechOperations();
        responseView.setText("La reconnaissance vocale a rencontré un problème : " + errorMsg);

        displayDroidSpeech.closeDroidSpeechOperations();
    }

    // en cas de permission manquante, on affiche un message
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