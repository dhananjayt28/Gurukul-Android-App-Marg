package in.jivanmuktas.www.marg.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import in.jivanmuktas.www.marg.R;
import com.google.firebase.messaging.FirebaseMessaging;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONObject;

import io.fabric.sdk.android.Fabric;
import in.jivanmuktas.www.marg.constant.Constant;
import in.jivanmuktas.www.marg.network.HttpClient;


public class SplashActivity extends BaseActivity {

    private Handler handler;

    private ImageView imgLogo;
    String currentVersion="null", latestVersion="null";

    AVLoadingIndicatorView loader;
    JSONObject jsonResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ///////////
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Full screen hide status bar
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);

        FirebaseMessaging.getInstance().subscribeToTopic("JIVANMUKTA");

        imgLogo = (ImageView) findViewById(R.id.imgLogo);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
        imgLogo.startAnimation(animation);

        loader = (AVLoadingIndicatorView) findViewById(R.id.loader);

//*****************************************************************************************************
        //Handler can be changed to the following java 8
            handler = new Handler();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new GetLatestVersion().execute();
                        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_right_out);
                }
            },4000);

//*****************************************************************************************************
    }


    ///Get Current Apk version
    private void getCurrentVersion(){
        PackageManager pm = this.getPackageManager();
        PackageInfo pInfo = null;

        try {
            pInfo =  pm.getPackageInfo(this.getPackageName(),0);

        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        currentVersion = pInfo.versionName;
    }

    ///////////Get play store Latest Version
    private class GetLatestVersion extends AsyncTask<String, String, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Boolean doInBackground(String... params) {
            try {
              /*  /////////////Get Current Version
                getCurrentVersion();

                //It retrieves the latest version by scraping the content of current version from play store at runtime
                Document doc = Jsoup.connect("https://play.google.com/store/apps/details?id=tata.motors.malnutrition").get();
                latestVersion = doc.getElementsByAttributeValue("itemprop","softwareVersion").first().text();
                Log.i("!!!!!!!Latest Version",latestVersion);
*/
            }catch (Exception e){


            }finally{

            }

            return true;
        }
        @Override
        protected void onPostExecute(Boolean b) {
            super.onPostExecute(b);
            currentVersion = "1.6";   // Please Remove it when you implement
            latestVersion = "1.6";  // Please Remove it when you implement

            if (latestVersion.equalsIgnoreCase("null")){
                Toast.makeText(SplashActivity.this,"Your Device Doesn’t Have Internet Connectivity, Please Connect To Internet And Try Accessing The App.",Toast.LENGTH_LONG).show();
                finish();

            }else if(!currentVersion.equals(latestVersion)){
                AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                builder.setTitle("Update Available");
                builder.setIcon(R.drawable.update);
                builder.setMessage("Current Version : "+currentVersion+"\nLatest Version : "+latestVersion);
                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=tata.motors.malnutrition"));
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }else {

                new LoginAsynctask().execute(); // For checking password validation

            }
        }
    }

    //********************%%%%%%%%%%%%%%%%%%%%******************##############################@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    public class LoginAsynctask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showProgressDailog();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                JSONArray reqArr = new JSONArray();
                JSONObject reqObj = new JSONObject();
                reqObj.put("USER_ID", app.getEmail());
                reqObj.put("PASSWORD", app.getPassword());

                reqArr.put(reqObj);
                System.out.println("!!!reqArr" + reqArr);

                String response = HttpClient.SendHttpPost(Constant.LOGIN, reqObj.toString());

                Log.d("!!Response", response.toString());
                jsonResponse = new JSONObject(response);

                if (jsonResponse.getBoolean("status"))
                    return true;
                else
                    return false;
            } catch (Exception e) {
                System.out.println("!! Reach here error " + e.getMessage());
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean response) {
            super.onPostExecute(response);
           //dismissProgressDialog();
            if(!response) {
                app.setSession(false);
            }
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
        }

    }
}
