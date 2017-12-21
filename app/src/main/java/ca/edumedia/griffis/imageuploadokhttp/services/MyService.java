package ca.edumedia.griffis.imageuploadokhttp.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import ca.edumedia.griffis.imageuploadokhttp.models.BuildingPOJO;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.google.gson.Gson;

import java.io.IOException;


public class MyService extends IntentService {

    public static final String TAG = "MyService";
    public static final String MY_SERVICE_MESSAGE = "myServiceMessage";
    public static final String MY_SERVICE_PAYLOAD = "myServicePayload";
    public static final String MY_SERVICE_EXCEPTION = "myServiceException";
    public static final String REQUEST_ENDPOINT = "endPoint";
    public static final String REQUEST_METHOD = "method";
    public static final String BUILDING_NAME = "bName";
    public static final String BUILDING_ADDRESS = "bAddress";
    public static final String IMAGE_BYTEARRAY = "imageByteArray";

    public static final MediaType JSON_MIME = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg");

    private final static OkHttpClient client = new OkHttpClient();

    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String endpoint = intent.getStringExtra(REQUEST_ENDPOINT);
        String method = intent.getStringExtra(REQUEST_METHOD);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("nameEN", intent.getStringExtra(BUILDING_NAME))
                .addFormDataPart("addressEN", intent.getStringExtra(BUILDING_ADDRESS))
                .addFormDataPart("image", "building.jpg",
                        RequestBody.create(MEDIA_TYPE_JPEG, intent.getByteArrayExtra(IMAGE_BYTEARRAY) ) )
                .build();

        String user = "YOUR USERNAME";
        String pass = "YOUR PASSWORD";

        String credentials = Credentials.basic(user, pass);

        Request request = new Request.Builder()
                .header("Authorization", credentials)
                .addHeader("Accept", "application/json; q=0.5")
                .url(endpoint)
                .post(requestBody)
                .build();
        //there are methods for get() post() put() and delete()

        try {
            Response response = client.newCall(request).execute(); //synchronous request

            //Do something with the response...
            if (response.isSuccessful()) {
                String output = response.body().string();
                Log.i(TAG, output);

                Gson gson = new Gson();
                BuildingPOJO dataItems = gson.fromJson(output, BuildingPOJO.class);

                Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
                messageIntent.putExtra(MY_SERVICE_PAYLOAD, dataItems);
                LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());
                manager.sendBroadcast(messageIntent);
                return;
            } else {
                throw new IOException("Exception: Response code " + response.code());
            }


        } catch (IOException e) {
            e.printStackTrace();
            Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
            messageIntent.putExtra(MY_SERVICE_EXCEPTION, e.getMessage());
            LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());
            manager.sendBroadcast(messageIntent);
            return;
        }


    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "MyService onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "MyService onDestroy");
    }

}