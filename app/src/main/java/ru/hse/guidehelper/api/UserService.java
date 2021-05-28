package ru.hse.guidehelper.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import ru.hse.guidehelper.model.User;

public interface UserService {

    @GET("users/{userId}")
    Call<User> getUser(@Path("userId") String userId);

    @POST("users")
    Call<Void> addUser(@Body User user);

    @PUT("users/{userId}")
    Call<Void> updateUser(@Body User user, @Path("userId") String userId);
}
