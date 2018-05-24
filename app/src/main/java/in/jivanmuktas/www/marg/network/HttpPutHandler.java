package in.jivanmuktas.www.marg.network;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@SuppressWarnings("deprecation")
public class HttpPutHandler {

    private static final String TAG = "HttpPutHandler";
    private static DefaultHttpClient httpclient;
    static InputStream inputStream = null;
    static String result = "";

    public static synchronized String SendHttpPut(String URL, String req) {
        System.out.println("!!! URL " + URL + " req body " + req);

        try {
            if (httpclient == null)
                httpclient = new DefaultHttpClient();

            HttpPut httpPutRequest = new HttpPut(URL);

            StringEntity se = new StringEntity(req);
            httpPutRequest.setEntity(se);
            httpPutRequest.setHeader("Accept", "application/json");

            HttpResponse response = httpclient.execute(httpPutRequest);

            inputStream = response.getEntity().getContent();

            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.e("Exception", "Exception");
            e.printStackTrace();
        }
        return result;
    }

    private static synchronized String convertInputStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {

                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}