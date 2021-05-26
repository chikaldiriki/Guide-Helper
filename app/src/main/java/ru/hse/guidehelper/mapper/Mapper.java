package ru.hse.guidehelper.mapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.hse.guidehelper.model.Tour;

public class Mapper {
    @SuppressWarnings("unused")
    public static Tour jsonToTour(JSONObject jsonObject) {
        try {
            return new Tour()
                    .setId(jsonObject.getLong("id"))
                    .setTitle(jsonObject.getString("title"))
                    .setCity(jsonObject.getString("city"))
                    .setDescription(jsonObject.getString("description"))
                    .setGuide(jsonObject.getString("guide"))
                    .setCost(jsonObject.getLong("cost"))
                    .setImage(null);  // TODO byte[] replace on string
        } catch (JSONException ignored) {
        }

        return new Tour();
    }

    public static JSONObject tourToJson(Tour tour) {
        try {
            return new JSONObject()
                    .put("id", 0)
                    .put("title", tour.getTitle())
                    .put("city", tour.getCity())
                    .put("description", tour.getDescription())
                    .put("guide", tour.getGuide())
                    .put("cost", tour.getCost())
                    .put("image", new JSONArray(tour.getImage())); // TODO byte[] replace on string
        } catch (JSONException ignored) {
        }

        return new JSONObject();
    }
}
