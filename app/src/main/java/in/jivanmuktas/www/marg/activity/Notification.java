package in.jivanmuktas.www.marg.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import in.jivanmuktas.www.marg.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import in.jivanmuktas.www.marg.database.DataBase;
import in.jivanmuktas.www.marg.fragment.GenNotifiFrag;
import in.jivanmuktas.www.marg.fragment.HODNotifiFrag;

public class Notification extends BaseActivity {

    DataBase dataBase;
    PagerAdapter adapter;
    ViewPager viewPager;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarNoti);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Notification");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Notification.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout.addTab(tabLayout.newTab().setText("General"));
        tabLayout.addTab(tabLayout.newTab().setText("Subject HOD"));

        adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        ///////***************Save get Intent Data to Database***************///////////
        dataBase = new DataBase(this);
        Calendar c = Calendar.getInstance();
        System.out.println("Current time =&gt; " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        String formattedDate = df.format(c.getTime());
        // Now formattedDate have current date/time
        String date = formattedDate;

        if (getIntent().getExtras() != null) {
            if(getIntent().getExtras().getString("TYPE").equals("0")){
                TabLayout.Tab tab = tabLayout.getTabAt(0);
                tab.select();
            }else if(getIntent().getExtras().getString("TYPE").equals("1")){
                TabLayout.Tab tab = tabLayout.getTabAt(1);
                tab.select();
            }else if(getIntent().getExtras().getString("TYPE").equals("GENERAL")){
                String msg = String.valueOf(getIntent().getExtras().getString("MESSAGE"));
                dataBase.addToGeneral(msg, date);
                TabLayout.Tab tab = tabLayout.getTabAt(0);
                tab.select();
            }else if(getIntent().getExtras().getString("TYPE").equals("HOD")){
                String msg = String.valueOf(getIntent().getExtras().getString("MESSAGE"));
                String mobile = String.valueOf(getIntent().getExtras().getString("MOBILE"));
                String email = String.valueOf(getIntent().getExtras().getString("EMAIL"));
                dataBase.addToHOD(msg,date,mobile,email);
                TabLayout.Tab tab = tabLayout.getTabAt(1);
                tab.select();
            }
        }

        ////////////*************** Save get Intent Data to Database tvend *****************///////////
    }
    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;
        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    GenNotifiFrag tab1 = new GenNotifiFrag();
                    return tab1;
                case 1:
                    HODNotifiFrag tab2 = new HODNotifiFrag();
                    return tab2;
                default:
                    return null;
            }
        }
        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Notification.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
