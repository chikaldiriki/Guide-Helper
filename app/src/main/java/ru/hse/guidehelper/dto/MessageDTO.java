package ru.hse.guidehelper.dto;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class MessageDTO {
    private String id;

    private String chatId;

    private String senderMail;

    private String receiverMail;

    private String senderName;

    private String receiverName;

    private String text;

    private Timestamp dispatchTime;
}
