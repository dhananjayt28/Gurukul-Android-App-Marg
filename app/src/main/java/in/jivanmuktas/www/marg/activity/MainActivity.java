package in.jivanmuktas.www.marg.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import in.jivanmuktas.www.marg.R;
import in.jivanmuktas.www.marg.constant.Constant;
import in.jivanmuktas.www.marg.database.JivanmuktasDB;
import in.jivanmuktas.www.marg.singleton.VolleySingleton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.special.ResideMenu.ResideMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class MainActivity extends BaseActivity{
    private static final String TAG = "MainActivity";
    private ResideMenu resideMenu;
    private Context mContext;
    Window window;
    JSONObject jsonResponse;
    TextView homeDoc;
    JivanmuktasDB database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("!!!Activity",TAG);
        window = (MainActivity.this).getWindow();
        mContext = this;
        homeDoc = (TextView) findViewById(R.id.homeDoc);
        homeDoc.setText(getResources().getString(R.string.hometext));

        setUpMenu();

        database = JivanmuktasDB.getInstance(this);
        database.open();

        //Creating Sync button for the sync
        if (database.selectSync().equals("0")) {
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//Hide Title .use before setContentView
            dialog.setContentView(R.layout.sync_alert_layout);
        //    dialog.setCancelable(false);
            TextView tvDesc = (TextView) dialog.findViewById(R.id.tvDesc);
            tvDesc.setText(R.string.dialog_sync);
            Button sync = (Button) dialog.findViewById(R.id.sync);
            Button exit = (Button) dialog.findViewById(R.id.exit);
            sync.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GetUserProfile();
                    dialog.dismiss();
                }
            });
            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            dialog.show();
        } else {
            String LastSyncTimeStamp = database.selectLastSyncStamp();
            String currentTimeStamp = String.valueOf(new Date().getTime());
            String dateStart = new String();
            String dateStop = new String();
            dateStart = TimeToDate(LastSyncTimeStamp);
            dateStop = TimeToDate(currentTimeStamp);
            int diffHour = DiffTime(dateStart, dateStop);

            if (diffHour >= 24) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//Hide Title .use before setContentView
                dialog.setContentView(R.layout.sync_alert_layout);
                dialog.setCancelable(false);
                TextView tvDesc = (TextView) dialog.findViewById(R.id.tvDesc);
                tvDesc.setText(R.string.Sync_Required);
                Button sync = (Button) dialog.findViewById(R.id.sync);
                Button exit = (Button) dialog.findViewById(R.id.exit);
                sync.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GetUserProfile();
                        dialog.dismiss();

                    }
                });
                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                dialog.show();

            } else {
            //    Toast.makeText(MainActivity.this, "No Sync available.", Toast.LENGTH_LONG).show();
            }
        }


    }

    private void setUpMenu() {
        // attach to current activity;
        resideMenu = new ResideMenu(this);
        resideMenu.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        LayoutInflater inflater = getLayoutInflater();
        View v=inflater.inflate(R.layout.menu_layout, null);

        resideMenu.addView(v);
        v.findViewById(R.id.profile).setOnClickListener(this);
        v.findViewById(R.id.volunteering).setOnClickListener(this);
        v.findViewById(R.id.faq).setOnClickListener(this);
        v.findViewById(R.id.ias_coaching).setOnClickListener(this);
        v.findViewById(R.id.hod).setOnClickListener(this);
        v.findViewById(R.id.abuot_us).setOnClickListener(this);
        v.findViewById(R.id.guidelines).setOnClickListener(this);
        v.findViewById(R.id.contact).setOnClickListener(this);
        v.findViewById(R.id.notification).setOnClickListener(this);
        v.findViewById(R.id.logout).setOnClickListener(this);

        resideMenu.attachToActivity(this);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.65f);
        resideMenu.setMenuListener(menuListener);///Menu open/close listener
        resideMenu.setSwipeDirectionDisable(1);

        findViewById(R.id.title_bar_left_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
    }



    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.profile) {
            Intent approvals = new Intent
                    (MainActivity.this, ViewProfile.class);
            startActivity(approvals);
        }
        if(id == R.id.volunteering){
            Intent dashboard = new Intent(MainActivity.this, DashboardActivity.class);
            startActivity(dashboard);
        }
        if (id == R.id.faq) {
            Intent faq = new Intent
                    (MainActivity.this, Project.class);
            faq.putExtra("TITLE","FAQ");
            startActivity(faq);
        }
        if (id == R.id.ias_coaching) {
            Intent ias = new Intent
                    (MainActivity.this, IASCoaching.class);
            ias.putExtra("TITLE","IAS Coaching");
            startActivity(ias);
        }
        if (id == R.id.hod) {
            Log.d("!!!roleid",app.getRoleId());
            if(app.getRoleId().equals("5")){
                Intent hod = new Intent(MainActivity.this, HODBoard.class);
                hod.putExtra("TITLE", "HOD Board");
                startActivity(hod);
            } else {
                Intent hod = new Intent(MainActivity.this,PermissionDenied.class);
                hod.putExtra("TITLE", "PERMISSION DENIED");
                startActivity(hod);
            }
        }
        if (id == R.id.abuot_us) {
            Intent about = new Intent(MainActivity.this, AboutUs.class);
            startActivity(about);
        }
        if (id == R.id.guidelines) {
            Intent guide = new Intent(MainActivity.this, Project.class);
            guide.putExtra("TITLE","Guidelines");
            startActivity(guide);
        }
        if (id == R.id.contact) {
            Intent guide = new Intent(MainActivity.this, ContactUs.class);
            guide.putExtra("TITLE","Contact Us");
            startActivity(guide);
        }
        if ( id == R.id.notification){
            Intent intent = new Intent(MainActivity.this, Notification.class);
            startActivity(intent);
            finish();
        }
        if (id == R.id.logout) {
     //       app.setSession(false);
     //      CustomIntent(LoginActivity.class);
            MainActivity.this.finish();
        }
    //    resideMenu.closeMenu();
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(getResources().getColor(R.color.blue));//////// Change color of status bar
            }*/
        }

        @Override
        public void closeMenu() {
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));//////// Change color of status bar
            }*/
        }
    };
    @Override
    public void onBackPressed() {
        if (resideMenu.isOpened()) {
            resideMenu.closeMenu();
        } else {
            super.onBackPressed();
        }
    }

    public void GetUserProfile(){
        showProgressDailog();
        final String url = Constant.ProfileView + "?user_id=" + app.getUserId() ;
        Log.d("!!!urlProfile",url);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dismissProgressDialog();
                        try {
                            JSONObject object = response;
                            if(object.getString("status").equals("true")){
                                Log.i("!!!Request",object.toString());
                                JSONArray jsonArray = object.getJSONArray("response");
                                Log.d("!!!response",response.toString());

                                JSONObject object1 = jsonArray.getJSONObject(0);
                                String UserId = object1.getString("USER_ID");
                                String title = object1.getString("TITLE");
                                String Roleid = object1.getString("ROLE_ID");
                                String Name = object1.getString("NAME");
                                String Gender = object1.getString("GENDER");
                                String dob = object1.getString("DOB");
                                String mobile_no = object1.getString("MOBILE_NO");
                                String emailId = object1.getString("EMAIL_ID");
                                String country = object1.getString("COUNTRY");
                                String countryCode = object1.getString("COUNTRY_CODE");
                                String city = object1.getString("CITY");
                                String education = object1.getString("EDUCATION");
                                String satsang_chap = object1.getString("SATSANG_CHAPTER");
                                String other_activity = object1.getString("HELP_IN_OTHER_ACTIVITY");
                                String status = object1.getString("STATUS");

                                app.setSession(true);
                                app.setUserId(UserId);
                                app.setRoleId(Roleid);
                                app.setGender(Gender);
                                app.setUserName(Name);
                                app.setDob(dob);
                                app.setAge(CalculateAge(dob));
                                app.setContact(countryCode + " " +mobile_no);
                                app.setEmail(emailId);
                                app.setCountry(country);
                                app.setEducation(education);
                                app.setChapter(satsang_chap);
                                app.setCity(city);
                                database.updateSync(String.valueOf(new Date().getTime()));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", error.toString());
            }
        });
        VolleySingleton.getInstance(this).addToRequestQueue(getRequest);
    }

}
