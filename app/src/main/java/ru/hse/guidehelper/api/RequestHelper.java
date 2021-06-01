package ru.hse.guidehelper.api;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import ru.hse.guidehelper.dto.ChatDTO;
import ru.hse.guidehelper.model.FavoriteTour;
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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
    }

    public static void addUser(User user) {
        Api.getInstance()
                .getUserService()
                .addUser(user)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                        if (!response.isSuccessful()) {
                            Log.e(String.valueOf(response.code()), "addUser");
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                        Log.e("error", "addUser");
                    }
                });
    }

    public static void updateUser(User user, String userId) {
        Api.getInstance()
                .getUserService()
                .updateUser(user, userId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                        if (!response.isSuccessful()) {
                            Log.e(String.valueOf(response.code()), "updateUser");
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                        Log.e("error", "updateUser");
                    }
                });
    }

    public static List<Tour> getFavoriteTours(String userMail) {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        try {
            return singleThreadExecutor.submit(() -> Api.getInstance()
                    .getTourService()
                    .getFavoriteTours(userMail).execute().body())
                    .get();
        } catch (ExecutionException | InterruptedException e) {
            Log.e("error", "getFavoriteTours");
            throw new RuntimeException(e);
        }
    }

    public static void addFavoriteTour(FavoriteTour newFavorite) {
        Api.getInstance()
                .getFavoriteTourService()
                .addFavoriteTour(newFavorite)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                        if (!response.isSuccessful()) {
                            Log.e(String.valueOf(response.code()), "addFavoriteTour");
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                        Log.e("error", "addFavoriteTour");
                    }
                });
    }

    public static Boolean isFavorite(String userMail, Long tourId) {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        try {
            return singleThreadExecutor.submit(() -> Api.getInstance()
                    .getFavoriteTourService()
                    .isFavorite(userMail, tourId).execute().body())
                    .get();
        } catch (ExecutionException | InterruptedException e) {
            Log.e("error", "isFavorite");
            throw new RuntimeException(e);
        }
    }

    public static void deleteFavoriteTour(FavoriteTour favoriteTour) {
        Api.getInstance()
                .getFavoriteTourService()
                .deleteFavoriteTour(favoriteTour)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                        if (!response.isSuccessful()) {
                            Log.e(String.valueOf(response.code()), "deleteFavoriteTour");
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                        Log.e("error", "deleteFavoriteTour");
                    }
                });
    }

}
