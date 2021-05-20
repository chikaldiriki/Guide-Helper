package ru.hse.guidehelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.Objects;

import ru.hse.guidehelper.config.ApplicationConfig;
import ru.hse.guidehelper.dto.UserDTO;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button mButtonToChat;
    public static int currentFragmentId = R.id.nav_host_fragment;
    public static NavController navController;
    private BottomNavigationView navView;
    public static UserDTO currentUser = null;
    public static String currentTourId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navView = findViewById(R.id.nav_view);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        mButtonToChat = findViewById(R.id.buttonToChat);
        mAuth = FirebaseAuth.getInstance();

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull @NotNull NavController controller,
                                             @NonNull @NotNull NavDestination destination,
                                             @Nullable @org.jetbrains.annotations.Nullable Bundle arguments) {
                if (Objects.equals(FirebaseAuth.getInstance().getCurrentUser(), null) && (destination.getId() == R.id.navigation_profile ||
                        destination.getId() == R.id.navigation_notifications ||
                        destination.getId() == R.id.navigation_dashboard)) {
                    navController.navigate(R.id.signInFragment);
                }
            }
        });

        mButtonToChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.dialogFragment2);
            }
        });

        File cacheFile = new File(getCacheDir(), "userCache.txt");
        ApplicationConfig.setCachedUserDTOfile(cacheFile);

        if (!ApplicationConfig.cachedUserDTOfile.exists()) {
            try {
                ApplicationConfig.cachedUserDTOfile.createNewFile();
            } catch (IOException e) {
            }
        } else {
            currentUser = readUserFromFile(ApplicationConfig.cachedUserDTOfile);
        }
    }

    public static void writeUserToFile(File file, UserDTO userDTO) {
        try (FileOutputStream outputStream = new FileOutputStream(file);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(userDTO);
        } catch (IOException e) {
        }
    }

    public static UserDTO readUserFromFile(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            return (UserDTO) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}