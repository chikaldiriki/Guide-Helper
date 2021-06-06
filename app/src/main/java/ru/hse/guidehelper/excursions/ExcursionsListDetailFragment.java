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
import ru.hse.guidehelper.model.Chat;
import ru.hse.guidehelper.chat.MessagesFragment;
import ru.hse.guidehelper.model.FavoriteTour;
import ru.hse.guidehelper.model.Tour;
import ru.hse.guidehelper.model.User;
import ru.hse.guidehelper.ui.navigationbar.excursion.AllTourRecyclerViewAdapter;

public class ExcursionsListDetailFragment extends Fragment {
    public static final String ARG_TOUR_ID = "tour_id";
    private Tour tour;
    private FloatingActionButton fabSub;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        BottomNavigationView navView = requireActivity().findViewById(R.id.nav_view);
        navView.setVisibility(BottomNavigationView.INVISIBLE);

        View root = inflater.inflate(R.layout.fragment_excursions_detail, container, false);
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.detail_toolbar);

        AppCompatActivity activity = (AppCompatActivity) this.getActivity();
        assert activity != null;
        activity.setSupportActionBar(toolbar);

        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putLong(ARG_TOUR_ID, MainActivity.currentTourId);

            Tour curTour = AllTourRecyclerViewAdapter.getTourById(MainActivity.currentTourId);
            FloatingActionButton fab = root.findViewById(R.id.fab);
            fab.setOnClickListener(view -> {
                String guideMail = curTour.getGuide();
                String userMail = MainActivity.currentUser.getUserMail();

                String chatId = RequestHelper.getChatId(guideMail, userMail);
                User guide = RequestHelper.getUser(guideMail);
                Chat chat = new Chat(chatId,
                        guideMail,
                        guide.getAvatarUrl(),
                        new ArrayList<>(Collections.singletonList(guide)),
                        null,
                        0);

                MessagesFragment.setChat(chat);
                MainActivity.navController.navigate(R.id.messagesFragment2);
            });

            fabSub = root.findViewById(R.id.fab_subscriptions);
            fabSub.setOnClickListener(new fabSubViewOnClickListener(root));

            if (arguments.containsKey(ARG_TOUR_ID)) {
                tour = AllTourRecyclerViewAdapter.getTourById(arguments.getLong(ARG_TOUR_ID));

                CollapsingToolbarLayout appBarLayout = root.findViewById(R.id.toolbar_layout);
                if (appBarLayout != null) {
                    appBarLayout.setTitle(tour.getTitle());
                }
            }

            if (tour != null) {
                ((TextView) root.findViewById(R.id.excursionslist_detail_container)).setText(tour.getDescription());
            }
        }

        return root;
    }

    private class fabSubViewOnClickListener implements View.OnClickListener {
        private boolean isAddedToFavorite;
        private final View root;

        public fabSubViewOnClickListener(View root) {
            this.root = root;
            isAddedToFavorite = MainActivity.currentUser != null &&
                    RequestHelper.isFavorite(MainActivity.currentUser.getUserMail(), MainActivity.currentTourId);
            if (isAddedToFavorite) {
                fabSub.setImageDrawable(ContextCompat.getDrawable(root.getContext(), R.drawable.ic_subscriptions_fullblack_24));
            } else {
                fabSub.setImageDrawable(ContextCompat.getDrawable(root.getContext(), R.drawable.ic_subscriptions_black_24dp));
            }
        }

        @Override
        public void onClick(View v) {
            if (MainActivity.currentUser == null) {
                MainActivity.navController.navigate(R.id.signInFragment);
                return;
            }

            FavoriteTour tour = new FavoriteTour()
                    .setUserMail(MainActivity.currentUser.getUserMail())
                    .setTourId(MainActivity.currentTourId);

            if (isAddedToFavorite) {
                RequestHelper.deleteFavoriteTour(tour.getUserMail(), tour.getTourId());
                fabSub.setImageDrawable(ContextCompat.getDrawable(root.getContext(), R.drawable.ic_subscriptions_black_24dp));
                isAddedToFavorite = false;
            } else {
                RequestHelper.addFavoriteTour(tour);
                fabSub.setImageDrawable(ContextCompat.getDrawable(root.getContext(), R.drawable.ic_subscriptions_fullblack_24));
                isAddedToFavorite = true;
            }
        }
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
