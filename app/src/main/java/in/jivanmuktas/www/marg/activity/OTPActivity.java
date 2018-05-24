package in.jivanmuktas.www.marg.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import in.jivanmuktas.www.marg.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.jivanmuktas.www.marg.constant.Constant;
import in.jivanmuktas.www.marg.network.HttpClient;

public class OTPActivity extends BaseActivity {
    private static final String TAG = "OTPActivity";
    public enum ChangePasswordView{
        OTP_VIEW, PASSWORD_CHANGE_VIEW
    }

    private PinEntryEditText etOTP;
    private Button btnSubmit;
    private String password,EmailId_PhoneNo,phone,email;
    // public static OTPActivity instance;
    private Toolbar toolbar;
    private Button  btnResend;
    private ProgressBar progressBar;
    private TextView tvTime;
    private int count  = 0;
    private int time = 60;
    private TextView tvOTPtxt;
    private LinearLayout llOtp,llChnagePassword;

    JSONObject jsonResponse;
    String txt;

    private EditText etNewPassword,etConfirmPassword;
    private Button btnSubmitChangePassword;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if(count>time){
                        btnResend.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        tvTime.setVisibility(View.GONE);
                        count = 0;
                    }else{
                        progressBar.setVisibility(View.VISIBLE);
                        tvTime.setVisibility(View.VISIBLE);
                        count++;
                        //progressBar.setProgress(count*(100/time));

                        progressBar.setProgress(count);
                        if((time-count)>=0)
                            tvTime.setText("Time: 0."+String.format("%02d", (time-count)));
                        handler.sendEmptyMessageDelayed(1,1000);
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        Log.i("!!!Activity", TAG);
        registerReceiver(myReceiver,new IntentFilter("com.jivanmukta.otp.message.receive"));
        toolbar = (Toolbar) findViewById(R.id.toolbarTop);
        btnResend = (Button) findViewById(R.id.btnResend);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        tvOTPtxt = (TextView) findViewById(R.id.tvOTPtxt);
        llOtp = (LinearLayout) findViewById(R.id.llOtp);
        llChnagePassword = (LinearLayout) findViewById(R.id.llChnagePassword);
        etNewPassword = (EditText)findViewById(R.id.etNewPassword);
        etConfirmPassword = (EditText)findViewById(R.id.etConfirmPassword);
        btnSubmitChangePassword = (Button)findViewById(R.id.btnSubmitChangePassword);
        llChnagePassword.setOnClickListener(this);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle("Enter One Time Password(OTP)");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etOTP = (PinEntryEditText)findViewById(R.id.etOTP);
        btnSubmit = (Button)findViewById(R.id.btnSubmit);
        tvTime = (TextView) findViewById(R.id.tvTime);
        btnSubmit.setOnClickListener(this);
        btnSubmitChangePassword.setOnClickListener(this);
        btnResend.setOnClickListener(this);
        // instance = this;

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            password = bundle.getString("password");
            EmailId_PhoneNo = bundle.getString("EmailId_PhoneNo");
            phone = bundle.getString("phone");
            email = bundle.getString("email");
        }

        if(mailValidationCk(EmailId_PhoneNo)){
            txt  = "One Time Password send to your Email Id "+EmailId_PhoneNo+" . Please enter the same here to change password.";
        }else {
            txt  = "One Time Password send to your mobile number "+phone+" . Please enter the same here to change password.";
        }

