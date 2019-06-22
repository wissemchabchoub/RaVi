package com.example.ravi.localisation;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;

public final class StringUtil {

    private static final String FIELD_SEPARATOR = "\n";
    private static final String RESULT_SEPARATOR = "\n\n\n";

    static String stringify(FindCurrentPlaceResponse response) {
        StringBuilder builder = new StringBuilder();

        for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                builder
                        .append("Likelihood: ")
                        .append(placeLikelihood.getLikelihood())
                        .append(FIELD_SEPARATOR)
                        .append("Place: ")
                        .append(stringify(placeLikelihood.getPlace()))
                        .append(RESULT_SEPARATOR);
            }

        return builder.toString();
    }

    static String stringify(Place place) {
        return place.getName() + " (" + place.getAddress() + ")" + place.getTypes();
    }
}