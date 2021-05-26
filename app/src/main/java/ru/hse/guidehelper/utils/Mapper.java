package ru.hse.guidehelper.utils;

import org.json.JSONException;
import org.json.JSONObject;

import ru.hse.guidehelper.model.Tour;

public class Mapper {
    public static Tour jsonToTour(JSONObject jsonObject) throws JSONException {
        return new Tour()
                .setId((Long) jsonObject.get("id"))
                .setTitle((String) jsonObject.get("title"))
                .setCity((String) jsonObject.get("city"))
                .setDescription((String) jsonObject.get("description"))
                .setGuide((String) jsonObject.get("guide"))
                .setCost((Long) jsonObject.get("cost"))
                .setImage((Byte[]) jsonObject.get("image"));
    }

    public static JSONObject tourToJson(Tour tour) throws JSONException {
        return new JSONObject()
                .put("id", 0)
                .put("title", tour.getTitle())
                .put("city", tour.getCity())
                .put("description", tour.getDescription())
                .put("guide", tour.getGuide())
                .put("cost", tour.getCost())
                .put("image", tour.getImage());
    }
}