        tvOTPtxt.setText(txt);
        progressBar.setVisibility(View.VISIBLE);
        tvTime.setVisibility(View.VISIBLE);
        btnResend.setVisibility(View.GONE);
        handler.sendEmptyMessageDelayed(1,1000);
//************** Automatic otp verified after enter text
        etOTP.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
            @Override
            public void onPinEntered(CharSequence str) {
                if(etOTP.getText().toString().trim().equalsIgnoreCase(password)){
                    visibleLayout(ChangePasswordView.PASSWORD_CHANGE_VIEW);

                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        visibleLayout(ChangePasswordView.OTP_VIEW);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSubmit:
                if(etOTP.getText().toString().trim().length()==0){
                    etOTP.setError("Please enter OTP");
                    return;
                }

                if(etOTP.getText().toString().trim().equalsIgnoreCase(password)){
                    visibleLayout(ChangePasswordView.PASSWORD_CHANGE_VIEW);
                    //  startActivity(new Intent(getApplicationContext(), ResetChangePasswordActivity.class).putExtra("EmailId_PhoneNo",EmailId_PhoneNo));

                }else{
                    Toast.makeText(getApplicationContext(),"OTP does not match.",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btnResend:
                new ResetAsynctask().execute();
                break;
            case R.id.btnSubmitChangePassword:
                if(isvalid()){
                    new ResetNewPasswordAsynctask().execute();
                }
                break;
        }
    }

    public class ResetAsynctask extends AsyncTask<String, String, Boolean> {

        // String password  = "";

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

                if(mailValidationCk(EmailId_PhoneNo)){
                    reqObj.put("EMAIL_ID", EmailId_PhoneNo);
                }else {
                    reqObj.put("PHONE_NO", EmailId_PhoneNo);
                }

                reqArr.put(reqObj);
                String response = HttpClient.SendHttpPost(Constant.PASSWORD_RESET, reqObj.toString());
                Log.d("!!Response", response.toString());
                jsonResponse = new JSONObject(response);
                if(jsonResponse.getString("status").equals("true")){
                    JSONArray array = jsonResponse.getJSONArray("response");
                    JSONObject object = array.getJSONObject(0);
                    password = object.getString("OTP");
                    return true;
                }else{
                    return  false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);
            dismissProgressDialog();
            if(aVoid){
                tvTime.setText("Time: 0.00");
                progressBar.setProgress(0);
                progressBar.setVisibility(View.VISIBLE);
                tvTime.setVisibility(View.VISIBLE);
                btnResend.setVisibility(View.GONE);
                handler.sendEmptyMessageDelayed(1, 1000);

                //startActivity(new Intent(getApplicationContext(), OTPActivity.class).putExtra("password", password).putExtra("EmailId_PhoneNo",etEmailOrPh.getText().toString()));

            }else{
                try {
                    String msg = jsonResponse.getString("response");
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                   e.printStackTrace();
                }
            }
        }
    }

    private final BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if(intent.getAction().equalsIgnoreCase("com.jivanmukta.otp.message.receive"))
            {
                String otp = intent.getStringExtra("otp");
                if (otp.equalsIgnoreCase(password)) {
                    Toast.makeText(getApplicationContext(),"OTP verified autometically",Toast.LENGTH_LONG).show();
                    // startActivity(new Intent(getApplicationContext(), ResetChangePasswordActivity.class).putExtra("EmailId_PhoneNo",EmailId_PhoneNo));
                    visibleLayout(ChangePasswordView.PASSWORD_CHANGE_VIEW);
                }
            }
        }
    };

    public void visibleLayout(ChangePasswordView changePasswordView){
        switch (changePasswordView){
            case OTP_VIEW:
                llOtp.setVisibility(View.VISIBLE);
                llChnagePassword.setVisibility(View.GONE);
                break;
            case PASSWORD_CHANGE_VIEW:
                llOtp.setVisibility(View.GONE);
                llChnagePassword.setVisibility(View.VISIBLE);
                setTitle("Enter new Password");
                break;
        }

    }
    public boolean isvalid(){
        boolean flag = true;
        if(etNewPassword.getText().toString().trim().length() ==0){
            etNewPassword.setError("Please enter new password");
            flag = false;
        }else if(!etNewPassword.getText().toString().trim().equalsIgnoreCase(etConfirmPassword.getText().toString().trim())){
            etConfirmPassword.setError("Confirm does not match");
            flag = false;
        }
        return flag;
    }

    public class ResetNewPasswordAsynctask extends AsyncTask<String, String, Boolean> {

        String password  = "";

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
                reqObj.put("EMAIL_ID", email);
                reqObj.put("PASSWORD", etNewPassword.getText().toString());
                reqArr.put(reqObj);
                String response = HttpClient.SendHttpPost(Constant.PASSWORD_UPDATE, reqObj.toString());
                Log.d("!!Response", response.toString());
                jsonResponse = new JSONObject(response);
                return  jsonResponse.getBoolean("status");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override

        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);
            dismissProgressDialog();
            if(aVoid){
                Toast.makeText(getApplicationContext(), "Password change successfully.", Toast.LENGTH_LONG).show();
                ResetPassword.instance.finish();
                OTPActivity.this.finish();

            }else{
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }


        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);*/
        return false;
    }
}
