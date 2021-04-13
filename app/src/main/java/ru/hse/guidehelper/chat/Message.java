package ru.hse.guidehelper.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.*;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Accessors(chain = true)
public class Message  {

    private int id;

    private int chatId;

    private String senderMail;

    private String receiverMail;

    private String text;

    private Timestamp dispatchTime;

    private boolean status;

}

