package in.jivanmuktas.www.marg.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import in.jivanmuktas.www.marg.R;

import org.json.JSONArray;
import org.json.JSONObject;

import in.jivanmuktas.www.marg.constant.Constant;
import in.jivanmuktas.www.marg.network.HttpClient;

public class ResetPassword extends BaseActivity {
    private static final String TAG = "ResetPassword";
    private EditText etEmailOrPh;
    private Button btReset;
    public static ResetPassword  instance;
    JSONObject jsonResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Log.i("!!!Activity", TAG);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTop);
        setSupportActionBar(toolbar);
        setTitle("Reset Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        etEmailOrPh = (EditText)findViewById(R.id.etEmailOrPh);
        editTextFocus(etEmailOrPh);/// SetFocus
        btReset = (Button)findViewById(R.id.btReset);
        instance = this;
        btReset.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btReset:
                if (etEmailOrPh.length() !=0) {
                    new ResetAsynctask().execute();
                }else {
                    editTextFocaus(etEmailOrPh);
                    etEmailOrPh.setError("Enter Valid Email Id");
                }
                break;
        }
    }

    public class ResetAsynctask extends AsyncTask<String, String, Boolean> {

        String password  = "123456";
        String phone  = "98*****348";
        String email  = "abc@gmail.com";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDailog();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                JSONArray reqArr = new JSONArray();
                JSONObject reqObj = new JSONObject();

                if(mailValidationCk(etEmailOrPh.getText().toString().trim())){
                    reqObj.put("EMAIL_ID", etEmailOrPh.getText().toString().trim());
                }else{
                    reqObj.put("PHONE_NO", etEmailOrPh.getText().toString().trim());
                }

                reqArr.put(reqObj);
                System.out.println("!!!reqArr  " + reqArr);
                String response =  HttpClient.SendHttpPost(Constant.PASSWORD_RESET, reqObj.toString());
                Log.d("!!Response", response.toString());
                jsonResponse = new JSONObject(response);

                if (jsonResponse.getBoolean("status"))
                    return true;
                else
                    return false;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);
            dismissProgressDialog();

                try {
                    if(aVoid){
                    JSONArray array = jsonResponse.getJSONArray("response");
                    JSONObject object = array.getJSONObject(0);
                    password = object.getString("OTP");
                    phone = object.getString("MOBILE_NO_SHOW");
                    email = object.getString("EMAIL");

                    startActivity(new Intent(getApplicationContext(), OTPActivity.class)
                            .putExtra("password", password)
                            .putExtra("EmailId_PhoneNo",etEmailOrPh.getText().toString())
                            .putExtra("phone",phone)
                            .putExtra("email",email));
                }else{
                        String msg = jsonResponse.getString("response");
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                }

                }catch (Exception e){}

        }
    }
    public void editTextFocaus(EditText editText){
        editText.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);*/
        return false;
    }
}
