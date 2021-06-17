package ru.hse.java.repetinder.activities;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import ru.hse.java.repetinder.notifications.MyResponse;
import ru.hse.java.repetinder.notifications.Sender;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA5BjstIs:APA91bHq_BS6qIATvKSit826o2phq8yWN_5uNULg0nE9NDxyAt-R11yZrUyGyh6pkZ6Kq7LELBhEEL7wng5WdXyePkXHw1Kur8KTR1M7Fx52_7V5doPGB9OhqODhGfPAj1RLD_-LXBPW"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
