package in.jivanmuktas.www.marg.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import in.jivanmuktas.www.marg.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import in.jivanmuktas.www.marg.constant.Constant;
import in.jivanmuktas.www.marg.network.HttpClient;
import in.jivanmuktas.www.marg.network.HttpGetHandler;
import in.jivanmuktas.www.marg.network.HttpPutHandler;

public class Nivritti extends BaseActivity {
    private SimpleDateFormat dateFormatter;
    private DatePickerDialog datePickerDialog;
    String EVENT_ID,status;
    TextView calendar,alottedSubject,commentToApprover,tvCkInTime,tvCkOutTime;
    EditText etCheckInDate,etCheckOutDate,etCheckInTime,etCheckOutTime,commentForHod;
    LinearLayout timeView,subjectLayout,dateTimeView,topicView,layoutNivritti;
    TextInputLayout commentForHodLayout;
    Button submit;
    JSONObject jsonResponse;
    ArrayList<Spinner> actionList = new ArrayList<Spinner>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nivritti);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Jivanmuktas");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        try {
        EVENT_ID = getIntent().getExtras().getString("EVENT_ID");
        } catch (Exception e) {

        }

        calendar = (TextView) findViewById(R.id.calendar);
        timeView = (LinearLayout) findViewById(R.id.timeView);
        tvCkInTime = (TextView) findViewById(R.id.tvCkInTime);
        tvCkOutTime = (TextView) findViewById(R.id.tvCkOutTime);
        alottedSubject = (TextView) findViewById(R.id.alottedSubject);
        commentToApprover = (TextView) findViewById(R.id.tvcommentToApprover);
        dateTimeView  = (LinearLayout) findViewById(R.id.dateTimeView);
        etCheckInDate = (EditText) findViewById(R.id.etCheckInDate);
        etCheckOutDate = (EditText) findViewById(R.id.etCheckOutDate);
        etCheckInTime = (EditText) findViewById(R.id.etCheckInTime);
        etCheckOutTime = (EditText) findViewById(R.id.etCheckOutTime);
        topicView = (LinearLayout) findViewById(R.id.topicView);
        subjectLayout = (LinearLayout) findViewById(R.id.subjectLayout);
        commentForHod = (EditText) findViewById(R.id.commentForHod);
        commentForHodLayout = (TextInputLayout) findViewById(R.id.commentForHodLayout);
        submit = (Button) findViewById(R.id.submit);
        layoutNivritti = (LinearLayout) findViewById(R.id.layoutNivritti);

        timeView.setVisibility(View.GONE);
        commentForHodLayout.setVisibility(View.GONE);
        topicView.setVisibility(View.GONE);
        if (isNetworkAvailable()) {
            new GetAllData().execute();
        }else {
            finish();
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()) {
                    new SubmitData().execute();
                }
            }
        });


        etCheckInDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                datePickerDialog = new DatePickerDialog(Nivritti.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        etCheckInDate.setText(dateFormatter.format(newDate.getTime()));

                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
        etCheckOutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                datePickerDialog = new DatePickerDialog(Nivritti.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        etCheckOutDate.setText(dateFormatter.format(newDate.getTime()));

                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();

            }
        });
        etCheckInTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Nivritti.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String format;
                        if (selectedHour == 0) {
                            selectedHour += 12;
                            format = "AM";
                        }
                        else if (selectedHour == 12) {
                            format = "PM";
                        }
                        else if (selectedHour > 12) {
                            selectedHour -= 12;
                            format = "PM";
                        }
                        else {
                            format = "AM";
                        }
                        etCheckInTime.setText( selectedHour + ":" + selectedMinute+" "+format);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        etCheckOutTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Nivritti.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String format;
                        if (selectedHour == 0) {
                            selectedHour += 12;
                            format = "AM";
                        }
                        else if (selectedHour == 12) {
                            format = "PM";
                        }
                        else if (selectedHour > 12) {
                            selectedHour -= 12;
                            format = "PM";
                        }
                        else {
                            format = "AM";
                        }
                        etCheckOutTime.setText( selectedHour + ":" + selectedMinute+" "+format);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
    }
    //***#***#***#***#***#***#***#***#***#***#***#***#***#***#***#***#***#***#

    public class GetAllData extends AsyncTask<String, String, Boolean>{
        JSONObject jsonResponse;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDailog();
        }
        @Override
        protected Boolean doInBackground(String... strings) {
            String response;
            HttpGetHandler handler = new HttpGetHandler();
            try {
                response = handler.makeServiceCall(Constant.VOLUNTEER_EVENT_APPROVE+"id="+app.getUserId()+"&eventid="+EVENT_ID);
                //Log.i("!!!Request",Constant.VOLUNTEER_EVENT_APPROVE+"id="+app.getUserId()+"&eventid="+EVENT_ID );
                //response = AssetJSONFile("nivrittieventview.json",Nivritti.this);
                jsonResponse = new JSONObject(response);
                Log.i("!!!Response", response);
                if (jsonResponse.getBoolean("status")) {
                    return true;
                } else
                    return false;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (prsDlg.isShowing()) {
                prsDlg.dismiss();
            }
            if (aBoolean){
                try {
                    JSONArray array = jsonResponse.getJSONArray("response");
                    JSONObject object = array.getJSONObject(0);
                    String projectName = object.getString("PROJECT_NAME");
                    String startDate = object.getString("START_DATE");
                    String endDate = object.getString("END_DATE");
                    String allottedSubject = object.getString("ALLOTTED_SUBJECT");
                    String comment = object.getString("COMMENT_TO_APPROVER");
                    String checkInDate = object.getString("CHECK_IN_DATE");
                    String checkOutDate = object.getString("CHECK_OUT_DATE");
                    String checkInTime = object.getString("CHECK_IN_TIME");
                    String checkOutTime = object.getString("CHECK_OUT_TIME");
                    String commentForHod = object.getString("COMMENT_FOR_HOD");
                    status = object.getString("STATUS");//Status for stage 1 or 2

                    if (status.equals("1")) {
                        tvCkInTime.setText(checkInTime);
                        tvCkOutTime.setText(checkOutTime);
                        timeView.setVisibility(View.VISIBLE);
                        dateTimeView.setVisibility(View.GONE);
                        topicView.setVisibility(View.VISIBLE);
                    JSONObject subject = object.getJSONObject("SUBJECT");
                        if (subject.length()==0){
                        TextView tv = new TextView(Nivritti.this);
                        tv.setText("No topic allocated yet");
                        tv.setTextSize(20);
                        subjectLayout.addView(tv);
                        submit.setVisibility(View.GONE);
                    }else {
                        commentForHodLayout.setVisibility(View.VISIBLE);
                    Iterator<String> iterator = subject.keys();//Get array name from json Object
                    while (iterator.hasNext()) {
                        String currentKey = iterator.next();
                        Log.i("!!! SUBJECT -> ", currentKey);
                        JSONArray jsonArray = subject.getJSONArray(currentKey);

                        TextView tv = new TextView(Nivritti.this);
                        tv.setText("Subject: " + currentKey);
                        tv.setTextSize(20);
                        subjectLayout.addView(tv);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject topic = jsonArray.getJSONObject(i);
                            String SUBJECT_ID = topic.getString("SUBJECT_ID");
                            String NAME = topic.getString("NAME");
                            final String SOURCE = topic.getString("SOURCE");

                            //#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#
                            LayoutInflater inflater = getLayoutInflater();
                            final View v = inflater.inflate(R.layout.topic, null);
                            final ImageView statusIco = (ImageView) v.findViewById(R.id.statusIco);
                            TextView topicName = (TextView) v.findViewById(R.id.topicName);
                            ImageView meterial = (ImageView) v.findViewById(R.id.meterial);
                            Spinner action = (Spinner) v.findViewById(R.id.action);
                            final TextInputLayout reasonLayout = (TextInputLayout) v.findViewById(R.id.reasonLayout);
                            EditText reason = (EditText) v.findViewById(R.id.reason);
                            topicName.setText(NAME);
                            meterial.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse(SOURCE));
                                    startActivity(i);
                                }
                            });
                            CustomSpinner(action, R.array.action);
                            action.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (position == 1) {
                                        statusIco.setImageResource(R.drawable.check_icon);
                                        reasonLayout.setVisibility(View.GONE);
                                    } else if (position == 2) {
                                        statusIco.setImageResource(R.drawable.bullet);
                                        reasonLayout.setVisibility(View.VISIBLE);
                                    } else {
                                        statusIco.setImageResource(R.drawable.bullet);
                                        reasonLayout.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            actionList.add(action);
                            subjectLayout.addView(v);
                            //#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#
                        }
                    }
                }}

                    calendar.setText(startDate+" - "+endDate);
                    alottedSubject.setText(allottedSubject);
                    commentToApprover.setText(comment);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else {

            }

        }
    }
