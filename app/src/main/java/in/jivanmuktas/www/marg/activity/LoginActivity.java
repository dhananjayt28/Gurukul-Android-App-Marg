package in.jivanmuktas.www.marg.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;


import in.jivanmuktas.www.marg.R;

import org.json.JSONArray;
import org.json.JSONObject;

import in.jivanmuktas.www.marg.constant.Constant;
import in.jivanmuktas.www.marg.network.HttpClient;

public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    JSONObject jsonResponse;
    EditText userId, userPass;
    Button button_Sign_In, button_Sign_Up;
    TextView forgetPassWord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ///////////
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
         //Full screen hide status bar
        setContentView(R.layout.activity_login);
        Log.i("!!!Activity", TAG);
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTop);
        toolbar.setTitle("Sign In");*/
        if (app.isSession()) {
            CustomIntent(MainActivity.class);
        }
        userId = (EditText) findViewById(R.id.userId);
        userPass = (EditText) findViewById(R.id.userPass);

        button_Sign_In = (Button) findViewById(R.id.button_Sign_In);
        button_Sign_Up = (Button) findViewById(R.id.button_Sign_Up);
        forgetPassWord = (TextView) findViewById(R.id.forgetPassWord);
        //Done as per doc in 310ct in drive pointNo. 7
        forgetPassWord.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        button_Sign_In.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    if (isValid()) {
                        new LoginAsynctask().execute();
                        //This is done for tesing purpose to be removed later
                        CustomIntent(MainActivity.class);
                        LoginActivity.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                    }
                }
              // login();
            }
        });

        button_Sign_Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(LoginActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.passwordialog);
// set the custom dialog components - text, image and button
                final EditText appKey = (EditText) dialog.findViewById(R.id.appKey);
                editTextFocus(appKey);
                Button keySubmit = (Button) dialog.findViewById(R.id.keySubmit);
