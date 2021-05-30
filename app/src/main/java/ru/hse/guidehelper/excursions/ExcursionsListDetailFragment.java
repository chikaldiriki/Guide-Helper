package ru.hse.guidehelper.excursions;

import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import lombok.SneakyThrows;
import ru.hse.guidehelper.MainActivity;
import ru.hse.guidehelper.R;
import ru.hse.guidehelper.api.RequestHelper;
import ru.hse.guidehelper.auth.SignInFragment;
import ru.hse.guidehelper.model.Chat;
import ru.hse.guidehelper.chat.MessagesFragment;
import ru.hse.guidehelper.model.User;
import ru.hse.guidehelper.ui.bottomNavBar.excursion.ExcursionFragment;

public class ExcursionsListDetailFragment extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";
    private ExcursionFragment.SimpleItemRecyclerViewAdapter.DummyItem mItem;

    private static int it_ = 1;

    public ExcursionsListDetailFragment() {
        System.out.println(it_++);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        BottomNavigationView navView = requireActivity().findViewById(R.id.nav_view);
        navView.setVisibility(BottomNavigationView.INVISIBLE);


        View root = inflater.inflate(R.layout.activity_excursionslist_detail, container, false);
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.detail_toolbar);
        //setSupportActionBar(toolbar);

        AppCompatActivity activity = (AppCompatActivity) this.getActivity();
        assert activity != null;
        activity.setSupportActionBar(toolbar);

        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {

            System.out.println("============== 1 ==============");

            Bundle arguments = new Bundle();
            arguments.putString(ARG_ITEM_ID,
                    MainActivity.currentTourId);

            ExcursionFragment.SimpleItemRecyclerViewAdapter.DummyItem curItem =
                    ExcursionFragment.SimpleItemRecyclerViewAdapter.itemMap
                            .get(MainActivity.currentTourId);
            FloatingActionButton fab = root.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @SneakyThrows
                @Override
                public void onClick(View view) {
                    String guideMail = curItem.guideMail;
                    String userMail = MainActivity.currentUser.getUserMail();
                    System.out.println(guideMail);
                    System.out.println(userMail);

                    String chatId = RequestHelper.getChatId(guideMail, userMail);
                    User guide = RequestHelper.getUser(guideMail);
                    Chat chat = new Chat(chatId,
                            guideMail,
                            guide.getAvatarUrl(),
                            new ArrayList<>(Collections.singletonList(guide)),
                            null,
                            0);

                    MessagesFragment.setChat(chat);

//                    activity.getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.excursionslist_detail_container, new MessagesFragment())
//                            .commit();

                    MainActivity.navController.navigate(R.id.messagesFragment2);
                }
            });

            FloatingActionButton fabSub = root.findViewById(R.id.fab_subscriptions);

            fabSub.setOnClickListener(new View.OnClickListener() {
                boolean isAddedToFavorite = true; // TODO get состояние
                @Override
                public void onClick(View v) {
                    if(MainActivity.currentUser == null) {
                        MainActivity.navController.navigate(R.id.signInFragment);
                        return;
                    }
                    if(isAddedToFavorite) {
                        // TODO change состояние
                        fabSub.setImageDrawable(ContextCompat.getDrawable(root.getContext(), R.drawable.ic_subscriptions_fullblack_24));
                        isAddedToFavorite = false;
                    } else {
                        // TODO change состояние
                        fabSub.setImageDrawable(ContextCompat.getDrawable(root.getContext(), R.drawable.ic_subscriptions_black_24dp));
                        isAddedToFavorite = true;
                    }
                }
            });

            if (arguments.containsKey(ARG_ITEM_ID)) {

                mItem = ExcursionFragment.SimpleItemRecyclerViewAdapter.itemMap.get(arguments.getString(ARG_ITEM_ID));

                CollapsingToolbarLayout appBarLayout = root.findViewById(R.id.toolbar_layout);
                if (appBarLayout != null) {
                    appBarLayout.setTitle(mItem.content);
                }
            }

            if (mItem != null) {
                ((TextView) root.findViewById(R.id.excursionslist_detail_container)).setText(mItem.details);
            }
        }


        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        BottomNavigationView navView = requireActivity().findViewById(R.id.nav_view);
        navView.setVisibility(BottomNavigationView.VISIBLE);

        if (id == android.R.id.home) {
            ExcursionsListDetailFragment.this.requireActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
