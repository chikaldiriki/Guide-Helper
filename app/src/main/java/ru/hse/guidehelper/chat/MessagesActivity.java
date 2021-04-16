package ru.hse.guidehelper.chat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.net.URI;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import ru.hse.guidehelper.R;
import ru.hse.guidehelper.dto.ChatDTO;
import ru.hse.guidehelper.dto.MessageDTO;

public class MessagesActivity extends AppCompatActivity {

    private static Chat chat;

    private MessagesList messagesList;
    private MessageInput messageInput;
    private MessagesListAdapter<IMessage> adapter;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        messagesList = findViewById(R.id.messagesList);
        messageInput = findViewById(R.id.input);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        ImageLoader imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, @Nullable String url, @Nullable Object payload) {

                Glide.with(MessagesActivity.this)
                        .load("https://chto-eto-takoe.ru/uryaimg/750dee84f4aa22327320ab543c4f5bab.jpg").into(imageView);
            }
        };

        adapter = new MessagesListAdapter<>("1", imageLoader);

        messageInput.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                //validate and send message
                Message message = new Message(
                        "someId", chat.getId(), chat.getUsers().get(0).getId(),
                        chat.getUsers().get(1).getId(),
                        input.toString(),
                        new Timestamp(new Date().getTime()),
                        chat.getUsers().get(0)
                );

                adapter.addToStart(message, true);
                adapter.update(message);
                return true;
            }
        });

        addAllMessagesInAdapter();

        messagesList.setAdapter(adapter);
    }

    public static void open(Context context, Chat chat) {
        MessagesActivity.chat = chat;
        context.startActivity(new Intent(context, MessagesActivity.class));
    }

    private void addAllMessagesInAdapter() {
        List<MessageDTO> allMessages = new ArrayList<>();
        Client client = null;
        try {
            client = new Client();
            //allMessages = client.getAllMessages("currentUserMail");
        } catch (Exception e) {
        }

        allMessages.stream()
                .map(messageDTO -> new Message(
                                messageDTO.getId(),
                                messageDTO.getChatId(),
                                messageDTO.getSenderMail(),
                                messageDTO.getReceiverMail(),
                                messageDTO.getText(),
                                messageDTO.getDispatchTime(),
                                new User(messageDTO.getSenderMail(),
                                        messageDTO.getSenderName(),
                                        "https://avatarko.ru/img/kartinka/1/avatarko_anonim.jpg")
                        )
                )
                .forEach(message -> {
                    adapter.addToStart(message, true);
                    adapter.update(message);
                });
    }
}