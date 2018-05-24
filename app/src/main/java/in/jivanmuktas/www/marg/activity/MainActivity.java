package in.jivanmuktas.www.marg.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import in.jivanmuktas.www.marg.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.special.ResideMenu.ResideMenu;

import org.json.JSONObject;

public class MainActivity extends BaseActivity{
    private static final String TAG = "MainActivity";
    private ResideMenu resideMenu;
    private Context mContext;
    Window window;
    JSONObject jsonResponse;
    TextView homeDoc;
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
       final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference user = database.getReference("User_Details/"+app.getUserId());

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
        user.child("FCM_TOKEN").setValue( FirebaseInstanceId.getInstance().getToken());

    }

    private void setUpMenu() {
        // attach to current activity;
        resideMenu = new ResideMenu(this);
        resideMenu.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        LayoutInflater inflater = getLayoutInflater();
        View v=inflater.inflate(R.layout.menu_layout, null);

        resideMenu.addView(v);
        v.findViewById(R.id.profile).setOnClickListener
                (this);
        v.findViewById
                (R.id.volunteering).setOnClickListener(this);
        v.findViewById(R.id.faq).setOnClickListener
                (this);
        v.findViewById
                (R.id.ias_coaching).setOnClickListener(this);
        v.findViewById(R.id.hod).setOnClickListener
                (this);
        v.findViewById
                (R.id.abuot_us).setOnClickListener(this);
        v.findViewById
                (R.id.guidelines).setOnClickListener(this);
        v.findViewById
                (R.id.contact).setOnClickListener(this);
        v.findViewById
                (R.id.notification).setOnClickListener(this);
        v.findViewById
                (R.id.logout).setOnClickListener(this);

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
            Intent hod = new Intent
                    (MainActivity.this, HODBoard.class);
            hod.putExtra("TITLE","HOD Board");
            startActivity(hod);
        }
        if (id == R.id.abuot_us) {
            Intent about = new Intent
                    (MainActivity.this, AboutUs.class);
            startActivity(about);
        }
        if (id == R.id.guidelines) {
            Intent guide = new Intent
                    (MainActivity.this, Project.class);
            guide.putExtra("TITLE","Guidelines");
            startActivity(guide);
        }
        if (id == R.id.contact) {
            Intent guide = new Intent
                    (MainActivity.this, ContactUs.class);
            guide.putExtra("TITLE","Contact Us");
            startActivity(guide);
        }
        if ( id == R.id.notification){
            Intent intent = new Intent
                    (MainActivity.this, Notification.class);
            startActivity(intent);
            finish();
        }
        if (id == R.id.logout) {
            app.setSession(false);
            CustomIntent(LoginActivity.class);
        }
        resideMenu.closeMenu();
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

}
