package ru.hse.guidehelper.api;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
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
