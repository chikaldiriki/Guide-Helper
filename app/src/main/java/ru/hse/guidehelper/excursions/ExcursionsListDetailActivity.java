package ru.hse.guidehelper.excursions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.widget.Toolbar;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.hse.guidehelper.MainActivity;
import ru.hse.guidehelper.R;
import ru.hse.guidehelper.model.Chat;
import ru.hse.guidehelper.chat.MessagesFragment;
import ru.hse.guidehelper.model.User;
import ru.hse.guidehelper.ui.bottomNavBar.excursion.ExcursionFragment;
import ru.hse.guidehelper.utils.ClientUtils;

public class ExcursionsListDetailActivity extends AppCompatActivity {

    public static final String ARG_ITEM_ID = "item_id";
    private ExcursionFragment.SimpleItemRecyclerViewAdapter.DummyItem mItem;

    private User getUser(OkHttpClient client, String userMail) throws IOException, JSONException, ExecutionException, InterruptedException {
        Request request = new Request.Builder()
                .url(ClientUtils.url + ClientUtils.suffUsers + "/" + userMail)
                .build();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<User> task = executorService.submit(() -> {
            try (Response response = client.newCall(request).execute()) {
                String res = response.body().string();
                System.out.println(res);

                JSONObject jsonObject = new JSONObject(res);

                return new User()
                        .setUserMail(userMail)
                        .setName(jsonObject.get("firstName") + (String) jsonObject.get("lastName"))
                        .setPhoneNumber((String) jsonObject.get("phoneNumber"))
                        .setCity((String) jsonObject.get("city"))
                        .setDescription((String) jsonObject.get("description"))
                        .setGuide((boolean) jsonObject.get("guide"));
            }
        });


        return task.get();
    }

    private String getChatId(OkHttpClient client, String firstUserMail, String secondUserMail) throws ExecutionException, InterruptedException {
        Request request = new Request.Builder()
                .url(ClientUtils.url + ClientUtils.suffChat + "/"
                        + firstUserMail + "/" + secondUserMail)
                .build();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> task = executorService.submit(() -> {
            try (Response response = client.newCall(request).execute()) {
                String res = response.body().string();
                System.out.println(res);

                return res;
            }
        });


        return task.get();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursionslist_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {

            System.out.println("============== 1 ==============");

            Bundle arguments = new Bundle();
            arguments.putString(ARG_ITEM_ID,
                    getIntent().getStringExtra(ARG_ITEM_ID));

            ExcursionFragment.SimpleItemRecyclerViewAdapter.DummyItem curItem =
                    ExcursionFragment.SimpleItemRecyclerViewAdapter.itemMap
                            .get(getIntent().getStringExtra(ARG_ITEM_ID));

            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @SneakyThrows
                @Override
                public void onClick(View view) {
                    String guideMail = curItem.guideMail;
                    String userMail = MainActivity.currentUser.getUserMail();
                    System.out.println(guideMail);
                    System.out.println(userMail);

                    String chatId = getChatId(ClientUtils.httpClient, guideMail, userMail);
                    User guide =  getUser(ClientUtils.httpClient, guideMail);
                    Chat chat = new Chat(chatId,
                            guideMail,
                            guide.getPhotoUrl(),
                            new ArrayList<>(Collections.singletonList(guide)),
                            null,
                            0);

                    MessagesFragment.setChat(chat);

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.excursionslist_detail_container, new MessagesFragment())
                            .commit();
                }
            });

            if (arguments.containsKey(ARG_ITEM_ID)) {

                mItem = ExcursionFragment.SimpleItemRecyclerViewAdapter.itemMap.get(arguments.getString(ARG_ITEM_ID));

                Activity activity = this;
                CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
                if (appBarLayout != null) {
                    appBarLayout.setTitle(mItem.content);
                }
            }

            if (mItem != null) {
                ((TextView) findViewById(R.id.excursionslist_detail_container)).setText(mItem.details);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, ExcursionFragment.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}