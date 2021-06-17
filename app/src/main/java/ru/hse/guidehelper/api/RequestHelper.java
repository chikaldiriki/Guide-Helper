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
import ru.hse.guidehelper.model.FavoriteTour;
import ru.hse.guidehelper.model.Order;
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
            // throw new RuntimeException(e);
        }
        return null;
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
            // throw new RuntimeException(e);
        }
        return null;
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
            // throw new RuntimeException(e);
        }
        return null;
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
            // throw new RuntimeException(e);
        }
        return null;
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
            // throw new RuntimeException(e);
        }
        return null;
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
            // throw new RuntimeException(e);
        }
        return null;
    }

    public static void deleteFavoriteTour(String userMail, Long tourId) {
        Api.getInstance()
                .getFavoriteTourService()
                .deleteFavoriteTour(userMail, tourId)
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

    public static void addOrder(Order order) {
        Api.getInstance()
                .getOrderService()
                .addOrder(order)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                        if (!response.isSuccessful()) {
                            Log.e(String.valueOf(response.code()), "addOrder");
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                        Log.e("error", "addOrder");
                    }
                });

    }

    public static void deleteOrder(String customerMail, Long tourId, String time) {
        Api.getInstance()
                .getOrderService()
                .deleteOrder(customerMail, tourId, time)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                        if (!response.isSuccessful()) {
                            Log.e(String.valueOf(response.code()), "deleteOrder");
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                        Log.e("error", "deleteOrder");
                    }
                });

    }

    public static List<Tour> getToursWithCostLimit(Long costLimit) {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        try {
            return singleThreadExecutor.submit(() -> Api.getInstance()
                    .getTourService()
                    .getToursWithCostLimit(costLimit).execute().body())
                    .get();
        } catch (ExecutionException | InterruptedException e) {
            Log.e("error", "getToursWithCostLimit");
            // throw new RuntimeException(e);
        }
        return null;
    }

    public static List<String> getKeywords(String firstUser, String secondUser) {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        try {
            return singleThreadExecutor.submit(() -> Api.getInstance()
                    .getChatService()
                    .getKeywords(firstUser, secondUser).execute().body()).get();
        } catch (ExecutionException | InterruptedException e) {
            Log.e("error", "getKeywords");
            //throw new RuntimeException(e);
        }
        return null;
    }

    public static List<Tour> getToursByCitySortedByOptionalParameter(String city) {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        try {
            return singleThreadExecutor.submit(() -> Api.getInstance()
                    .getTourService()
                    .getToursByCitySortedByOptionalParameter(city).execute().body())
                    .get();
        } catch (ExecutionException | InterruptedException e) {
            Log.e("error", "getToursByCitySortedByOptionalParameter");
            // throw new RuntimeException(e);
        }
        return null;
    }

    public static List<Order> getOrdersByUser(String userId) {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        try {
            return singleThreadExecutor.submit(() -> Api.getInstance()
                    .getOrderService()
                    .getOrdersByUser(userId).execute().body())
                    .get();
        } catch (ExecutionException | InterruptedException e) {
            Log.e("error", "getOrdersByUser");
            // throw new RuntimeException(e);
        }
        return null;
    }

    public static List<String> getPopularKeywordsFromDB(String userMail) {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        try {
            return singleThreadExecutor.submit(() -> Api.getInstance()
                    .getChatService()
                    .getPopularKeywordsFromDB(userMail).execute().body()).get();
        } catch (ExecutionException | InterruptedException e) {
            Log.e("error", "getPopularKeywordsFromDB");
            //throw new RuntimeException(e);
        }
        return null;
    }

    public static List<String> getNewPopularKeywords(String userMail) {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        try {
            return singleThreadExecutor.submit(() -> Api.getInstance()
                    .getChatService()
                    .getNewPopularKeywords(userMail).execute().body()).get();
        } catch (ExecutionException | InterruptedException e) {
            Log.e("error", "getNewPopularKeywords");
            //throw new RuntimeException(e);
        }
        return null;
    }

    public static List<ChatDTO> getChatsByKeyword(String word) {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        try {
            return singleThreadExecutor.submit(() -> Api.getInstance()
                    .getChatService()
                    .getChatsByKeyword(word).execute().body()).get();
        } catch (ExecutionException | InterruptedException e) {
            Log.e("error", "getChatsByKeyword");
            //throw new RuntimeException(e);
        }
        return null;
    }

    public static String getToken(String userMail) {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        try {
            return singleThreadExecutor.submit(() -> Api.getInstance()
                    .getUserService()
                    .getToken(userMail).execute().body()).get();
        } catch (ExecutionException | InterruptedException e) {
            Log.e("error", "getToken");
            //throw new RuntimeException(e);
        }
        return null;
    }

    public static void updateToken(String userMail, String token) {
        Api.getInstance()
                .getUserService()
                .updateToken(userMail, token)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                        if (!response.isSuccessful()) {
                            Log.e(String.valueOf(response.code()), "updateToken");
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                        Log.e("error", "updateToken");
                    }
                });
    }

}
