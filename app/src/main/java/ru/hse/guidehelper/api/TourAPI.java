package ru.hse.guidehelper.api;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ru.hse.guidehelper.utils.ClientUtils;

public class TourAPI {
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    public static void addTour(OkHttpClient client, JSONObject jsonTour) {
        RequestBody body = RequestBody.create(jsonTour.toString(), JSON);
        Request request = new Request.Builder()
                .url(ClientUtils.url + ClientUtils.suffAddTour)
                .post(body)
                .build();

        System.out.println(jsonTour.toString());

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Response> task = executorService.submit(() -> client.newCall(request).execute());

        try {
            task.get();
        } catch (ExecutionException | InterruptedException ignored) {

        }
    }
}
