package ru.hse.guidehelper.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class TourOrder extends Tour {

    public TourOrder(Tour tour, String date) {
        this.setCity(tour.getCity());
        this.setCost(tour.getCost());
        this.setDescription(tour.getDescription());
        this.setGuide(tour.getGuide());
        this.setId(tour.getId());
        this.setTitle(tour.getTitle());
        this.setImage(tour.getImage());
        this.setDate(date);
    }

    private String date;
}
