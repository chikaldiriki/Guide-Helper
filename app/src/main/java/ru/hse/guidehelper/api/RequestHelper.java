package ru.hse.guidehelper.api;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.hse.guidehelper.model.Tour;
import ru.hse.guidehelper.model.User;

public class RequestHelper {

    @SuppressWarnings("unused")
    public static List<Tour> getAllTours() {
        AtomicReference<List<Tour>> result = new AtomicReference<>();
        Api.getInstance()
                .getTourService()
                .getAllTours()
                .enqueue(new Callback<List<Tour>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<Tour>> call, @NotNull Response<List<Tour>> response) {
                        if (response.isSuccessful()) {
                            result.set(response.body());
                        } else {
                            Log.e(String.valueOf(response.code()), "getAllTours");
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<Tour>> call, @NotNull Throwable t) {
                        Log.e("error", "getAllTours");
                    }
                });

        return result.get();
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
        AtomicReference<User> result = new AtomicReference<>();

        Api.getInstance()
                .getUserService()
                .getUser(userId)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {
                        if (response.isSuccessful()) {
                            result.set(response.body());
                        } else {
                            Log.e(String.valueOf(response.code()), "getUser");
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<User> call, @NotNull Throwable t) {
                        Log.e("error", "getUser");
                    }
                });

        return result.get();
    }

    public static String getChatId(String firstUserId, String secondUserId) {
        AtomicReference<String> result = new AtomicReference<>();

        Api.getInstance()
                .getChatService()
                .getChatId(firstUserId, secondUserId)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                        if (response.isSuccessful()) {
                            result.set(response.body());
                        } else {
                            Log.e(String.valueOf(response.code()), "getChatId");
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                        Log.e("error", "getChatId");
                    }
                });

        return result.get();
    }
}
