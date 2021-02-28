package com.example.ksdandroidocrreader;

import android.os.AsyncTask;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Upload a file to an Amazon S3 bucket.
 *
 * This code expects that you have AWS credentials set up per:
 * http://docs.aws.amazon.com/java-sdk/latest/developer-guide/setup-credentials.html
 */

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
    }
}

class S3FileUpload {
    private static final String BUCKET_NAME = "ocr.image.ksd.hansung.ac.kr";
    private static final String ACCESS_KEY = "AKIAVEEOCLJLEJWUMC7L";
    private static final String SECRET_KEY = "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";
    private AmazonS3 amazonS3;

    public static String getBucket_name(){
        return BUCKET_NAME;
    }
    public S3FileUpload() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        amazonS3 = new AmazonS3Client(awsCredentials);
        amazonS3.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));
    }

    public void uploadFile(File file) throws FileNotFoundException {
        PutObjectResult result;
        if (amazonS3 != null) {
            try {
                PutObjectRequest putObjectRequest =
                        new PutObjectRequest(BUCKET_NAME, file.getName(), file);
                putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead); // file permission
                result = amazonS3.putObject(putObjectRequest); // upload file

            } catch (AmazonServiceException ase) {
                ase.printStackTrace();
            } finally {
                amazonS3 = null;
            }
        }
    }
}

//출처: https://dwfox.tistory.com/57 [DWFOX]