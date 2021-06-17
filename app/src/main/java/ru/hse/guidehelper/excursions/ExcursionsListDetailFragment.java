package ru.hse.guidehelper.excursions;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.hse.guidehelper.MainActivity;
import ru.hse.guidehelper.R;
import ru.hse.guidehelper.api.RequestHelper;
import ru.hse.guidehelper.model.Chat;
import ru.hse.guidehelper.chat.MessagesFragment;
import ru.hse.guidehelper.model.FavoriteTour;
import ru.hse.guidehelper.model.Tour;
import ru.hse.guidehelper.model.TourOrder;
import ru.hse.guidehelper.model.User;

public class ExcursionsListDetailFragment extends Fragment {
    public static final String ARG_TOUR_ID = "tour_id";
    private Tour tour;
    private FloatingActionButton fabFavorite;
    private ExtendedFloatingActionButton fabOrderBook;
    private ExtendedFloatingActionButton fabOrderUnBook;


    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
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

            Tour curTour = ((MainActivity)requireActivity()).getTourById(MainActivity.currentTourId);
            FloatingActionButton fab = root.findViewById(R.id.fab);
            fab.setOnClickListener(view -> {
                if (MainActivity.currentUser == null) {
                    MainActivity.navController.navigate(R.id.signInFragment);
                    return;
                }

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

            fabFavorite = root.findViewById(R.id.fab_subscriptions);
            fabFavorite.setOnClickListener(new FabSubOnClickListener(root));

            fabOrderBook = root.findViewById(R.id.fab_order_book);
            fabOrderBook.setOnClickListener(view -> {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.addOrderFragment);
            });

            View alertDialogView = inflater.inflate(R.layout.linear_layout_order_unbook_warning, null);

            fabOrderUnBook = root.findViewById(R.id.fab_order_unbook);
            fabOrderUnBook.setOnClickListener(new FabUnbookOnClickListener(alertDialogView));

            if(MainActivity.currentTour.getClass() == TourOrder.class) {
                fabOrderBook.setVisibility(View.INVISIBLE);
            } else if (MainActivity.currentTour.getClass() == Tour.class) {
                fabOrderUnBook.setVisibility(View.INVISIBLE);
            }

            if (arguments.containsKey(ARG_TOUR_ID)) {
                tour = ((MainActivity)requireActivity()).getTourById(arguments.getLong(ARG_TOUR_ID));

                CollapsingToolbarLayout appBarLayout = root.findViewById(R.id.toolbar_layout);
                if (appBarLayout != null) {
                    appBarLayout.setTitle(tour.getTitle());
                }
            }

            if (tour != null) {
                ((TextView) root.findViewById(R.id.excursionslist_detail_container)).setText(tour.getDescription());
                if (tour.getImage() != null) {
                    byte[] imageByteArray = Base64.getDecoder().decode(tour.getImage());
                    Bitmap image = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);

                    root.findViewById(R.id.app_bar).setBackground(new BitmapDrawable(getResources(), image));
                }
            }

            TextView tvDuration = root.findViewById(R.id.duration);
            tvDuration.setText(Time.valueOf(MainActivity.currentTour.getDuration()).getHours() + " ч"); // TODO добавить продолжительность



            TextView tvSizeOfGroup = root.findViewById(R.id.sizeOfGroup);
            tvSizeOfGroup.setText(MainActivity.currentTour.getCapacity() + " человек"); // TODO добавить количество экскурсий

            TextView tvCity = root.findViewById(R.id.locationOfTour);
            tvCity.setText(MainActivity.currentTour.getCity());

            TextView tvPrice = root.findViewById(R.id.price);
            tvPrice.setText(MainActivity.currentTour.getCost().toString() + " " + Html.fromHtml(" &#x20bd"));

            User user = RequestHelper.getUser(MainActivity.currentTour.getGuide());
            assert user.getIsGuide(); // ???

            String personImage = user.getAvatarUrl();
            CircleImageView profileImageView = root.findViewById(R.id.profileImage);
            Glide.with(root.getContext()).load(personImage).into(profileImageView);

            TextView tvGuideName = root.findViewById(R.id.excursionGuideName);
            tvGuideName.setText("Гид " + user.getName());

            TextView tvGuideEmail = root.findViewById(R.id.emailGuideDetail);
            tvGuideEmail.setText(user.getId());

            TextView tvGuideNumber = root.findViewById(R.id.mobileNumberGuideDetail);
            tvGuideNumber.setText(user.getPhoneNumber());

            TextView tvDescription = root.findViewById(R.id.descriptionGuideDetail);
            tvDescription.setText(user.getDescription());

        }

        return root;
    }

    private class FabSubOnClickListener implements View.OnClickListener {
        private boolean isAddedToFavorite;
        private final View root;

        public FabSubOnClickListener(View root) {
            this.root = root;
            isAddedToFavorite = MainActivity.currentUser != null &&
                    RequestHelper.isFavorite(MainActivity.currentUser.getUserMail(), MainActivity.currentTourId);
            if (isAddedToFavorite) {
                fabFavorite.setImageDrawable(ContextCompat.getDrawable(root.getContext(), R.drawable.ic_subscriptions_fullblack_24));
            } else {
                fabFavorite.setImageDrawable(ContextCompat.getDrawable(root.getContext(), R.drawable.ic_subscriptions_black_24dp));
            }
        }

        @Override
        public void onClick(View v) {
            if (MainActivity.currentUser == null) {
                MainActivity.navController.navigate(R.id.signInFragment);
                return;
            }

            FavoriteTour favoriteTour = new FavoriteTour()
                    .setUserMail(MainActivity.currentUser.getUserMail())
                    .setTourId(MainActivity.currentTourId);

            Tour tour = ((MainActivity)requireActivity()).getTourById(MainActivity.currentTourId);

            if (isAddedToFavorite) {
                RequestHelper.deleteFavoriteTour(favoriteTour.getUserMail(), favoriteTour.getTourId());
                ((MainActivity)requireActivity()).deleteFavorite(tour);
                fabFavorite.setImageDrawable(ContextCompat.getDrawable(root.getContext(), R.drawable.ic_subscriptions_black_24dp));
                isAddedToFavorite = false;
            } else {
                RequestHelper.addFavoriteTour(favoriteTour);
                ((MainActivity)requireActivity()).addFavorite(tour);
                fabFavorite.setImageDrawable(ContextCompat.getDrawable(root.getContext(), R.drawable.ic_subscriptions_fullblack_24));
                isAddedToFavorite = true;
            }
        }
    }

    private class FabUnbookOnClickListener implements View.OnClickListener {

        private final View alertDialogView;

        public FabUnbookOnClickListener(View alertDialogView) {
            this.alertDialogView = alertDialogView;
        }

        @Override
        public void onClick(View v) {
            if(alertDialogView.getParent() != null) {
                ((ViewGroup)alertDialogView.getParent()).removeView(alertDialogView);
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                    .setTitle("Вы точно уверенны, что хотите отменить бронирование?")
                    .setView(alertDialogView)
                    .setPositiveButton("Да", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            TourOrder tourOrder = ((TourOrder)MainActivity.currentTour);

                            RequestHelper.deleteOrder(MainActivity.currentUser.getId(),
                                    MainActivity.currentTour.getId(), tourOrder.getDate());
                            ((MainActivity)requireActivity()).deleteOrder(tourOrder);

                            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                            navController.navigate(R.id.navigation_dashboard);

                            requireActivity().findViewById(R.id.nav_view).setVisibility(BottomNavigationView.VISIBLE);

                        }

                    }).setNegativeButton("Нет", null);

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
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
