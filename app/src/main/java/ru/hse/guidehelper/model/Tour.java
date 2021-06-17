package ru.hse.guidehelper.model;

import java.sql.Time;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class Tour {

    private Long id;

    private String title;

    private String city;

    private String description;

    private String guide;

    private Long cost;

    private String image;

    private int capacity;

    private String duration;
}
