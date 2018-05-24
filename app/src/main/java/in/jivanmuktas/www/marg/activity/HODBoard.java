package in.jivanmuktas.www.marg.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import in.jivanmuktas.www.marg.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HODBoard extends BaseActivity {
    String title;
    EditText hodMsgField;
    OkHttpClient mClient;
    JSONArray jsonArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hodboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.top_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title = getIntent().getExtras().getString("TITLE");
        setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mClient = new OkHttpClient();
        hodMsgField = (EditText) findViewById(R.id.hodMsgField);
    }

    public void SendMsgFromHOD(View view) {
        if(hodMsgField.getText().toString().trim().length()>0) {

            sendMessage();
            Toast.makeText(HODBoard.this, "Message Successfully send", Toast.LENGTH_LONG).show();
            hodMsgField.setText("");
        }else{
            Toast.makeText(HODBoard.this, "Please write message before you send", Toast.LENGTH_LONG).show();
        }
    }

    public void sendMessage() {
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    JSONObject root = new JSONObject();
                    /*JSONObject notification = new JSONObject();
                    notification.put("body", body);
                    notification.put("rgTitle", rgTitle);
                    //notification.put("icon", icon);
*/
                    JSONObject data = new JSONObject();
                    data.put("TYPE", "HOD");
                    data.put("MESSAGE", hodMsgField.getText().toString());
                    data.put("MOBILE", "9876543210");
                    data.put("EMAIL", "admin@gmail.com");
                    JSONObject notification = new JSONObject();
                    notification.put("rgTitle", "Jivanmukta Volunteering");
                    notification.put("text", "Good Afternoon");
                    notification.put("click_action", "OPEN_ACTIVITY_NOTIFICATION");

                    root.put("to", "/topics/JIVANMUKTA");
                    root.put("data", data);
                    root.put("notification", notification);
                    String result = postToFCM(root.toString());

                    return result;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                try {
                    /*Log.i("!!!!Result",result);

                    JSONObject resultJson = new JSONObject(result);
                    int success, failure;
                    success = resultJson.getInt("success");
                    failure = resultJson.getInt("failure");*/
                    Toast.makeText(HODBoard.this, "Message Success: " + /*success +*/ "Message Failed: " /*+ failure*/, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(HODBoard.this, "Message Failed, Unknown error occurred.", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

    String postToFCM(String bodyString) throws IOException {

        String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
        final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, bodyString);
        Request request = new Request.Builder()
                .url(HttpUrl.parse(FCM_MESSAGE_URL))
                .post(body)
                .addHeader("Authorization", "key=" + "AIzaSyCSVaC3frJFj8dQKLjUaJWegRTJC57vTi4")
                .build();
        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }


}
