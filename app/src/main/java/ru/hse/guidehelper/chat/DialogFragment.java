package ru.hse.guidehelper.chat;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import com.stfalcon.chatkit.utils.DateFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import lombok.SneakyThrows;
import ru.hse.guidehelper.MainActivity;
import ru.hse.guidehelper.R;
import ru.hse.guidehelper.api.RequestHelper;
import ru.hse.guidehelper.chat.keywords.KeywordsFilterRecyclerViewAdapter;
import ru.hse.guidehelper.chat.keywords.KeywordsRecyclerViewAdapter;
import ru.hse.guidehelper.dto.ChatDTO;
import ru.hse.guidehelper.model.Chat;
import ru.hse.guidehelper.model.Message;
import ru.hse.guidehelper.model.User;

public class DialogFragment extends Fragment
        implements DialogsListAdapter.OnDialogClickListener<Chat> {

    private DialogsListAdapter<Chat> adapter;
    private TextView emptyChatListTextView = null;
    private final String defaultAvatarUrl = Uri.parse("R.drawable.ic_account_circle_black_36dp").toString();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private static class CustomDateFormatter implements DateFormatter.Formatter {

        @Override
        public String format(Date date) {
            if (DateFormatter.isToday(date)) {
                return DateFormatter.format(date, DateFormatter.Template.TIME);
            } else if (DateFormatter.isYesterday(date)) {
                return "Yesterday";
            } else {
                return DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH_YEAR);
            }
        }
    }

    @SneakyThrows
    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dialog, container, false);

        BottomNavigationView navView = requireActivity().findViewById(R.id.nav_view);
        navView.setVisibility(View.VISIBLE);

        ImageLoader imageLoader = (imageView, url, payload) -> {
            if (url == null) {
                Glide.with(DialogFragment.this.requireActivity())
                        .load(R.drawable.ic_account_circle_black_36dp)
                        .into(imageView);
            } else {
                Glide.with(DialogFragment.this.requireActivity())
                        .load(url)
                        .into(imageView);
            }
        };

        DialogsList chatList = root.findViewById(R.id.chatList);
        emptyChatListTextView = root.findViewById(R.id.emptyChatListTextView);

        adapter = new DialogsListAdapter<>(imageLoader);
        String currentUserMail = MainActivity.currentUser.getUserMail();

        RecyclerView keywordsFilter = root.findViewById(R.id.keywords_filter_list);
        KeywordsFilterRecyclerViewAdapter keywordsAdapter =
                new KeywordsFilterRecyclerViewAdapter(currentUserMail, word -> v -> {
                    this.addChatsInAdapter(RequestHelper.getChatsByKeyword(word));
                    adapter.notifyDataSetChanged();
                    FloatingActionButton backButton = root.findViewById(R.id.buttonBack);
                    backButton.setVisibility(View.VISIBLE);
                    backButton.setOnClickListener(v2 -> {
                        this.addChatsInAdapter(RequestHelper.getDialogs(currentUserMail));
                        adapter.notifyDataSetChanged();
                        backButton.setVisibility(View.INVISIBLE);
                    });
                });
        keywordsFilter.setAdapter(keywordsAdapter);
        ImageButton updateKeywordsButton = root.findViewById(R.id.buttonUpdateKeywords);
        updateKeywordsButton.setOnClickListener(v -> {
            keywordsAdapter.updatePopularKeywords(currentUserMail);
            keywordsAdapter.notifyDataSetChanged();
        });

        adapter.setOnDialogClickListener(this);

        adapter.setOnDialogViewLongClickListener(new DialogsListAdapter.OnDialogViewLongClickListener<Chat>() {
            @Override
            public void onDialogViewLongClick(View view, Chat dialog) {

                String secondUser = dialog.getUsers().get(0).getUserMail();

                RecyclerView recyclerView = root.findViewById(R.id.keywordslist_list);
                ImageButton buttonView = root.findViewById(R.id.buttonClose);
                buttonView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(new KeywordsRecyclerViewAdapter(currentUserMail, secondUser));
                buttonView.setOnClickListener(v -> {
                    recyclerView.setVisibility(View.INVISIBLE);
                    buttonView.setVisibility(View.INVISIBLE);
                });
            }
        });

        chatList.setAdapter(adapter);

        adapter.setDatesFormatter(new CustomDateFormatter());
        addChatsInAdapter(RequestHelper.getDialogs(currentUserMail));

        adapter.sort(new Comparator<Chat>() {
            @Override
            public int compare(Chat o1, Chat o2) {
                return o1.getDialogName().compareTo(o2.getDialogName());
            }
        });
        adapter.notifyDataSetChanged();

        return root;
    }

    public void addChatsInAdapter(List<ChatDTO> allChats) {
        if (allChats == null || allChats.isEmpty()) {
            emptyChatListTextView.setVisibility(View.VISIBLE);
            return;
        }
        emptyChatListTextView.setVisibility(View.INVISIBLE);

        adapter.clear();


        List<String> listChatIds = allChats
                .stream()
                .map(chatDTO -> RequestHelper.getChatId(chatDTO.getFirstUserMail(), chatDTO.getSecondUserMail()))
                .collect(Collectors.toList());

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();


        AtomicInteger countBindedDialogs = new AtomicInteger();
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
                        } else {
                            return;
                        }

                        countBindedDialogs.getAndIncrement();

                        Chat chat = new Chat(listChatIds.get(finalI),
                                anotherUserName,
                                anotherUserAvatarUrl,
                                new ArrayList<>(
                                        Collections.singletonList(new User()
                                                .setUserMail(anotherUserMail)
                                                .setName(anotherUserName)
                                                .setAvatarUrl(anotherUserAvatarUrl))), lastMessage.get().setUser(new User()), 0);

                        adapter.addItem(chat);
                        adapter.notifyDataSetChanged();
                        adapter.sortByLastMessageDate();
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
