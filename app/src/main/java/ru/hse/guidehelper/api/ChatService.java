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

    @GET("chat/keywords/{firstUserId}/{secondUserId}")
    Call<List<String>> getKeywords(@Path("firstUserId") String firstUserId, @Path("secondUserId") String secondUserId);

    @GET("chat/keywords/DB/user_mail={userMail}")
    Call<List<String>> getPopularKeywordsFromDB(@Path("userMail") String userMail);

    @GET("chat/keywords/new/user_mail={userMail}")
    Call<List<String>> getNewPopularKeywords(@Path("userMail") String userMail);

    @GET("chat/keywords/word={word}")
    Call<List<ChatDTO>> getChatsByKeyword(@Path("word") String word);
}
