package ru.hse.guidehelper.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.hse.guidehelper.model.User;

public interface UserService {

    @GET("users/{userId}")
    Call<User> getUser(@Path("userId") String userId);
}
