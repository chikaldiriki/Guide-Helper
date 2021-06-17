package ru.hse.guidehelper.model;

import com.stfalcon.chatkit.commons.models.IDialog;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Accessors(chain = true)
public class Chat implements IDialog<Message> {

    private String id; // id чата
    private String dialogName; // имя собеседника
    private String dialogPhoto; // фото собеседника
    private ArrayList<User> users; // собеседник
    private Message lastMessage; // последнее сообщение
    private int unreadCount; // кол-во непрочитанных сообщений

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDialogPhoto() {
        return dialogPhoto;
    }

    @Override
    public String getDialogName() {
        return dialogName;
    }

    @Override
    public ArrayList<User> getUsers() {
        return users;
    }

    @Override
    public Message getLastMessage() {
        return lastMessage;
    }

    @Override
    public void setLastMessage(Message message) {
        this.lastMessage = message;
    }

    @Override
    public int getUnreadCount() {
        return unreadCount;
    }
}
