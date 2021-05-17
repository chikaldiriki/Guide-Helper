package ru.hse.guidehelper.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class UserDTO implements Serializable {
    private String userMail;

    private boolean isGuide;

    private String name;

    private String phoneNumber; // для гидов

    private String city; // для гидов

    private String description; // для гидов

    private String photoUrl;
}