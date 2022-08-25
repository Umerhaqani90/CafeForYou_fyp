package com.yousif.cafeforyou.notification;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificationAPI {

    String SERVER_KEY = "";
    @Headers({"Authorization:key=AAAAbCpS80c:APA91bHKUrX2wctzt8nHcV2bKQHUWFyPMmOxA9JRNgJ3xVNvBGiyEMfjScoI1ESeZas108U1u3v4QQXiibK9sz9uQMYQlX9N3lBxIPNiqodgQK6AFOaZ9V_r4fYoAxs9JkhVfRGGg6ak"
            , "Content-Type:application/json"})
    @POST("fcm/send")
    Call<ResponseBody> sendNotification(@Body PushNotification root);
}
