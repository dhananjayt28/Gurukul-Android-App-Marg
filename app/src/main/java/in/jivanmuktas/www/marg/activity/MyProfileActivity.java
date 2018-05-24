package in.jivanmuktas.www.marg.activity;


import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import in.jivanmuktas.www.marg.R;

import java.util.ArrayList;
import java.util.List;

public class MyProfileActivity extends BaseActivity {

    BaseActivity baseActivity;
    Spinner spinner_role;
    Spinner spinner_education;
    Spinner spinner_sChapter;
    Spinner spinner_sArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            //window.setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_top);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Profile");

       /* Toolbar toolbarBottom=(Toolbar)findViewById(R.id.toolbar_bottom);
        toolbarBottom.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.home:
                        // TODO: Other Cases
                        break;
                    // TODO: Other Cases
                }
                return true;
            }
        });
*/
        // Inflate a menu to be displayed in the toolbar
       // toolbarBottom.inflateMenu(R.menu.main_bottom);

        //spinner_role = (Spinner)findViewById(R.id.spinner_role);

        spinner_education = (Spinner)findViewById(R.id.spinner_education);

        spinner_sChapter = (Spinner)findViewById(R.id.spinner_sChapter);

        spinner_sArea = (Spinner)findViewById(R.id.spinner_sArea);

        // Spinner Drop down elements
        List<String> role = new ArrayList<>();
        role.add("Select Role");
        role.add("Admin");
        role.add("User");
        role.add("Etc");

        List<String> education = new ArrayList<>();
        education.add("Select Education");
        education.add("IX");
        education.add("X");
        education.add("XI");
        education.add("XII");

        List<String> chapter = new ArrayList<>();
        chapter.add("Select Chapter");
        chapter.add("Chapter I");
        chapter.add("Chapter II");
        chapter.add("Chapter III");
        chapter.add("Chapter IV");
        chapter.add("Chapter V");
        chapter.add("Chapter VI");
        chapter.add("Chapter VII");

        List<String> area = new ArrayList<>();
        area.add("Select Area");
        area.add("Bandra");
        area.add("Dadar");
        area.add("Thane");
        area.add("Pune");


        /*spinner_role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,R.layout.activity_spinner_item, role);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_role.setAdapter(dataAdapter);*/

        spinner_education.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                String item = parent.getItemAtPosition(position).toString();

                /*// Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        //simple_spinner_item
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, R.layout.activity_spinner_item, education);

        // Drop down layout style - list view with radio button
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_education.setAdapter(dataAdapter2);

        spinner_sChapter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                String item = parent.getItemAtPosition(position).toString();

                /*// Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        //simple_spinner_item
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<>(this, R.layout.activity_spinner_item, chapter);

        // Drop down layout style - list view with radio button
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_sChapter.setAdapter(dataAdapter3);

        spinner_sArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                String item = parent.getItemAtPosition(position).toString();

                /*// Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        //simple_spinner_item
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(this, R.layout.activity_spinner_item, area);

        // Drop down layout style - list view with radio button
        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_sArea.setAdapter(dataAdapter4);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
