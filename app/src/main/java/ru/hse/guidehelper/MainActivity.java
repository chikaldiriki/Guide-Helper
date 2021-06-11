package ru.hse.guidehelper;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ru.hse.guidehelper.api.RequestHelper;
import ru.hse.guidehelper.model.Order;
import ru.hse.guidehelper.model.Tour;
import ru.hse.guidehelper.model.User;

public class MainActivity extends AppCompatActivity {
    public static NavController navController;
    public static User currentUser = null;
    public static Long currentTourId = null;
    private List<Tour> tours = null;
    private List<Tour> orders = null;
    private Map<Long, Tour> mapIdTour; // Соглашение - это единсвенная map из id в Tour

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (Objects.equals(FirebaseAuth.getInstance().getCurrentUser(), null) && (destination.getId() == R.id.navigation_profile ||
                    destination.getId() == R.id.navigation_notifications ||
                    destination.getId() == R.id.navigation_dashboard)) {
                navController.navigate(R.id.signInFragment);
            }
        });

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            MainActivity.currentUser = RequestHelper.getUser(user.getEmail());
        }
    }

    @Override
    public void onBackPressed() {
        BottomNavigationView navView = this.findViewById(R.id.nav_view);
        navView.setVisibility(BottomNavigationView.VISIBLE);
        super.onBackPressed();
    }

    public List<Tour> getTours() {
        if (tours == null) {
            initTours();
        }

        return tours;
    }

    public Tour getTourById(Long id) {
        return mapIdTour.get(id);
    }

    private void initTours() {
        if (tours == null) {
            tours = RequestHelper.getAllTours();

            mapIdTour = new HashMap<>();
            for (int i = 0; i < tours.size(); i++) {
                Tour tour = tours.get(i);
                mapIdTour.put(tour.getId(), tour);
            }
        }
    }

    public List<Tour> getOrders() {
        if (orders == null) {
            initOrders();
        }
        return orders;
    }

    private void initOrders() {
        if (orders == null) {
            orders = new ArrayList<>();
            List<Order> requestOrders = RequestHelper.getOrdersByUser(MainActivity.currentUser.getUserMail());
            if(requestOrders != null) {
                for(Order order : requestOrders) {
                    orders.add(getTourById(order.getTourId()));
                }
            }
        }
    }

    public void setOrder(Tour tour) {
        if (orders == null) {
            initOrders();
        }
        orders.add(tour);
    }

}