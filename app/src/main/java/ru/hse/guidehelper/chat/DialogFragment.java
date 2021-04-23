package ru.hse.guidehelper.chat;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.hse.guidehelper.R;
import ru.hse.guidehelper.dto.ChatDTO;

import static ru.hse.guidehelper.MainActivity.currentFragmentId;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DialogFragment extends Fragment
        implements DialogsListAdapter.OnDialogClickListener<Chat> {

    private DialogsList chatList;
    private DialogsListAdapter<Chat> adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DialogFragment newInstance(String param1, String param2) {
        DialogFragment fragment = new DialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dialog, container, false);


        ImageLoader imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, @Nullable String url, @Nullable Object payload) {

                Glide.with(DialogFragment.this.getActivity())
                        .load("https://avatarko.ru/img/kartinka/1/avatarko_anonim.jpg").into(imageView);
            }
        };

        chatList = root.findViewById(R.id.chatList);

        adapter = new DialogsListAdapter<>(imageLoader);

        adapter.addItem(new Chat("1", "FirstUser", "https://avatarko.ru/img/kartinka/1/avatarko_anonim.jpg",
                new ArrayList<>(Collections.singletonList(new User("1", "aziz", "https://avatarko.ru/img/kartinka/1/avatarko_anonim.jpg"))),
                null, 0));
        adapter.addItem(new Chat("2", "SecondUser", "https://avatarko.ru/img/kartinka/1/avatarko_anonim.jpg",
                new ArrayList<>(Collections.singletonList(new User("2", "aziz", "https://avatarko.ru/img/kartinka/1/avatarko_anonim.jpg"))),
                null, 0));

        addAllChatsInAdapter();

        adapter.setOnDialogClickListener(this);

        chatList.setAdapter(adapter);
        return root;
    }

    private void addAllChatsInAdapter() {
        List<ChatDTO> allChats = new ArrayList<>();

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
        MessagesFragment.setChat(chat);
        Fragment chosenFragment = new MessagesFragment();
        this.getActivity().getSupportFragmentManager().beginTransaction().replace(currentFragmentId,
                chosenFragment).commit();

        currentFragmentId = chosenFragment.getId();
    }
}