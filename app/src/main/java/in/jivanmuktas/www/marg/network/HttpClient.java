package in.jivanmuktas.www.marg.network;

import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;

@SuppressWarnings("deprecation")
public class HttpClient {

    private static final String TAG = "HttpClient";
    private static DefaultHttpClient httpclient;

    public static synchronized String SendHttpPost(String URL, String req) {
        System.out.println("!!! URL " + URL + " req body " + req);

        try {
            if (httpclient == null)
                httpclient = new DefaultHttpClient();

            HttpPost httpPostRequest = new HttpPost(URL);

            StringEntity se;
            se = new StringEntity(req);

            httpPostRequest.setEntity(se);
            httpPostRequest.setHeader("Accept", "application/json");
            // httpPostRequest.setHeader("Content-type", "application/json");
            //httpPostRequest.setHeader("Accept-Encoding", "gzip");

            //long t = System.currentTimeMillis();
            HttpResponse response = httpclient.execute(httpPostRequest);

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream instream = entity.getContent();

                Header contentEncoding = response.getFirstHeader("Content-Encoding");
                if (contentEncoding != null
                        && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                    instream = new GZIPInputStream(instream);
                }

                Log.d("Result", instream.toString());
                String resultString = convertStreamToString(instream);

                instream.close();

                resultString = resultString.substring(0, resultString.length() - 1);


                /*JsonParser parser = new JsonParser();
                String retVal = parser.parse(resultString).getAsString();
                //Log.d("val", retVal);


                JSONArray mainjsonArray = new JSONArray(retVal);
                JSONObject responseObj = mainjsonArray.getJSONObject(0);
                System.out.println("!!Response  " + responseObj.toString());

                return responseObj;*/

                return resultString;
            }

        } catch (Exception e) {
            Log.e("Exception", "Exception");
            e.printStackTrace();
        }
        return null;
    }

    private static synchronized String convertStreamToString(InputStream is) {
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


    public static String makeRequest(String uri, String json) {

        Log.d("JSON", json);

        HttpResponse response= null;

        int TIMEOUT_MILLISEC = 10000;  // = 10 seconds
        String postMessage = json; //HERE_YOUR_POST_STRING.
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
        HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
        DefaultHttpClient client = new DefaultHttpClient(httpParams);

        HttpPost request = new HttpPost(uri);
        try {
            request.setEntity(new ByteArrayEntity(
                    postMessage.toString().getBytes("UTF8")));
            response = client.execute(request);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();

        /*HttpURLConnection urlConnection;
        String url;
        String data = json;
        String result = null;
        try {
            //Connect
            urlConnection = (HttpURLConnection) ((new URL(uri).openConnection()));
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.connect();

            //Write
            OutputStream outputStream = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(data);
            writer.close();
            outputStream.close();

            //Read
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

            String line = null;
            StringBuilder sb = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            bufferedReader.close();
            result = sb.toString();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;*/
    }
}