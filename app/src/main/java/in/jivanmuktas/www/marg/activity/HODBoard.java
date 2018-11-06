package in.jivanmuktas.www.marg.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
                    //notification.put("icon", icon);*/

                    JSONObject data = new JSONObject();
                    data.put("TYPE", "HOD");
                    data.put("DATE", GetCurrentDateTime());
                    data.put("MESSAGE", hodMsgField.getText().toString());
                    data.put("MOBILE", app.getContact());
                    data.put("EMAIL", app.getEmail());
                    JSONObject notification = new JSONObject();
                    notification.put("rgTitle", "Jivanmukta Volunteering");
                    notification.put("text", hodMsgField.getText().toString());
                    notification.put("click_action", "OPEN_ACTIVITY_NOTIFICATION");

                    root.put("to", "/topics/JIVANMUKTA");
                    root.put("data", data);
                    root.put("notification", notification);
                    String result = postToFCM(root.toString());
//*******************************************
                    StoreHODNotification(data.toString());
                    return result;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                Toast.makeText(HODBoard.this, "Message Successfully send", Toast.LENGTH_LONG).show();
                hodMsgField.setText("");
                try {
                    Log.i("!!!!Result",result);

                   /* JSONObject resultJson = new JSONObject(result);
                    int success, failure;
                    success = resultJson.getInt("success");
                    failure = resultJson.getInt("failure");*/
                   // Toast.makeText(HODBoard.this, "Message Success: "+result /*+ success + "Message Failed: " + failure*/, Toast.LENGTH_LONG).show();
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
                .addHeader("Authorization", "key=" + "AIzaSyArmCdvx6WLNNjvq5yPSfojUa7UhyBg8tg")
                .build();
        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }
//********************************//
    public void StoreHODNotification(String notification){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref1 = database.getReference("NOTIFICATION").child("HOD");
        ref1.push().setValue(notification);
    }

}
