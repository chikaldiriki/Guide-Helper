package ru.hse.guidehelper.chat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ru.hse.guidehelper.chat.Client.*;

import ru.hse.guidehelper.R;
import ru.hse.guidehelper.dto.ChatDTO;

import javax.xml.stream.*;

public class DialogsActivity extends AppCompatActivity
        implements DialogsListAdapter.OnDialogClickListener<Chat> {

    private DialogsList chatList;
    private DialogsListAdapter<Chat> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogs);

        ImageLoader imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, @Nullable String url, @Nullable Object payload) {

                Glide.with(DialogsActivity.this).load("https://avatarko.ru/img/kartinka/1/avatarko_anonim.jpg").into(imageView);
            }
        };

        chatList = findViewById(R.id.chatList);

        adapter = new DialogsListAdapter<>(imageLoader);

        /*adapter.addItem(new Chat("1", "FirstUser", "https://avatarko.ru/img/kartinka/1/avatarko_anonim.jpg",
                new ArrayList<User>(Collections.singletonList(new User("1", "aziz", "https://avatarko.ru/img/kartinka/1/avatarko_anonim.jpg"))),
                null, 0));
        adapter.addItem(new Chat("2", "SecondUser", "https://avatarko.ru/img/kartinka/1/avatarko_anonim.jpg",
                new ArrayList<User>(Collections.singletonList(new User("2", "aziz", "https://avatarko.ru/img/kartinka/1/avatarko_anonim.jpg"))),
                null, 0));*/

        addAllChatsInAdapter();

        adapter.setOnDialogClickListener(this);

        chatList.setAdapter(adapter);
    }

    private void addAllChatsInAdapter() {
        List<ChatDTO> allChats = new ArrayList<>();
        Client client = null;
        try {
            client = new Client();
            //allChats = client.getAllChats();
        } catch (Exception e) {
            System.out.println(1);
        }

        /*Client finalClient = client;
        allChats.stream()
                .map(chatDTO -> new Chat(
                        chatDTO.getId(),
                        chatDTO.getFirstUserName().equals(finalClient.getUserId()) ? chatDTO.getSecondUserName() : chatDTO.getFirstUserName(),
                        "https://avatarko.ru/img/kartinka/1/avatarko_anonim.jpg",
                        new ArrayList<>(Arrays.asList(
                                new User(
                                        chatDTO.getFirstUserMail(),
                                        chatDTO.getFirstUserName(),
                                        "https://avatarko.ru/img/kartinka/1/avatarko_anonim.jpg"
                                ),
                                new User(
                                        chatDTO.getSecondUserMail(),
                                        chatDTO.getSecondUserName(),
                                        "https://avatarko.ru/img/kartinka/1/avatarko_anonim.jpg"
                                )
                        )),
                        null, 0
                ))
                .forEach(chat -> adapter.addItem(chat));*/
    }

    @Override
    public void onDialogClick(Chat chat) {
        MessagesActivity.open(this, chat);
    }
}