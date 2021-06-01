package ru.hse.guidehelper;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Objects;

import ru.hse.guidehelper.config.ApplicationConfig;
import ru.hse.guidehelper.model.User;

public class MainActivity extends AppCompatActivity {
    public static NavController navController;
    public static User currentUser = null;
    public static Long currentTourId = null;

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
                    destination.getId() == R.id.navigation_dashboard ||
                    destination.getId() == R.id.excursionsListDetailActivity)) {
                navController.navigate(R.id.signInFragment);
            }
        });

        File cacheFile = new File(getCacheDir(), "userCache.txt");
        ApplicationConfig.setCachedUserDTOfile(cacheFile);

        if (!ApplicationConfig.cachedUserDTOfile.exists()) {
            try {
                ApplicationConfig.cachedUserDTOfile.createNewFile();
            } catch (IOException ignored) {
            }
        } else {
            currentUser = readUserFromFile(ApplicationConfig.cachedUserDTOfile);
        }
    }

    public static void writeUserToFile(File file, User user) {
        try (FileOutputStream outputStream = new FileOutputStream(file);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(user);
        } catch (IOException ignored) {
        }
    }

    public static User readUserFromFile(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            return (User) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}