package com.example.kartishe.test2;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * This {@code IntentService} does the app's actual work.
 * {@code SampleAlarmReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class SampleSchedulingService2 extends IntentService {
    public SampleSchedulingService2() {
        super("SchedulingService");
    }
    
    public static final String TAG = "Scheduling Demo";
    // An ID used to post the notification.
    public static final int NOTIFICATION_ID = 1;
    // The string the app searches for in the Google home page content. If the app finds 
    // the string, it indicates the presence of a doodle.  
    public static final String SEARCH_STRING = "doodle";
    public static final String SEARCH_STRING2 = "Error";

    // The Google home page URL from which the app fetches content.
    // You can find a list of other Google domains with possible doodles here:
    // http://en.wikipedia.org/wiki/List_of_Google_domains
    public static final String URL = "http://www.google.com";
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    @Override
    protected void onHandleIntent(Intent intent) {
        // BEGIN_INCLUDE(service_onhandle)
        // The URL from which to fetch content.
        //String urlString = URL;
    
        //String result ="";
        
        // Try to connect to the Google homepage and download content.
        /*
        try {
            result = loadFromNetwork(urlString);
        } catch (IOException e) {
            Log.i(TAG, getString(R.string.connection_error));
        }
        */

       /* String loginUrlString = "https://172.25.223.247:55443/api/v1/auth/token-services";
        String dataUrlString = "https://172.25.223.247:55443/api/v1/global/cli";
        String username = "lab";
        String pwd = "lab";*/

       /* JsonNode resultJson = commToNetwork(loginUrlString, dataUrlString, username, pwd);
        result = resultJson.get("results").asText();
       // result="Error sfs";
        // If the app finds the string "doodle" in the Google home page content, it
        // indicates the presence of a doodle. Post a "Doodle Alert" notification.
        if (result.indexOf(SEARCH_STRING2) != -1) {

            Log.d("amit","sending sms");
            //sendSMS("+19797393878", "Yo");
            Log.d("amit", "sent sms");
            // sendNotification(getString(R.string.doodle_found));
            //sendNotification(result);
            //Log.i(TAG, "Found Doodle!!");
            Log.i(TAG, "Error found!!");
        } else {
            //sendNotification(getString(R.string.no_doodle));
            //sendNotification("CSR operating normally");
            //Log.i(TAG, "No doodle found. :-( v2");
        }*/
        // Release the wake lock provided by the BroadcastReceiver.
       // SampleAlarmReceiver2.completeWakefulIntent(intent);
        // END_INCLUDE(service_onhandle)
    }


    
    // Post a notification indicating whether a doodle was found.
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
               this.getSystemService(Context.NOTIFICATION_SERVICE);
    
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity2.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_launcher)
        .setContentTitle(getString(R.string.doodle_alert))
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText(msg))
        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
 
//
// The methods below this line fetch content from the specified URL and return the
// content as a string.
//


    private String getB64Auth (String login, String pass) {
        String source=login+":"+pass;
        String ret="Basic "+ Base64.encodeToString(source.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);
        return ret;
    }

    private void disableCertVerification() {
        SSLContext ctx = null;
        try {
            ctx = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            ctx.init(null, new TrustManager[] {
                    new X509TrustManager() {
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                        public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[]{}; }
                    }
            }, null);
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
    }

    public InputStream commToUrlWithUsrPwd(String methodString, String urlString, String username, String pwd, String postData) {
        // Disable the verification of the certificate when using SSL
        disableCertVerification();

        java.net.URL url = null;
        HttpsURLConnection urlConnection = null;
        PrintWriter out = null;
        InputStream in_st = null;

        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod(methodString);
            urlConnection.setRequestProperty("Authorization", getB64Auth(username, pwd));
            urlConnection.setRequestProperty("Content-type", "application/json");
            urlConnection.setRequestProperty("Accept", "*/*");

            out = new PrintWriter(urlConnection.getOutputStream());

            // Send the Data
            out.print(postData);
            out.close();

            int statusCode = urlConnection.getResponseCode();

            if (statusCode >= 200 && statusCode < 400) {
                // Create an InputStream in order to extract the response object
                in_st = urlConnection.getInputStream();
            }
            else {
                in_st = urlConnection.getErrorStream();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return in_st;
    }

    private InputStream commToUrlWithToken(String methodString, String urlString, String tokenString, String postData) {
        // Disable the verification of the certificate when using SSL
        disableCertVerification();

        java.net.URL url = null;
        HttpsURLConnection urlConnection = null;
        PrintWriter out = null;
        InputStream in_st = null;

        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod(methodString);
            urlConnection.setRequestProperty("X-Auth-Token", tokenString);
            urlConnection.setRequestProperty("Content-type", "application/json");
            urlConnection.setRequestProperty("Accept", "*/*");

            out = new PrintWriter(urlConnection.getOutputStream());

            // Send the Data
            out.print(postData);
            out.close();

            int statusCode = urlConnection.getResponseCode();

            if (statusCode >= 200 && statusCode < 400) {
                // Create an InputStream in order to extract the response object
                in_st = urlConnection.getInputStream();
            }
            else {
                in_st = urlConnection.getErrorStream();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return in_st;
    }

    private JsonNode commToNetwork(String loginUrlString, String dataUrlString, String username, String pwd) {
        Boolean test = false;
        String token;

        ObjectMapper mapper = new ObjectMapper();

        if (!test) {
            // First, use the username to get the token
            InputStream is1 = commToUrlWithUsrPwd("POST",loginUrlString, username, pwd, "");

            JsonNode node = null;
            try {
                node = mapper.readTree(is1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Log.d("Log", node.get("token-id").asText());

            // Recover the token
            token = node.get("token-id").asText();
        } else {
            token = "7xgz0xGoKYaV1r5ROHI4SYeuqx6GzdgAQ3cmCxLMa7w=";
        }

        // Get the JSON query that will be used.
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("show","self-diag");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String query = jsonObject.toString();
        InputStream is2 = commToUrlWithToken("PUT", dataUrlString, token,query);

        JsonNode node2 = null;
        try {
            node2 = mapper.readTree(is2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return node2;
    }

    // OLD FUNCTIONS

    /** 
     * Reads an InputStream and converts it to a String.
     * @param stream InputStream containing HTML from www.google.com.
     * @return String version of InputStream.
     * @throws IOException
     */
    private String readIt(InputStream stream) throws IOException {
      
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        for(String line = reader.readLine(); line != null; line = reader.readLine())
            builder.append(line);
        reader.close();
        return builder.toString();
    }

    /** Given a URL string, initiate a fetch operation. */
    private String loadFromNetwork(String urlString) throws IOException {
        InputStream stream = null;
        String str ="";

        try {
            stream = downloadUrl(urlString);
            str = readIt(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return str;
    }

    /**
     * Given a string representation of a URL, sets up a connection and gets
     * an input stream.
     * @param urlString A string representation of a URL.
     * @return An InputStream retrieved from a successful HttpURLConnection.
     * @throws IOException
     */
    private InputStream downloadUrl(String urlString) throws IOException {

        java.net.URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Start the query
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
    }
}
