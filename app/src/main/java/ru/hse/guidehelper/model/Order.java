package ru.hse.guidehelper.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class Order {

    private String customerMail;

    private Long tourId;

    private LocalDateTime tourTime;
}
