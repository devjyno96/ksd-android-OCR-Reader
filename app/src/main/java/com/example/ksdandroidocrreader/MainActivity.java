package com.example.ksdandroidocrreader;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ksdandroidocrreader.CallRest;
import com.example.ksdandroidocrreader.S3FileUpload;
import com.example.ksdandroidocrreader.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    private TextView Test_Viewer;
    private Uri selectedImageUri;
    private final int GET_GALLERY_IMAGE = 200;
    private ImageView imageview;
    // URL 설정.
    private String REST_URL = "http://ec2-13-209-123-84.ap-northeast-2.compute.amazonaws.com/api/ocr-requests/s3";
    private String S3_URL = "https://s3.ap-northeast-2.amazonaws.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Test_Viewer = (TextView) findViewById(R.id.Test_Viewer);
        imageview = (ImageView) findViewById(R.id.testImageView);

        imageview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
                // startActivityForResult(intent, REQ_CAMERA_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            selectedImageUri = data.getData();
            imageview.setImageURI(selectedImageUri);

        }
    }

    public class RestNetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

        public RestNetworkTask(String url, ContentValues values) {

            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {

            String result = null; // 요청 결과를 저장할 변수.
            CallRest requestHttpURLConnection = new CallRest();
            // result = requestHttpURLConnection.request(url, values); // 해당 URL로 부터 결과물을 얻어온다.
            result = requestHttpURLConnection.request(url, values); // 해당 URL로 부터 결과물을 얻어온다.

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
            Test_Viewer.setText(s);
        }
    }
    public class S3NetworkTask extends AsyncTask<Void, Void, String> {

        private File uploadfile;

        public S3NetworkTask(File uploadfile) {
            this.uploadfile = uploadfile;
        }

        @Override
        protected String doInBackground(Void... params) {

            S3FileUpload s3FileUpload = new S3FileUpload();
            try {
                s3FileUpload.uploadFile(uploadfile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String result = "s3upload complete";

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
            Test_Viewer.setText(s);
        }
    }

    public void bntClick(View view) throws IOException {


        Cursor cursor = getContentResolver().query(selectedImageUri, null, null, null, null );
        cursor.moveToNext();
        String path = cursor.getString( cursor.getColumnIndex( "_data" ) );

        S3NetworkTask s3NetworkTask = new S3NetworkTask(new File(path));
        s3NetworkTask.execute();

        String[] temp = path.split("/");
        String fileName = temp[temp. length-1];

        ContentValues params = new ContentValues();
        params.put("url", S3_URL + S3FileUpload.getBucket_name() + "/" + fileName);

        RestNetworkTask restNetworkTask = new RestNetworkTask(REST_URL, params);
        restNetworkTask.execute();
    }
}