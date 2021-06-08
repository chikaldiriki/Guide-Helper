package ru.hse.guidehelper.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class Order {

    private String customerMail;

    private Long tourId;

    private String tourTime;
}
