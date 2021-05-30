package ru.hse.guidehelper.api;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.hse.guidehelper.dto.ChatDTO;
import ru.hse.guidehelper.model.Tour;
import ru.hse.guidehelper.model.User;

public class RequestHelper {

    public static List<Tour> getAllTours() {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        try {
            return singleThreadExecutor.submit(() -> Api.getInstance()
                    .getTourService()
                    .getAllTours().execute().body())
                    .get();
        } catch (ExecutionException | InterruptedException e) {
            Log.e("error", "getAllTours");
            throw new RuntimeException();
        }
    }

    public static void addTour(Tour tour) {
        Api.getInstance()
                .getTourService()
                .addTour(tour)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                        if (!response.isSuccessful()) {
                            Log.e(String.valueOf(response.code()), "getAllTours");
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                        Log.e("error", "addTour");
                    }
                });
    }

    public static User getUser(String userId) {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        try {
            return singleThreadExecutor.submit(() -> Api.getInstance()
                    .getUserService()
                    .getUser(userId).execute().body())
                    .get();
        } catch (ExecutionException | InterruptedException e) {
            Log.e("error", "getAllTours");
            throw new RuntimeException();
        }
    }

    public static String getChatId(String firstUserId, String secondUserId) {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        try {
            return String.valueOf(singleThreadExecutor.submit(() -> Api.getInstance()
                    .getChatService()
                    .getChatId(firstUserId, secondUserId).execute().body())
                    .get());
        } catch (ExecutionException | InterruptedException e) {
            Log.e("error", "getChatId");
            throw new RuntimeException();
        }
    }

    public static List<ChatDTO> getDialogs(String userId) {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        try {
            return singleThreadExecutor.submit(() -> Api.getInstance()
                    .getChatService()
                    .getDialogs(userId).execute().body())
                    .get();
        } catch (ExecutionException | InterruptedException e) {
            Log.e("error", "getDialogs");
            throw new RuntimeException();
        }
    }
}
