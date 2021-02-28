package com.example.ksdandroidocrreader;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MainActivity extends AppCompatActivity {

    private boolean image_selected = false;


    private TextView Test_Viewer;
    private TextView image_state;
    private String selectedImageUri;
    private final int GET_GALLERY_IMAGE = 200;
    private final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    // URL 설정.
    private String REST_URL = "http://ec2-13-209-123-84.ap-northeast-2.compute.amazonaws.com/api/ocr-requests/s3";
    private String S3_URL = "https://s3.ap-northeast-2.amazonaws.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Test_Viewer = (TextView) findViewById(R.id.Result_Viewer);
        Test_Viewer.setMovementMethod(new ScrollingMovementMethod());
        image_state = (TextView) findViewById(R.id.image_selected_statement);
    }


    public void restCallOnClick(View view) throws IOException, ExecutionException, InterruptedException {
        if(!(image_selected)){
            String Text = "이미지를 선택해 주십시오.";
            Toast.makeText(getApplicationContext(),Text, Toast.LENGTH_SHORT).show();
            return;
        }

        S3NetworkTask s3NetworkTask = new S3NetworkTask(new File(selectedImageUri));
        String resultS3 = s3NetworkTask.execute().get();
        Test_Viewer.setText(resultS3);

        String[] temp = selectedImageUri.split("/");
        String fileName = temp[temp.length - 1];

        ContentValues params = new ContentValues();
        params.put("url", S3_URL + S3FileUpload.getBucket_name() + "/" + fileName);

        RestNetworkTask restNetworkTask = new RestNetworkTask(REST_URL, params);
        String resultRest = restNetworkTask.execute().get();
        Test_Viewer.setText(resultRest);

        Toast.makeText(getApplicationContext(),"실행 완료", Toast.LENGTH_SHORT).show();
    }

    public void imageSelectOnClick(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, GET_GALLERY_IMAGE);
    }


    public void takePhotoOnClick(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                System.out.println(ex);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // select image
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
            cursor.moveToNext();
            String path = cursor.getString(cursor.getColumnIndex("_data"));
            selectedImageUri = path;
        }

        image_selected = true;
        image_state.setText(new File(String.valueOf(selectedImageUri)).getName());
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        selectedImageUri = image.getAbsolutePath();
        return image;
    }
    public void testChangeLayoutOnClick(View v) {
        setContentView(R.layout.activity_main);
        CategoryFragment categoryFragment = new CategoryFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, categoryFragment).commit();
    }
}