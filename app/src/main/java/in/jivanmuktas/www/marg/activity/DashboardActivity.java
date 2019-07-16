package in.jivanmuktas.www.marg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import in.jivanmuktas.www.marg.R;
import in.jivanmuktas.www.marg.fragment.ApprovedFragment;
import in.jivanmuktas.www.marg.fragment.AvailableFragment;
import in.jivanmuktas.www.marg.fragment.RejectedFragment;

public class DashboardActivity extends BaseActivity {
    private static final String TAG = "DashboardActivity";
    ViewPagerAdapter adapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Log.i("!!!Activity",TAG);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Marg");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_logout) {
//            MyApplication.getInstance().setSession(false);
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            //DashboardActivity.this.finish();
            return true;
        }
        if (id == R.id.action_user_profile) {
            Intent approvals = new Intent(DashboardActivity.this, ViewProfile.class);
            startActivity(approvals);
        }
        // For notifications
        if ( id == R.id.action_notification){
            Intent intent = new Intent(DashboardActivity.this, Notification.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
*/

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private  int NUM_ITEMS = 3;

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to displayGeneral for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return AvailableFragment.newInstance(0, "AVAILABLE");
                case 1:
                    return ApprovedFragment.newInstance(1, "APPROVED");
                /*case 2:
                    return AppliedFragment.newInstance(2, "APPLIED");*/
                case 2:
                    return RejectedFragment.newInstance(2, "REJECTED");
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0)
                return "AVAILABLE";
            else if(position == 1)
                return "APPROVED";
            /*else if(position==2)
                return "APPLIED";*/
            else
                return "REJECTED";
        }

    }
 }
