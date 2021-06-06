package ru.hse.guidehelper.api;

import retrofit2.http.DELETE;
import retrofit2.http.POST;
import ru.hse.guidehelper.model.Order;

public interface OrderService {

    @POST("orders")
    public void addOrder(Order order);

    @DELETE("orders/delete") // TODO before usage - fixed on server
    public void deleteOrder(Order order);
}