// if button is clicked, close the custom dialog
                keySubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (appKey.getText().toString().equals(app.AppKey)) {
                            CustomOnlyIntent(RegistrationActivity.class);///Custom Intent Without closing current Activity
                        } else if(appKey.getText().toString().equals("")) {
                            CustomToast("Please enter valid Key.");
                        }else{
                            CustomToast("You entered a Wrong Key.");
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        forgetPassWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,ForgotPassword.class);
                startActivity(intent);
            }
        });
    }

    public boolean isValid() {
        boolean flag = true;
        if (userId.getText().toString().trim().length() == 0) {
            userId.setError("Please enter Email Id/Phone No. Number");
            editTextFocus(userId);
            flag = false;
        } else if (userPass.getText().toString().trim().length() == 0) {
            userPass.setError("Please enter Password");
            editTextFocus(userPass);
            flag = false;
        }
        return flag;
    }

    /*public void login(){
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
    }*/
    public class LoginAsynctask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDailog();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
        //        JSONArray reqArr = new JSONArray();
                JSONObject reqObj = new JSONObject();
                reqObj.put("USER_ID", userId.getText().toString().trim());
                reqObj.put("PASSWORD", userPass.getText().toString());

        //        reqArr.put(reqObj);
                System.out.println("!!!reqObj  " + reqObj);  //OBJECT reqObj USED INSTEAD IF ARRAY  reqArr

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
            dismissProgressDialog();
            /*try {
                if (jsonResponse.getBoolean("status")) {
                    CustomToast("Login Successful");
                } else {
                    CustomToast("Login Failed");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
            if (!response) {
                //SnackbarRed(R.id.llout, "Please enter Registered Id & Password !");

                try {
                    JSONArray ck = jsonResponse.optJSONArray("response");
                    if(ck != null) {
                        JSONArray array = jsonResponse.getJSONArray("response");
                        JSONObject object = array.getJSONObject(0);
                        String isActive = object.getString("ISACTIVE");
                        String isAccLock = object.getString("IS_ACC_LOCK");
                        String loginAttempt = object.getString("LOGIN_ATTEMPT");

                            if (isAccLock.equals("1")) {
                                CustomToast("Your Account is Lock. Please reset your password.");
                            } else {
                                int rem = 5 - Integer.parseInt(loginAttempt);
                                CustomToast("Wrong Password. \nYou still have " + rem + " out of 5 attempts left. Please try again");
                            }
                    }else{
                        String msg = jsonResponse.getString("response");
                        CustomToast(msg);
                    }
                }catch (Exception e){}


            } else {
                try {
                    JSONArray array = jsonResponse.getJSONArray("response");

                    JSONObject object = array.getJSONObject(0);
                    String id = object.getString("USER_ID");
                    String emailId = object.getString("EMAIL_ID");
                    String username = object.getString("NAME");
                    String education = object.getString("EDUCATION");
                    String chapter = object.getString("SATSANG_CHAPTER");
                    String dateOfBirth = object.getString("DOB");
                    String gender = object.getString("GENDER");
                    String contactNo = object.getString("CONTACT_NO");
                    String countryCode = object.getString("COUNTRY_CODE");
                    String country = object.getString("COUNTRY");
                    ////////////////////
                    String isAccLock = object.getString("IS_ACC_LOCK");
                    String initial_login = object.getString("INITIAL_LOGIN");
                    String loginAttempt = object.getString("LOGIN_ATTEMPT");
                //    String isApproved = object.getString("ISAPPROVED");
                    String isActive = object.getString("ISACTIVE");
                    String Status = object.getString("STATUS");
                    String Status_id = object.getString("STATUS_ID");
                    String Role_id = object.getString("ROLE_ID");
                    //String user_account_status_id =object.getString("user_account_status_id");

                    //If ISACTIVE==0 Account is Blocked
                    //If IS_ACC_LOCK==1 Account is Locked
                    //If ISAPPROVED==0 Account under verification process
                    //If ISAPPROVED==1 Account Approved
                    //If ISAPPROVED==2 Account Rejected

                    if (isActive.equals("0")) {
                        CustomToast("Unable to login. Please contact your satsang coordinator.");
                    } else {
                        if (isAccLock.equals("1")) {
                            CustomToast("Your Account is Lock. Please reset your password.");
                        } else {
                            //if (Status.equals("0")) {
                            if (Status.equals("0")) {
                                CustomToast("Your Account under verification process.");
                            } else if (Status_id.equals("18")) {
                                CustomToast("Login Successful");
                                app.setSession(true);
                                app.setUserId(id);
                                app.setPassword(userPass.getText().toString());
                                app.setUserName(username);
                                app.setEducation(education);
                                app.setDob(dateOfBirth);
                                app.setAge(CalculateAge(dateOfBirth));
                                app.setGender(gender);
                                app.setContact(countryCode + " " + contactNo);
                                app.setEmail(emailId);
                                app.setCountry(country);
                                app.setChapter(chapter);
                                app.setRoleId(Role_id);
                                //Save To fire base
                                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference user = database.getReference("User_Details/" + app.getUserId());

                                user.child("USER_ID").setValue(app.getUserId());
                                user.child("USER_NAME").setValue(app.getUserName());
                                user.child("GENDER").setValue(app.getGender());
                                user.child("DOB").setValue(app.getDob());
                                user.child("Age").setValue(app.getAge());
                                user.child("EDUCATION").setValue(app.getEducation());
                                user.child("CONTACT").setValue(app.getContact());
                                user.child("EMAIL").setValue(app.getEmail());
                                user.child("COUNTRY").setValue(app.getCountry());
                                user.child("CHAPTER").setValue(app.getChapter());
                                user.child("FCM_TOKEN").setValue(FirebaseInstanceId.getInstance().getToken());
                                //****************************************************

                                CustomIntent(MainActivity.class);
                                LoginActivity.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }/*else if (isApproved.equals("2")) {
                                CustomToast("Please contact Support at appsupport@gurukul.com with a description of the issue faced by you.");
                            } */
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
