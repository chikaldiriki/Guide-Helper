package ru.hse.guidehelper.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ChatDTO {
    private String firstUserMail;

    private String secondUserMail;

    private String firstUserName;

    private String secondUserName;
}