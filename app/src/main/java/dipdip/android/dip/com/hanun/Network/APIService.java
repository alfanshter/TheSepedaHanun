package dipdip.android.dip.com.hanun.Network;

import dipdip.android.dip.com.hanun.FCMService.NotificationSender;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAs4QFEpQ:APA91bEVJz7FcQlirnIYJHsV9k7VNnZsGjHnBFRmFofsWtQl2ZRvqvnE-pkGcwotdHrHSi6YZaMecDj2Cmv-JR3NYJHsEkBaUFzJARkvjM9uyFKWzXkw6Ah5bbKKrc9swHK5RRYIyUua" // Your server key refer to video for finding your server key
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}

