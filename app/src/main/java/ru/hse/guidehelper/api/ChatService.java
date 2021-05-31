package ru.hse.guidehelper.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.hse.guidehelper.dto.ChatDTO;

public interface ChatService {

    @GET("chat/{firstUserId}/{secondUserId}")
    Call<Long> getChatId(@Path("firstUserId") String firstUserId, @Path("secondUserId") String secondUserId);

    @GET("chat/dialogs/{userId}")
    Call<List<ChatDTO>> getDialogs(@Path("userId") String userId);
}
