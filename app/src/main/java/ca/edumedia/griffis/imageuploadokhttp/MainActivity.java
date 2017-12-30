package ca.edumedia.griffis.imageuploadokhttp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import ca.edumedia.griffis.imageuploadokhttp.models.BuildingPOJO;
import ca.edumedia.griffis.imageuploadokhttp.services.MyService;
import ca.edumedia.griffis.imageuploadokhttp.utils.NetworkHelper;

public class MainActivity extends AppCompatActivity {

    private final static int CAMERA_REQUEST_CODE = 100;
    private ImageView photoView;
    //private static final String JSON_URL = "https://doors-open-ottawa.mybluemix.net/buildings"; //OLD endpoint
    public static final String JSON_URL = "https://doors-open-ottawa.mybluemix.net/buildings/form"; //NEW endpoint

    private BroadcastReceiver mBR = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(MyService.MY_SERVICE_PAYLOAD)) {
                //make sure to handle things like adding a new Building which will return an object, NOT an array
                BuildingPOJO buildingObj= (BuildingPOJO) intent.getParcelableExtra(MyService.MY_SERVICE_PAYLOAD);

                //For the POST upload the length should be one. There will be one building sent back
                //we can show the id of the new building   buildingsArray[0].getBuildingId();

                Toast.makeText(MainActivity.this,
                        "Received new building from service. " + buildingObj.getBuildingId(),
                        Toast.LENGTH_SHORT).show();

                //Save the Buildings as a global member field in a List or ArrayList
                //or add the new building to the list...
                //mBuildingsList = new ArrayList<>(Arrays.asList(buildingsArray));


            } else if (intent.hasExtra(MyService.MY_SERVICE_EXCEPTION)) {
                String message = intent.getStringExtra(MyService.MY_SERVICE_EXCEPTION);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        photoView = (ImageView) findViewById(R.id.photoView);

        //register the BroadcastReceiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mBR, new IntentFilter(MyService.MY_SERVICE_MESSAGE));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBR);
    }

    public void takePicture(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent){
        Bundle extras;
        Bitmap bitmap;
        if(resultCode == RESULT_CANCELED){
            Toast.makeText(getApplicationContext(), "No picture taken", Toast.LENGTH_SHORT).show();
            return;
        }
        switch(requestCode){
            case CAMERA_REQUEST_CODE:
                extras = resultIntent.getExtras();
                bitmap = (Bitmap) extras.get("data");
                if(bitmap != null){
                    //there is a picture
                    photoView.setImageBitmap(bitmap);
                }
                break;
        }
    }

    public void uploadBuilding(View view){
        //we need the name, address, and image for the building
        //we will use the Service to make the request to the server as an HTTP POST
        // nameEN, addressEN, and image will be the three fields we send.
        if (NetworkHelper.hasNetworkAccess(this)) {
            String bName = ((EditText) findViewById(R.id.editName)).getText().toString();
            String bAddress = ((EditText) findViewById(R.id.editAddress)).getText().toString();


            //call on the Service to make the call
            Intent intent = new Intent(this, MyService.class);
            intent.putExtra(MyService.REQUEST_ENDPOINT, JSON_URL);
            intent.putExtra(MyService.REQUEST_METHOD, "POST");
            intent.putExtra(MyService.BUILDING_NAME, bName);
            intent.putExtra(MyService.BUILDING_ADDRESS, bAddress);

            Bitmap bitmap  = ((BitmapDrawable) photoView.getDrawable( ) ).getBitmap( );
            //then this bitmap needs to be turned into a ByteArray to be uploaded.
            ByteArrayOutputStream baos = new ByteArrayOutputStream( );
            bitmap.compress( Bitmap.CompressFormat.JPEG, 0, baos);
            //now we can pass baos.toByteArray() to the upload as part of the request body
            intent.putExtra(MyService.IMAGE_BYTEARRAY, baos.toByteArray());
            startService(intent);
        } else {
            Toast.makeText(this, "Network not available", Toast.LENGTH_SHORT).show();
            //Such sad. No Network
        }
    }
}
