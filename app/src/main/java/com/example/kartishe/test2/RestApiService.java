package com.example.kartishe.test2;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import com.example.kartishe.test2.SampleAlarmReceiver2;

/**
 * Created by squmruzz on 10/15/15.
 */
public class RestApiService extends IntentService{
    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = "DownloadService";

    public RestApiService()
    {
        super(RestApiService.class.getName());
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Service Started!");

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        String[] urls = intent.getStringArrayExtra("url");
        //String url = "https://172.25.223.182:55443/api/v1/global/host-name";

        Bundle bundle = new Bundle();

        String resultArray[] = new String[20];
        int i = 0;
        for (String url : urls) {
            if (!TextUtils.isEmpty(url)) {
            /* Update UI: Download Service is Running */
                receiver.send(STATUS_RUNNING, Bundle.EMPTY);


                String results = invokeRest(url);

                /* Sending result back to activity */
                if (null != results) {

                    resultArray[i] = results;
                }
                i++;
            }
        }

        try {
            bundle.putStringArray("result", resultArray);
            receiver.send(STATUS_FINISHED, bundle);
        }catch (Exception e) {

                /* Sending error message back to activity */
            bundle.putString(Intent.EXTRA_TEXT, e.toString());
            receiver.send(STATUS_ERROR, bundle);
        }

        Log.d(TAG, "Service Stopping!");
        this.stopSelf();
    }

    private String invokeRest(String requestUrl){
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        }
        catch (Exception e)
        {

        }

        // Install the all-trusting host verifier

        InputStream inputStream = null;
        HttpsURLConnection urlConnection = null;

        String[] results = null;
        String response = "";

        /* forming th java.net.URL object */
        try {
            URL url = new URL(requestUrl);
            urlConnection = (HttpsURLConnection) url.openConnection();

           // SampleAlarmReceiver2.commToUrlWithUsrPwd("POST","https://172.25.223.182:55443/api/v1/auth/token-services", "root", "lab", "");

            urlConnection.setRequestProperty("X-Auth-Token",getToken());

        /* optional request header */
            urlConnection.setRequestProperty("Content-Type", "application/json");

        /* optional request header */
            urlConnection.setRequestProperty("Accept", "application/json");

        /* for Get request */
            urlConnection.setRequestMethod("GET");
            int statusCode = urlConnection.getResponseCode();


            ObjectMapper mapper = new ObjectMapper();
            //mapper.rea

            //JsonNode node = mapper.readTree(urlConnection.getInputStream());


            //Log.d("Log", node.get("kind").asText());

        /* 200 represents HTTP OK */
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                response = convertInputStreamToString(inputStream);
                //results = parseResult(response);

            } else {
                throw new DownloadException("Failed to fetch data!!");
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return response;
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";

        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

            /* Close Stream */
        if (null != inputStream) {
            inputStream.close();
        }

        return result;
    }

    private String[] parseResult(String result) {

        String[] blogTitles = null;
        try {
            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray("posts");
            blogTitles = new String[posts.length()];

            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                String title = post.optString("title");
                blogTitles[i] = title;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return blogTitles;
    }

    public class DownloadException extends Exception {

        public DownloadException(String message) {
            super(message);
        }

        public DownloadException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public String getToken(){

        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        }
        catch (Exception e)
        {

        }

        // Install the all-trusting host verifier

        InputStream inputStream = null;
        HttpsURLConnection urlConnection = null;

        String[] results = null;
        String response = "";

        /* forming th java.net.URL object */
        try {
            URL url = new URL("https://172.25.223.182:55443/api/v1/auth/token-services");
            urlConnection = (HttpsURLConnection) url.openConnection();

            //urlConnection.setRequestProperty("X-Auth-Token", "y5WpKU3jsMTQovmaqE5J8FBJIfrjTvWDPPtl+Tv5Q2g=");

        /* optional request header */
            urlConnection.setRequestProperty("Content-Type", "application/json");

        /* optional request header */
            urlConnection.setRequestProperty("Accept", "application/json");
            String userpass = "root" + ":" + "lab";
            String basicAuth = "Basic " + new String(Base64.encode(userpass.getBytes(), 0));
            urlConnection.setRequestProperty("Authorization", basicAuth);

        /* for Get request */
            urlConnection.setRequestMethod("POST");
            int statusCode = urlConnection.getResponseCode();


            ObjectMapper mapper = new ObjectMapper();

            Log.d("amit",statusCode+"");
        /* 200 represents HTTP OK */
            if (statusCode == 200) {
                JsonNode node = mapper.readTree(urlConnection.getInputStream());
                response = node.get("token-id").asText();
                Log.d("amit",response);
                //results = parseResult(response);

            } else {
                throw new DownloadException("Failed to fetch data!!");
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return response;
    }
}
