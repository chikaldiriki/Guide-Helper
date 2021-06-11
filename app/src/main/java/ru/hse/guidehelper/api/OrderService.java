package ru.hse.guidehelper.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.hse.guidehelper.model.Order;

public interface OrderService {

    @POST("orders")
    Call<Void> addOrder(@Body Order order);

    @DELETE("orders/delete") // TODO before usage - fixed on server
    Call<Void> deleteOrder(Order order);

    @GET("orders/user_mail={userId}")
    Call<List<Order>> getOrdersByUser(@Path("userId") String userId);
}
