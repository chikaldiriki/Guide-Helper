package ru.hse.guidehelper.model;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.util.Date;

@AllArgsConstructor
@Setter
@Getter
@Accessors(chain = true)
public class Message implements IMessage {

    private String id;

    private String chatId;

    private String senderMail;

    private String receiverMail;

    private String text;

    private Timestamp dispatchTime;

    private IUser user;

    private String name;

    private String imageUrl;

    private String dispatchTimeString;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public IUser getUser() {
        return user;
    }

    @Override
    public Date getCreatedAt() {
        return dispatchTime;
    }

    public Message(String text, String name, String imageUrl) {
        this.text = text;
        this.name = name;
        this.imageUrl = imageUrl;
        this.dispatchTimeString = new Timestamp(System.currentTimeMillis()).toString();
    }

    public Message() {
        this.dispatchTimeString = new Timestamp(System.currentTimeMillis()).toString();
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDispatchTimeString() {
        return this.dispatchTimeString;
    }
}

