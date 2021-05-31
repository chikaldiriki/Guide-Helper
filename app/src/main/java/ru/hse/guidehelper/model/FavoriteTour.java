package ru.hse.guidehelper.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class FavoriteTour {

    private String userMail;

    private Long tourId;
}
