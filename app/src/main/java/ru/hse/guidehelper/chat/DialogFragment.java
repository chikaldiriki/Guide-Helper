package ru.hse.guidehelper.chat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import ru.hse.guidehelper.MainActivity;
import ru.hse.guidehelper.R;
import ru.hse.guidehelper.api.RequestHelper;
import ru.hse.guidehelper.dto.ChatDTO;
import ru.hse.guidehelper.model.Chat;
import ru.hse.guidehelper.model.Message;
import ru.hse.guidehelper.model.User;

public class DialogFragment extends Fragment
        implements DialogsListAdapter.OnDialogClickListener<Chat> {

    private DialogsListAdapter<Chat> adapter;
    private TextView emptyChatListTextView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dialog, container, false);

        BottomNavigationView navView = requireActivity().findViewById(R.id.nav_view);
        navView.setVisibility(View.VISIBLE);

        ImageLoader imageLoader = (imageView, url, payload) -> Glide
                .with(DialogFragment.this.requireActivity())
                .load(url)
                .into(imageView);

        DialogsList chatList = root.findViewById(R.id.chatList);
        emptyChatListTextView = root.findViewById(R.id.emptyChatListTextView);

        adapter = new DialogsListAdapter<>(imageLoader);

        adapter.setOnDialogClickListener(this);
        adapter.sortByLastMessageDate();

        chatList.setAdapter(adapter);

        addAllChatsInAdapter();

        return root;
    }

    private void addAllChatsInAdapter() {
        List<ChatDTO> allChats = RequestHelper.getDialogs(MainActivity.currentUser.getUserMail());
        if (allChats.isEmpty()) {
            emptyChatListTextView.setVisibility(View.VISIBLE);
            return;
        }
        emptyChatListTextView.setVisibility(View.INVISIBLE);


        List<String> listChatIds = allChats
                .stream()
                .map(chatDTO -> RequestHelper.getChatId(chatDTO.getFirstUserMail(), chatDTO.getSecondUserMail()))
                .collect(Collectors.toList());

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();


        for (int i = 0; i < allChats.size(); i++) {
            ChatDTO currentChatDTO = allChats.get(i);

            String anotherUserMail = currentChatDTO.getFirstUserMail().equals(MainActivity.currentUser.getUserMail())
                    ? currentChatDTO.getSecondUserMail()
                    : currentChatDTO.getFirstUserMail();

            String anotherUserName = currentChatDTO.getFirstUserMail().equals(MainActivity.currentUser.getUserMail())
                    ? currentChatDTO.getSecondUserName()
                    : currentChatDTO.getFirstUserName();

            String anotherUserAvatarUrl = currentChatDTO.getFirstUserMail().equals(MainActivity.currentUser.getUserMail())
                    ? currentChatDTO.getSecondUserPhoto()
                    : currentChatDTO.getFirstUserPhoto();

            AtomicReference<Message> lastMessage = new AtomicReference<>(null);

            int finalI = i;
            mDatabase.getReference()
                    .child("messages")
                    .child(listChatIds.get(i))
                    .limitToLast(1)
                    .get()
                    .addOnCompleteListener(task -> {
                        Iterator<DataSnapshot> childrenIterator = task.getResult().getChildren().iterator();
                        if (childrenIterator.hasNext()) {
                            lastMessage.set(childrenIterator.next().getValue(Message.class));
                        }

                        Chat chat = new Chat(listChatIds.get(finalI),
                                anotherUserName,
                                anotherUserAvatarUrl,
                                new ArrayList<>(
                                        Collections.singletonList(new User()
                                                .setUserMail(anotherUserMail)
                                                .setName(anotherUserName)
                                                .setAvatarUrl(anotherUserAvatarUrl))), lastMessage.get().setUser(new User()), 0);

                        adapter.addItem(chat);
                    });
        }
    }

    @Override
    public void onDialogClick(Chat chat) {
        MessagesFragment.setChat(chat);

        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.messagesFragment2);
    }
}