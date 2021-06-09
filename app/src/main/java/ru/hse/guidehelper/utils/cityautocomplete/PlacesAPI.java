package ru.hse.guidehelper.utils.cityautocomplete;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PlacesAPI {

    private final String API_KEY = "INSERT_YOUR_GOOGLE_CLOUD_KEY";

    public ArrayList<String> autoComplete(String input) {
        ArrayList<String> arrayList = new ArrayList<>();
        HttpURLConnection connection = null;
        StringBuilder jsonResult = new StringBuilder();
        try {
            StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/autocomplete/json?");
            stringBuilder.append("input=").append(input);
            stringBuilder.append("&types=(cities)");
            stringBuilder.append("&language=ru-RU");
            stringBuilder.append("&key=").append(API_KEY);

            URL url = new URL(stringBuilder.toString());
            connection = (HttpURLConnection) url.openConnection();
            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());

            int read;

            char[] buff = new char[1024];
            while ((read = inputStreamReader.read(buff)) != -1) {
                jsonResult.append(buff, 0, read);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonResult.toString());

            JSONArray predictions = jsonObject.getJSONArray("predictions");

            for (int i = 0; i < predictions.length(); i++) {
                arrayList.add(predictions.getJSONObject(i).get("description").toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayList;
    }
}
