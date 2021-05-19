package ru.hse.guidehelper.chat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.hse.guidehelper.MainActivity;
import ru.hse.guidehelper.R;
import ru.hse.guidehelper.dto.ChatDTO;
import ru.hse.guidehelper.model.Chat;
import ru.hse.guidehelper.model.User;

public class DialogFragment extends Fragment
        implements DialogsListAdapter.OnDialogClickListener<Chat> {

    private DialogsList chatList;
    private DialogsListAdapter<Chat> adapter;

    public DialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dialog, container, false);

        ImageLoader imageLoader = (imageView, url, payload) -> Glide
                .with(DialogFragment.this.requireActivity())
                .load("https://avatarko.ru/img/kartinka/1/avatarko_anonim.jpg")
                .into(imageView);

        chatList = root.findViewById(R.id.chatList);

        adapter = new DialogsListAdapter<>(imageLoader);

        /*adapter.addItem(new Chat("1", "FirstUser", "https://avatarko.ru/img/kartinka/1/avatarko_anonim.jpg",
                new ArrayList<>(Collections.singletonList(new User("1", "aziz", "https://avatarko.ru/img/kartinka/1/avatarko_anonim.jpg"))),
                null, 0));
        adapter.addItem(new Chat("2", "SecondUser", "https://avatarko.ru/img/kartinka/1/avatarko_anonim.jpg",
                new ArrayList<>(Collections.singletonList(new User("2", "aziz", "https://avatarko.ru/img/kartinka/1/avatarko_anonim.jpg"))),
                null, 0));

        adapter.addItem(new Chat("3", "Ilya1", "https://avatarko.ru/img/kartinka/1/avatarko_anonim.jpg",
                new ArrayList<>(Collections.singletonList(new User("228", "Ilya2", "https://avatarko.ru/img/kartinka/1/avatarko_anonim.jpg"))),
                null, 0));

        adapter.addItem(new Chat("5", "TestUser", "https://avatarko.ru/img/kartinka/1/avatarko_anonim.jpg",
                new ArrayList<>(Collections.singletonList(new User("322", "TestUser", "https://avatarko.ru/img/kartinka/1/avatarko_anonim.jpg"))),
                null, 0));*/

        addAllChatsInAdapter();

        adapter.setOnDialogClickListener(this);

        chatList.setAdapter(adapter);
        return root;
    }

    private void addAllChatsInAdapter() {
        List<ChatDTO> allChats = new ArrayList<>(); // getAllChats

        allChats.stream()
                .map(chatDTO -> new Chat(
                        chatDTO.getFirstUserMail() + chatDTO.getSecondUserMail(),
                        chatDTO.getFirstUserName().equals(MainActivity.currentUser.getUserMail()) ? chatDTO.getSecondUserName() : chatDTO.getFirstUserName(),
                        "https://avatarko.ru/img/kartinka/1/avatarko_anonim.jpg",
                        new ArrayList<>(chatDTO.getFirstUserName().equals(MainActivity.currentUser.getUserMail()) ?
                                Collections.singletonList(new User()
                                        .setUserMail(chatDTO.getSecondUserMail())
                                        .setName(chatDTO.getSecondUserName())
                                        .setPhotoUrl(chatDTO.getSecondUserPhotoUrl())) :
                                Collections.singletonList(new User()
                                        .setUserMail(chatDTO.getFirstUserMail())
                                        .setName(chatDTO.getFirstUserName())
                                        .setPhotoUrl(chatDTO.getFirstUserPhotoUrl()))
                        ),
                        null, 0
                ))
                .forEach(chat -> adapter.addItem(chat));
    }

    @Override
    public void onDialogClick(Chat chat) {
        MessagesFragment.setChat(chat);

        BottomNavigationView navView = requireActivity().findViewById(R.id.nav_view);
        navView.setVisibility(BottomNavigationView.INVISIBLE);

        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.messagesFragment2);
    }
}