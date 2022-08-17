package com.example.notificationapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.notificationapp.RetroFitBuilder.IPostApi;
import com.example.notificationapp.RetroFitBuilder.Response;
import com.example.notificationapp.RetroFitBuilder.RetroFitBuilderClass;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class MyFirebaseMessagingService extends FirebaseMessagingService{
    private static final String TAG = "MyFirebaseMsgService";
//    Context context;
//    public MyFirebaseMessagingService(Context context){
//        this.context=context;
//    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
  if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData().get("userId"));


            if (true) {
                scheduleJob();
            } else {
                handleNow();
            }

        }
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
        String[]uId=remoteMessage.getData().get("userId").split("\\s");
        List<String> userId= Arrays.asList(uId);

        String imageUrl = remoteMessage.getData().get("url");
        SharedPreferences sh = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String s1 = sh.getString("userId", "");
        System.out.println("userIdShared"+s1);
        if(userId.contains("234")) {
            try {
                sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle(), imageUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        Intent intent=new Intent(MyFirebaseMessagingService.this,test.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
    }
    private void scheduleJob() {
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
                .build();
        WorkManager.getInstance(this).beginWith(work).enqueue();
        // [END dispatch_job]
    }
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        Retrofit retrofit= RetroFitBuilderClass.getInstance();
        IPostApi iPostApi=retrofit.create(IPostApi.class);
        Response response=new Response();
        response.setToken(token);
        response.setUserId("234");
        response.setPlatformId("3");
        response.setSocialMediaId(1);
        Call<Response> responseCall=iPostApi.sendToken(response);
        responseCall.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(@NonNull Call<Response> call, @NonNull retrofit2.Response<Response> response) {
                System.out.println("Success");
            }

            @Override
            public void onFailure(@NonNull Call<Response> call, @NonNull Throwable t) {
                System.out.println("Failed");
            }
        });

    }
    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("newToken", "Refreshed token: " + token);
        sendRegistrationToServer(token);
        super.onNewToken(token);
    }
    private void sendNotification(String messageBody, String title, String url) throws IOException {
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this,"myChannel")
                        .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true);
             Bitmap bitmap = getBitmapfromUrl(url);
            notificationBuilder.setStyle(
                    new NotificationCompat.BigPictureStyle()
                            .bigPicture(bitmap)
                            .bigLargeIcon(null)
            ).setLargeIcon(bitmap);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("myChannel",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0, notificationBuilder.build());
    }
    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            Log.e("awesome", "Error in getting notification image: " + e.getLocalizedMessage());
            return null;
        }
    }

}