//@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%
   public boolean isValid(){
        if(!status.equals("1")){//State 1 validation check
        if (etCheckInDate.length()==0){
            editTextFocus(etCheckInDate);
            SnackbarRed(R.id.layoutNivritti,"Please choose Check In Date");
            return false;
        }else if (etCheckInTime.length()==0){
            editTextFocus(etCheckInTime);
            SnackbarRed(R.id.layoutNivritti,"Please choose Check In Time");
            return false;
        }else if (etCheckOutDate.length()==0){
            editTextFocus(etCheckOutDate);
            SnackbarRed(R.id.layoutNivritti,"Please choose Check Out Date");
            return false;
        }else if (etCheckOutTime.length()==0){
            editTextFocus(etCheckOutTime);
            SnackbarRed(R.id.layoutNivritti,"Please choose Check Out Time");
            return false;
        }
        }else if(status.equals("2")){//State 2 validation check
            for (Spinner spinner:actionList){
                if (spinner.getSelectedItemPosition()==0){
                    Toast.makeText(Nivritti.this,"Please select action.",Toast.LENGTH_LONG).show();
                    return false;
                }
            }
            if (commentForHod.length()==0){
                editTextFocus(commentForHod);
                commentForHod.setError("Please fill this field");
                return false;
            }
        }
        return true;
    }
