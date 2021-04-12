package com.example.ksdandroidocrreader;

import android.content.ContentValues;
import android.os.AsyncTask;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;


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

        return getResultField(result);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
    }

    private String getResultField(String json){
        JsonParser parser = new JsonParser();
        String result = "";
        return json;
        // 판독 성공
//        JsonArray elements = parser.parse(json).
//                getAsJsonObject().get("images").
//                getAsJsonArray().get(0).
//                getAsJsonObject().get("fields").
//                getAsJsonArray();
//        //판독 실패 시 원문 출력
//        if(elements.size() == 0){
//            return json;
//        }
//
//        for (int i=0;i<elements.size();i++) {
//            result += elements.get(i) + "\n" ;
//        }
//        System.out.println(elements.size());
//        return result;
    }
}


class CallRest {

    public String request(String apiURL, ContentValues _params) {
        StringBuffer sbParams = new StringBuffer();
        if (_params == null)
            sbParams.append("");
            // 보낼 데이터가 있으면 파라미터를 채운다.
        else {
            // 파라미터가 2개 이상이면 파라미터 연결에 &가 필요하므로 스위칭할 변수 생성.
            boolean isAnd = false;
            // 파라미터 키와 값.
            String key;
            String value;
            for (Map.Entry<String, Object> parameter : _params.valueSet()) {
                key = parameter.getKey();
                value = parameter.getValue().toString();
                // 파라미터가 두개 이상일때, 파라미터 사이에 &를 붙인다.
                if (isAnd)
                    sbParams.append("&");

                sbParams.append(key).append("=").append(value);

                // 파라미터가 2개 이상이면 isAnd를 true로 바꾸고 다음 루프부터 &를 붙인다.
                if (!isAnd)
                    if (_params.size() >= 2)
                        isAnd = true;
            }
        }
        try {
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("dataType", "json");
            con.setRequestProperty("Content-Type", "application/json; charset=utf-8");


            // [2-2]. parameter 전달 및 데이터 읽어오기.
            String strParams = sbParams.toString(); //sbParams에 정리한 파라미터들을 스트링으로 저장. 예)id=id1&pw=123;
            //출처: https://jaehoney.tistory.com/14 [A work-loving developer]
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(strParams.getBytes("UTF-8"));
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            return response.toString();

        } catch (Exception e) {
            System.out.println(e);
        }

        return "False";
    }
}