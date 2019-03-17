package in.jivanmuktas.www.marg.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import in.jivanmuktas.www.marg.constant.Constant;
import in.jivanmuktas.www.marg.network.HttpClient;
import in.jivanmuktas.www.marg.network.HttpGetHandler;
import in.jivanmuktas.www.marg.R;
public class CreateScheduleActivity extends BaseActivity {
    LinearLayout regisrtationLayout;
    TextView age;
    EditText etPersonName;
    EditText etSubjectName;
    EditText etComment;
    TextView tvNote;
    LinearLayout subjectView;
    ArrayList<CheckBox> subjectCkBox =new ArrayList<>();
    ArrayList<String> subIdList = new ArrayList<>();
    ArrayList<EditText> etPersonList = new ArrayList<>();
   // LinearLayout bcom, baeco, bahis;
    JSONObject jsonResponse;

    TextView classTxt;
    Spinner classes;

    LinearLayout age_group;
    EditText scheStartDate, scheEndDate;
    TextView availableProject, availableDate;

    Button createSchedule;
    private SimpleDateFormat dateFormatter;
    private DatePickerDialog datePickerDialog;
    Spinner person;

    LinearLayout personLayout, subjectLayout, volDate, noteLayout;
    LinearLayout addPerson;
    EditText editText;
    ImageView projName;
    TextInputLayout comtoaprov;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_schedule_bk);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTop4);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Create Schedule");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        regisrtationLayout = (LinearLayout) findViewById(R.id.regisrtationLayout);
        age_group = (LinearLayout) findViewById(R.id.age_group);

        classTxt = (TextView) findViewById(R.id.classTxt);
        classes = (Spinner) findViewById(R.id.classes);
        subjectView = (LinearLayout) findViewById(R.id.subjectView);
        tvNote = (TextView) findViewById(R.id.tvNote);

      /*bcom = (LinearLayout) findViewById(R.id.bcom);
        baeco = (LinearLayout) findViewById(R.id.baeco);
        bahis = (LinearLayout) findViewById(R.id.bahis);*/

        availableProject = (TextView) findViewById(R.id.availableProject);
        availableDate = (TextView) findViewById(R.id.availableDate);
        availableProject.setText(getIntent().getExtras().getString("PROJECT"));
        availableDate.setText(getIntent().getExtras().getString("START_DATE")+"   -   "+getIntent().getExtras().getString("END_DATE"));

        age = (TextView) findViewById(R.id.age);
        etPersonName = (EditText) findViewById(R.id.etPersonName);
        etSubjectName = (EditText) findViewById(R.id.etSubjectName);
        //etTopic = (EditText) findViewById(R.id.etTopic);
        etComment = (EditText) findViewById(R.id.etComment);
        createSchedule = (Button) findViewById(R.id.createSchedule);

        person = (Spinner) findViewById(R.id.person);
        personLayout = (LinearLayout) findViewById(R.id.personLayout);
        addPerson = (LinearLayout) findViewById(R.id.addPerson);
        subjectLayout = (LinearLayout) findViewById(R.id.subjectLayout);
        volDate = (LinearLayout) findViewById(R.id.volDate);
        comtoaprov = (TextInputLayout) findViewById(R.id.comtoaprov);
        noteLayout = (LinearLayout) findViewById(R.id.noteLayout);
        projName = (ImageView) findViewById(R.id.projName);

        if (getIntent().getExtras().getString("PROJECT").equals("Nivritti Gurukul")) {
            projName.setImageDrawable(getResources().getDrawable(R.drawable.gurukul));
        }
        if (getIntent().getExtras().getString("PROJECT").equals("Workshop")) {
            classTxt.setVisibility(View.GONE);
            classes.setVisibility(View.GONE);
            subjectLayout.setVisibility(View.GONE);
         //   comtoaprov.setVisibility(View.GONE);
            personLayout.setVisibility(View.VISIBLE);
            addPerson.setVisibility(View.VISIBLE);
            projName.setImageDrawable(getResources().getDrawable(R.drawable.workshop));
        }
        if (getIntent().getExtras().getString("PROJECT").equals("Gita Distribution")) {
            tvNote.setText(getIntent().getExtras().getString("MESSAGE"));
            classTxt.setVisibility(View.GONE);
            classes.setVisibility(View.GONE);
            volDate.setVisibility(View.GONE);
            subjectLayout.setVisibility(View.GONE);
            comtoaprov.setVisibility(View.GONE);
            noteLayout.setVisibility(View.VISIBLE);
            projName.setImageDrawable(getResources().getDrawable(R.drawable.distribution));
        }


        ArrayAdapter<CharSequence> clas = ArrayAdapter.createFromResource(CreateScheduleActivity.this, R.array.classname, R.layout.spinner_item);
        clas.setDropDownViewResource(R.layout.spinner_dropdown_item);
        classes.setAdapter(clas);

        ArrayAdapter<CharSequence> adapterPresonNo = ArrayAdapter.createFromResource(CreateScheduleActivity.this, R.array.personno, R.layout.spinner_item);
        adapterPresonNo.setDropDownViewResource(R.layout.spinner_dropdown_item);
        person.setAdapter(adapterPresonNo);

        person.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                etPersonList.clear();
                if (position == 0) {
                    addPerson.removeAllViews();
                } else if (position > 0) {
                    addPerson.removeAllViews();
                    for (int i = 1; i <= position; i++) {
                        editText = new EditText(CreateScheduleActivity.this);
                        editText.setHint("Enter person " + i + " Email Id");
                        addPerson.addView(editText);
                        etPersonList.add(editText);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        classes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("!!!Position", "" + position);
                if (position == 0) {
                    subjectView.removeAllViews();
                    subjectCkBox.clear();
                    subIdList.clear();
                    /*TextView tv = new TextView(CreateScheduleActivity.this);
                    tv.setText(" Sorry! no subject found");//If Position 0 is selected Msg will show
                    subjectView.addView(tv);*/
                } else if (position == 1) {
                    new GetSubject().execute("1");
                } else if (position == 2) {
                    new GetSubject().execute("2");
                } else if (position == 3) {
                    new GetSubject().execute("3");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        scheStartDate = (EditText) findViewById(R.id.scheStartDate);
        scheEndDate = (EditText) findViewById(R.id.scheEndDate);
        scheStartDate.setFocusable(false);
        scheEndDate.setFocusable(false);

        scheStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                datePickerDialog = new DatePickerDialog(CreateScheduleActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        scheStartDate.setText(dateFormatter.format(newDate.getTime()));
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

               /* Date date;
                try {
                    ////////////////// Select Date In between Two date
                    date = new SimpleDateFormat("dd MMM yyyy").parse(getIntent().getExtras().getString("START_DATE"));
                    datePickerDialog.getDatePicker().setMinDate(date.getTime());
                    date = new SimpleDateFormat("dd MMM yyyy").parse(getIntent().getExtras().getString("END_DATE"));
                    datePickerDialog.getDatePicker().setMaxDate(date.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }*/

                datePickerDialog.show();

            }
        });

        scheEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                datePickerDialog = new DatePickerDialog(CreateScheduleActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        scheEndDate.setText(dateFormatter.format(newDate.getTime()));

                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
               /* Date date;
                try {
                    ////////////////// Select Date In between Two date
                    date = new SimpleDateFormat("dd MMM yyyy").parse(getIntent().getExtras().getString("START_DATE"));
                    datePickerDialog.getDatePicker().setMinDate(date.getTime());
                    date = new SimpleDateFormat("dd MMM yyyy").parse(getIntent().getExtras().getString("END_DATE"));
                    datePickerDialog.getDatePicker().setMaxDate(date.getTime());

                } catch (ParseException e) {
                    e.printStackTrace();
                }*/
                datePickerDialog.show();
            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra("TEST")) {
            /*tvStartDate.setKeyListener(null);
            tvEndDate.setKeyListener(null);
            tvCheckInDate.setKeyListener(null);
            tvCheckInTime.setKeyListener(null);
            tvCheckOutDate.setKeyListener(null);
            tvCheckOutTime.setKeyListener(null);*/
            age.setKeyListener(null);
            etPersonName.setKeyListener(null);
            etSubjectName.setKeyListener(null);
            //etTopic.setKeyListener(null);
            etComment.setKeyListener(null);
            //spinnerNumberOfPersons.setKeyListener(null)
            //spinnerRelationship.setKeyListener(null);
        }


        Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final int mon = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);


        final List<String> AGE_GROUP = new ArrayList<>();
        AGE_GROUP.add("Select Age Group");
        AGE_GROUP.add("0-2 Yrs");
        AGE_GROUP.add("2-5 Yrs");
        AGE_GROUP.add("5-8 Yrs");
        AGE_GROUP.add("8-13 Yrs");
        AGE_GROUP.add("13-19 Yrs");

        List<String> relationship = new ArrayList<>();
        relationship.add("Select Relation With Person");
        relationship.add("Mother");
        relationship.add("Father");
        relationship.add("Wife");
        relationship.add("Husband");
        relationship.add("Sister");
        relationship.add("Brother");
        relationship.add("Cousin");
        relationship.add("Friend");
        relationship.add("Others");

        List<String> kids = new ArrayList<>();
        kids.add("Select No. of Kids");
        kids.add("1");
        kids.add("2");
        kids.add("3");
        kids.add("4");
        kids.add("5");

        createSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNetworkAvailable()) {
                    if(isValid()) {
                        new RegesterAsyntask().execute();

                    }
                }

                /*AlertDialog.Builder builder = new AlertDialog.Builder(CreateScheduleActivity.this);
                builder.setMessage(Html.fromHtml("<font color='#FA7F27'>Please check your status in a couple of days.</font>"))
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.alerticon1);
                AlertDialog alert = builder.create();
                alert.setTitle(Html.fromHtml("<font color='#FF7F27'>Registration Successful</font>"));
                alert.show();*/
            }
        });

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

    public boolean isNumeric(String str) {
        try {
            int d = Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    //***********************************************************************
    public class RegesterAsyntask extends AsyncTask<String, String, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDailog();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                JSONArray reqArr = new JSONArray();
                JSONObject reqObj = new JSONObject();
                if(getIntent().getExtras().getString("PROJECT").equals("Nivritti Gurukul")){
                    reqObj.put("EVENT_SYS_ID",getIntent().getExtras().getString("EVENT"));
                    reqObj.put("USER_ID",app.getUserId());
                    reqObj.put("EVENT_TYPE","1");
                    reqObj.put("START_DATE", scheStartDate.getText().toString());
                    reqObj.put("END_DATE", scheEndDate.getText().toString());
                    reqObj.put("COMMENT",etComment.getText().toString());
                    reqObj.put("CLASSID",classes.getSelectedItemPosition());
                    String subIds ="";
                    for (int i=0; i<subjectCkBox.size(); i++){
                        if(subjectCkBox.get(i).isChecked()){
                            subIds = subIds+subIdList.get(i)+",";//For multiple subject selected
                        }
                    }
                    if(subIds.length()>0){
                        subIds=subIds.substring(0, (subIds.length() - 1));// for delete comma(,) from tvend of the String
                    }
                    reqObj.put("SUBJECTID",subIds);
                }else if(getIntent().getExtras().getString("PROJECT").equals("Workshop")){
                    reqObj.put("EVENT_SYS_ID",getIntent().getExtras().getString("EVENT"));
                    reqObj.put("USER_ID",app.getUserId());
                    reqObj.put("EVENT_TYPE","2");
                    reqObj.put("START_DATE", scheStartDate.getText().toString());
                    reqObj.put("END_DATE", scheEndDate.getText().toString());
                    reqObj.put("COMMENT",etComment.getText().toString());
                    reqObj.put("PERSON_COUNT",etPersonList.size());
                    if(etPersonList.size() == 0){
                        reqObj.put("CO_PERSON_EMAIL1","");
                        reqObj.put("CO_PERSON_EMAIL2","");
                    }
                    if(etPersonList.size() == 1){
                        reqObj.put("CO_PERSON_EMAIL1",etPersonList.get(0).getText().toString().trim());
                        reqObj.put("CO_PERSON_EMAIL2","");
                    }
                    if(etPersonList.size() == 2){
                        reqObj.put("CO_PERSON_EMAIL1",etPersonList.get(0).getText().toString().trim());
                        reqObj.put("CO_PERSON_EMAIL2",etPersonList.get(1).getText().toString().trim());
                    }

                }else if (getIntent().getExtras().getString("PROJECT").equals("Gita Distribution")){
                    reqObj.put("EVENT_SYS_ID",getIntent().getExtras().getString("EVENT") );
                    reqObj.put("EVENT_TYPE","3");
            //        reqObj.put("START_DATE",scheStartDate.getText().toString());
            //        reqObj.put("END_DATE",scheEndDate.getText().toString());
                    reqObj.put("USER_ID",app.getUserId());
                }

                reqArr.put(reqObj);
                System.out.println("!!!reqArr  " + reqArr);

                String response = HttpClient.SendHttpPost(Constant.EVENT_REGISTRTION, reqObj.toString());

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
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            dismissProgressDialog();

            try {
                CustomToast(jsonResponse.getString("response"));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
//*********************************************************************
//*********************************************************************
public class GetSubject extends AsyncTask<String, String, Boolean> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        showProgressDailog();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String sub = strings[0];
        try {
            JSONArray reqArr = new JSONArray();
            JSONObject reqObj = new JSONObject();

            reqArr.put(reqObj);
            System.out.println("!!!reqArr  " + reqArr);
            HttpGetHandler httpGetHandler = new HttpGetHandler();
            String response = httpGetHandler.makeServiceCall(Constant.GET_SUBJECT+sub);

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
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        dismissProgressDialog();
        subjectView.removeAllViews();
        subjectCkBox.clear();
        subIdList.clear();
        if(aBoolean){
            try {
                JSONArray array = jsonResponse.getJSONArray("response");
                for(int i =0 ; i<array.length();i++){
                    CheckBox ck = new CheckBox(CreateScheduleActivity.this);
                    JSONObject object = array.getJSONObject(i);

                    String subject_id = object.getString("SUB_ID");
                    String subject_name = object.getString("SUB_NAME");

                    ck.setText(subject_name);
                    subjectView.addView(ck);
                    subjectCkBox.add(ck); //Store all check box to arrayList
                    subIdList.add(subject_id);// Store all id to arrayList
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            /*TextView tv = new TextView(CreateScheduleActivity.this);
            tv.setText("Sorry! no subject found");//If Position 0 is selected Msg will show
            tv.setGravity(View.TEXT_ALIGNMENT_CENTER);
            subjectView.addView(tv);*/
        }

    }
}
//*********************************************************************
    public boolean isValid(){
        boolean flag= true;
        if(getIntent().getExtras().getString("PROJECT").equals("Nivritti Gurukul")){
            if(scheStartDate.getText().length()<1){
                flag = false;
                SnackbarRed(R.id.regisrtationLayout,"Please enter Volunteering starting date");
            }else if(scheEndDate.getText().length()<1){
                flag = false;
                SnackbarRed(R.id.regisrtationLayout,"Please enter Volunteering ending date");
            }else if(classes.getSelectedItemPosition()==0){
                flag = false;
                SnackbarRed(R.id.regisrtationLayout,"Please select class");
            }else if(subjectCkBox.size()>0){
                int count=0;
                for(int i=0;i<subjectCkBox.size();i++){
                    if(subjectCkBox.get(i).isChecked()){
                        count++;
                    }
                }
                if (count==0){
                    flag=false;
                    SnackbarRed(R.id.regisrtationLayout,"Please select Subject");
                }else if(etComment.getText().toString().trim().length()<1){
                    flag = false;
                    etComment.setError("Please fill this field.");
                    editTextFocus(etComment);
                }
            }else if(etComment.getText().toString().trim().length()<1){
                flag = false;
                etComment.setError("Please fill this field.");
                editTextFocus(etComment);
            }
        }else if(getIntent().getExtras().getString("PROJECT").equals("Workshop")){
            if(scheStartDate.getText().length()<1){
                flag = false;
                SnackbarRed(R.id.regisrtationLayout,"Please enter Volunteering starting date");
            }else if(scheEndDate.getText().length()<1){
                flag = false;
                SnackbarRed(R.id.regisrtationLayout,"Please enter Volunteering ending date");
            }else if(etPersonList.size()>0){
                for(int i=0;i<etPersonList.size();i++){
                    if(etPersonList.get(i).getText().toString().trim().length()<1){
                        etPersonList.get(i).setError("Please fill this field.");
                        flag = false;
                        break;
                    }else if(!mailValidationCk(etPersonList.get(i).getText().toString())) {
                        etPersonList.get(i).setError("Please enter valid email");
                        etPersonList.get(i).setText("");
                        flag = false;
                        break;
                    }else if(etComment.getText().toString().trim().length()<1){
                        flag = false;
                        etComment.setError("Please fill this field.");
                        editTextFocus(etComment);
                    }
                }

            }else if(etComment.getText().toString().trim().length()<1){
                flag = false;
                etComment.setError("Please fill this field.");
                editTextFocus(etComment);
            }
        }
        return flag;
    }
}
