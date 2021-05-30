package ru.hse.guidehelper.chat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import ru.hse.guidehelper.MainActivity;
import ru.hse.guidehelper.R;
import ru.hse.guidehelper.api.RequestHelper;
import ru.hse.guidehelper.dto.ChatDTO;
import ru.hse.guidehelper.model.Chat;
import ru.hse.guidehelper.model.User;

public class DialogFragment extends Fragment
        implements DialogsListAdapter.OnDialogClickListener<Chat> {

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
                .load(url)
                .into(imageView);

        DialogsList chatList = root.findViewById(R.id.chatList);

        adapter = new DialogsListAdapter<>(imageLoader);

        /*adapter.addItem(new Chat("1", "FirstUser", "https://avatarko.ru/img/kartinka/1/avatarko_anonim.jpg",
                new ArrayList<>(Collections.singletonList(new User("1", "aziz", "https://avatarko.ru/img/kartinka/1/avatarko_anonim.jpg"))),
                null, 0));*/

        addAllChatsInAdapter();

        adapter.setOnDialogClickListener(this);

        chatList.setAdapter(adapter);
        return root;
    }

    private void addAllChatsInAdapter() {
        List<ChatDTO> allChats = RequestHelper.getDialogs(MainActivity.currentUser.getUserMail());
        if (allChats.isEmpty()) {
            return;
        }

        List<String> listChatIds = allChats
                .stream()
                .map(chatDTO -> RequestHelper.getChatId(chatDTO.getFirstUserMail(), chatDTO.getSecondUserMail()))
                .collect(Collectors.toList());

        for (int i = 0; i < allChats.size(); i++) {
            User anotherUser = RequestHelper.getUser(allChats.get(0).getFirstUserName().equals(MainActivity.currentUser.getUserMail())
                    ? allChats.get(0).getSecondUserMail() :
                    allChats.get(0).getFirstUserMail());

            Chat chat = new Chat(listChatIds.get(i),
                    anotherUser.getName(),
                    anotherUser.getPhotoUrl(),
                    new ArrayList<>(
                            Collections.singletonList(new User()
                                    .setUserMail(anotherUser.getUserMail())
                                    .setName(anotherUser.getName())
                                    .setPhotoUrl(anotherUser.getPhotoUrl()))),
                    null, 0);

            adapter.addItem(chat);
        }
    }

    @Override
    public void onDialogClick(Chat chat) {
        MessagesFragment.setChat(chat);

        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.messagesFragment2);
    }
}