//@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%

    public class SubmitData extends AsyncTask<String,String,Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                JSONObject reqObj = new JSONObject();
                reqObj.put("USER_ID", app.getUserId());
                if (!status.equals("1")) {
                    reqObj.put("CHECKIN_DATE", etCheckInDate.getText().toString().trim());
                    reqObj.put("CHECKOUT_DATE", etCheckOutDate.getText().toString().trim());
                    reqObj.put("CHECKIN_TIME", etCheckInTime.getText().toString().trim());
                    reqObj.put("CHECKOUT_TIME", etCheckOutTime.getText().toString().trim());
                } else if (status.equals("2")) {
                    reqObj.put("COMMENT_FOR_HOD", commentForHod.getText().toString().trim());
                }
                Log.d("!!!Request", reqObj.toString());
                //String response = HttpClient.SendHttpPost(Constant.VOLUNTEER_EVENT_CHECKINOUT_UPDATE+EVENT_ID, reqObj.toString());
                String response = HttpPutHandler.SendHttpPut(Constant.VOLUNTEER_EVENT_CHECKINOUT_UPDATE + EVENT_ID, reqObj.toString());
                //String response = AssetJSONFile("submit.json",Nivritti.this);
                Log.d("!!!Response", response.toString( ));
                jsonResponse = new JSONObject(response);
                if (jsonResponse.getString("status").equals("true")) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                CustomToast("Submit Successful!");
            } else {
                CustomToast("Submit Failed!");
            }
        }
    }}