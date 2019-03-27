package in.jivanmuktas.www.marg.activity;

import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.app.TimePickerDialog;
import android.content.Context;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.widget.ArrayAdapter;

import com.google.gson.JsonObject;

import in.jivanmuktas.www.marg.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import in.jivanmuktas.www.marg.constant.Constant;
import in.jivanmuktas.www.marg.model.Title;
import in.jivanmuktas.www.marg.model.TopicCompletionStatus;
import in.jivanmuktas.www.marg.network.HttpClient;
import in.jivanmuktas.www.marg.network.HttpGetHandler;
import in.jivanmuktas.www.marg.network.HttpPutHandler;
import in.jivanmuktas.www.marg.singleton.VolleySingleton;

public class Nivritti extends BaseActivity {
    private SimpleDateFormat dateFormatter;
    private DatePickerDialog datePickerDialog;
    String EVENT_ID, status = "", Status, SUBJECT_ID, TOPIC_ID;
    TextView calendar, alottedSubject, commentToApprover, tvCkInTime, tvCkOutTime, alloted_subject, comment_toApprover;
    EditText etCheckInDate, etCheckOutDate, etCheckInTime, etCheckOutTime, commentForHod;
    LinearLayout timeView, subjectLayout, dateTimeView, topicView, layoutNivritti, subjectStatusLayout;
    TextInputLayout commentForHodLayout;
    ImageView tvcheckinTime_image, tvcheckoutTime_image;
    Button submit, update;
    JSONObject jsonResponse;
    String checkin_date = "", checkout_date = "", checkin_time = "", checkout_time = "";
    ArrayList<Spinner> actionList = new ArrayList<Spinner>();
    final ArrayList<TopicCompletionStatus> statuses = new ArrayList<>();
    String stat = "";
    DownloadManager downloadManager;
    String START_DATE,END_DATE,MESSAGE,NOTES;

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
            Status = getIntent().getExtras().getString("STATUS");
            START_DATE = getIntent().getExtras().getString("START_DATE");
            END_DATE = getIntent().getExtras().getString("END_DATE");
            Log.i("!!! status", Status.toString());
        } catch (Exception e) {

        }

        calendar = (TextView) findViewById(R.id.calendar);
        timeView = (LinearLayout) findViewById(R.id.timeView);
        tvCkInTime = (TextView) findViewById(R.id.tvCkInTime);
        tvCkOutTime = (TextView) findViewById(R.id.tvCkOutTime);
        alottedSubject = (TextView) findViewById(R.id.alottedSubject);
        alottedSubject.setVisibility(View.GONE);
        alloted_subject = (TextView) findViewById(R.id.alotted_Subject);
        alloted_subject.setVisibility(View.GONE);
        commentToApprover = (TextView) findViewById(R.id.tvcommentToApprover);
        commentToApprover.setVisibility(View.GONE);
        comment_toApprover = findViewById(R.id.tvcomment_ToApprover);
        comment_toApprover.setVisibility(View.GONE);
        dateTimeView = (LinearLayout) findViewById(R.id.dateTimeView);
        etCheckInDate = (EditText) findViewById(R.id.etCheckInDate);
        etCheckInDate.setVisibility(View.GONE);
        etCheckOutDate = (EditText) findViewById(R.id.etCheckOutDate);
        etCheckOutDate.setVisibility(View.GONE);
        etCheckInTime = (EditText) findViewById(R.id.etCheckInTime);
        etCheckInTime.setVisibility(View.GONE);
        etCheckOutTime = (EditText) findViewById(R.id.etCheckOutTime);
        etCheckOutTime.setVisibility(View.GONE);
        topicView = (LinearLayout) findViewById(R.id.topicView);
        subjectLayout = (LinearLayout) findViewById(R.id.subjectLayout);
        subjectLayout.setVisibility(View.GONE);
        subjectStatusLayout = (LinearLayout) findViewById(R.id.subjectStatusLayout);
        subjectStatusLayout.setVisibility(View.GONE);
        commentForHod = (EditText) findViewById(R.id.commentForHod);
        commentForHodLayout = (TextInputLayout) findViewById(R.id.commentForHodLayout);
        submit = (Button) findViewById(R.id.submit);
        submit.setVisibility(View.GONE);
        update = (Button) findViewById(R.id.update);
        update.setVisibility(View.GONE);
        layoutNivritti = (LinearLayout) findViewById(R.id.layoutNivritti);
        timeView.setVisibility(View.GONE);
        commentForHodLayout.setVisibility(View.GONE);
        topicView.setVisibility(View.GONE);
        tvcheckinTime_image = (ImageView) findViewById(R.id.tvCkInTime_image);
//        tvcheckoutTime_image.setVisibility(View.GONE);
        tvcheckoutTime_image = (ImageView) findViewById(R.id.tvCkOutTime_image);
//        tvcheckoutTime_image.setVisibility(View.GONE);


        if (isNetworkAvailable()) {
            //    new GetAllData().execute();
            SubjectAlloted();
            SubjectFetched();
            //    TopicCompletionStatus();
        } else {
            finish();
        }
        RegisteredEventData();
        Log.d("!!! find ", checkin_time.toString());

        switch (Status) {
            case "18":
                calendar.setText(START_DATE+" - "+END_DATE);
                alottedSubject.setVisibility(View.VISIBLE);
                alloted_subject.setVisibility(View.VISIBLE);
                commentToApprover.setVisibility(View.VISIBLE);
                comment_toApprover.setVisibility(View.VISIBLE);
                etCheckInDate.setVisibility(View.VISIBLE);
                etCheckOutDate.setVisibility(View.VISIBLE);
                etCheckInTime.setVisibility(View.VISIBLE);
                etCheckOutTime.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);
                tvcheckoutTime_image.setVisibility(View.GONE);
                tvcheckinTime_image.setVisibility(View.GONE);
                tvCkInTime.setVisibility(View.GONE);
                tvCkOutTime.setVisibility(View.GONE);


                break;
            case "26":
                alloted_subject.setVisibility(View.GONE);
                alottedSubject.setVisibility(View.GONE);
                commentToApprover.setVisibility(View.GONE);
                comment_toApprover.setVisibility(View.VISIBLE);
                etCheckInDate.setVisibility(View.GONE);
                etCheckOutDate.setVisibility(View.GONE);
                etCheckInTime.setVisibility(View.GONE);
                etCheckOutTime.setVisibility(View.GONE);
                submit.setVisibility(View.GONE);
                timeView.setVisibility(View.VISIBLE);


                //    tvCkInTime.setText(checkin_date);
                //    tvCkOutTime.setText(checkout_time);

                //        tvcheckoutTime_image.setVisibility(View.VISIBLE);
                //        tvcheckinTime_image.setVisibility(View.VISIBLE);
                //        tvCkInTime.setVisibility(View.VISIBLE);
                //        tvCkOutTime.setVisibility(View.VISIBLE);

                break;
            case "29":
                timeView.setVisibility(View.VISIBLE);
                alottedSubject.setVisibility(View.VISIBLE);
                alloted_subject.setVisibility(View.VISIBLE);
                commentToApprover.setVisibility(View.VISIBLE);
                comment_toApprover.setVisibility(View.VISIBLE);
                topicView.setVisibility(View.VISIBLE);
                commentForHodLayout.setVisibility(View.VISIBLE);
                update.setVisibility(View.VISIBLE);
                submit.setVisibility(View.GONE);
                subjectLayout.setVisibility(View.VISIBLE);
                subjectStatusLayout.setVisibility(View.GONE);
                break;
            case "30":
                timeView.setVisibility(View.VISIBLE);
                alottedSubject.setVisibility(View.VISIBLE);
                alloted_subject.setVisibility(View.VISIBLE);
                commentToApprover.setVisibility(View.VISIBLE);
                comment_toApprover.setVisibility(View.VISIBLE);
                topicView.setVisibility(View.VISIBLE);
                subjectStatusLayout.setVisibility(View.VISIBLE);

            default:

        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNetworkAvailable()) {
                    if (isValid()) {
                        //        new SubmitData().execute();
                        SubmitData();
                    }
                }
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateData();
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
                        } else if (selectedHour == 12) {
                            format = "PM";
                        } else if (selectedHour > 12) {
                            selectedHour -= 12;
                            format = "PM";
                        } else {
                            format = "AM";
                        }
                        etCheckInTime.setText(selectedHour + ":" + selectedMinute + " " + format);
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
                        } else if (selectedHour == 12) {
                            format = "PM";
                        } else if (selectedHour > 12) {
                            selectedHour -= 12;
                            format = "PM";
                        } else {
                            format = "AM";
                        }
                        etCheckOutTime.setText(selectedHour + ":" + selectedMinute + " " + format);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

    }
    //***#***#***#***#***#***#***#***#***#***#***#***#***#***#***#***#***#***#

    public boolean isValid() {
            if (etCheckInDate.length() == 0) {
                editTextFocus(etCheckInDate);
                SnackbarRed(R.id.layoutNivritti, "Please choose Check In Date");
                return false;
            } else if (etCheckInTime.length() == 0) {
                editTextFocus(etCheckInTime);
                SnackbarRed(R.id.layoutNivritti, "Please choose Check In Time");
                return false;
            } else if (etCheckOutDate.length() == 0) {
                editTextFocus(etCheckOutDate);
                SnackbarRed(R.id.layoutNivritti, "Please choose Check Out Date");
                return false;
            } else if (etCheckOutTime.length() == 0) {
                editTextFocus(etCheckOutTime);
                SnackbarRed(R.id.layoutNivritti, "Please choose Check Out Time");
                return false;
            }
        return true;
    }
    public boolean isValidFinal(){

        if (commentForHod.length() == 0) {
            editTextFocus(commentForHod);
            commentForHod.setError("Please fill this field");
            return false;
        }
        return true;
    }


    public class GetAllData extends AsyncTask<String, String, Boolean> {
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
                response = handler.makeServiceCall(Constant.VOLUNTEER_EVENT_APPROVE +/*"id="+app.getUserId()+"&eventid="+EVENT_ID*/"event_id=" + EVENT_ID);
                //Log.i("!!!Request",Constant.VOLUNTEER_EVENT_APPROVE+"id="+app.getUserId()+"&eventid="+EVENT_ID );
                //response = AssetJSONFile("nivrittieventview.json",Nivritti.this);
                jsonResponse = new JSONObject(response);
                Log.i("!!!Responsegetal", response);
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
            if (aBoolean) {
                try {
                    JSONArray array = jsonResponse.getJSONArray("response");
                    JSONObject object = array.getJSONObject(0);
                    String projectName = object.getString("PROJECT_NAME");
                    String startDate = object.getString("START_DATE");
                    String endDate = object.getString("END_DATE");
                    String allottedSubject = object.getString("ALLOTTED_SUBJECT");
                    String comment = object.getString("COMMENT_TO_APPROVER");
        //            String comment = object.getString("COMMENT");
                    String checkInDate = object.getString("CHECK_IN_DATE");
                    String checkOutDate = object.getString("CHECK_OUT_DATE");
                    String checkInTime = object.getString("CHECK_IN_TIME");
                    String checkOutTime = object.getString("CHECK_OUT_TIME");
                    String commentForHod = object.getString("COMMENT_FOR_HOD");
                    //   status = object.getString("STATUS");//Status for stage 1 or 2
                    //    status = "18";               //manually changed to 26

                    if (status.equals("1")) {        //manually changed to 26
                        tvCkInTime.setText(checkInTime);
                        tvCkOutTime.setText(checkOutTime);
                        timeView.setVisibility(View.VISIBLE);
                        dateTimeView.setVisibility(View.GONE);
                        topicView.setVisibility(View.VISIBLE);
                        JSONObject subject = object.getJSONObject("SUBJECT");

                        if (subject.length() == 0) {
                            TextView tv = new TextView(Nivritti.this);
                            tv.setText("No topic allocated yet");
                            tv.setTextSize(20);
                            subjectLayout.addView(tv);
                            submit.setVisibility(View.GONE);
                        } else {
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
                                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(SOURCE));
                                            startActivity(i);
                                        }
                                    });
                                    //   CustomSpinner(action, R.array.action);
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
                        }
                    }
                //    calendar.setText(START_DATE + " - " + END_DATE);
                    alottedSubject.setText(allottedSubject);
                    commentToApprover.setText(comment);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {

            }
        }
    }

    //@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%
    /*public boolean isValid() {
        if (Status == "18") {//State 1 validation check   //manually changed to 26
            if (etCheckInDate.length() == 0) {
                editTextFocus(etCheckInDate);
                SnackbarRed(R.id.layoutNivritti, "Please choose Check In Date");
                return false;
            } else if (etCheckInTime.length() == 0) {
                editTextFocus(etCheckInTime);
                SnackbarRed(R.id.layoutNivritti, "Please choose Check In Time");
                return false;
            } else if (etCheckOutDate.length() == 0) {
                editTextFocus(etCheckOutDate);
                SnackbarRed(R.id.layoutNivritti, "Please choose Check Out Date");
                return false;
            } else if (etCheckOutTime.length() == 0) {
                editTextFocus(etCheckOutTime);
                SnackbarRed(R.id.layoutNivritti, "Please choose Check Out Time");
                return false;
            }
        } else if (status.equals("2")) {//State 2 validation check
            for (Spinner spinner : actionList) {
                if (spinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(Nivritti.this, "Please select action.", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
            if (commentForHod.length() == 0) {
                editTextFocus(commentForHod);
                commentForHod.setError("Please fill this field");
                return false;
            }
        }
        return true;

    }*/
//@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%

    public void SubmitData() {
        final String url = Constant.VOLUNTEER_EVENT_CHECKINOUT_UPDATE;

        JSONArray jsonArray = new JSONArray();
        JSONObject reqObj = new JSONObject();
        try {
            reqObj.put("USER_ID", app.getUserId());
            reqObj.put("STATUS", "26");
            reqObj.put("CHECKIN_DATE", etCheckInDate.getText().toString().trim());
            reqObj.put("CHECKOUT_DATE", etCheckOutDate.getText().toString().trim());
            reqObj.put("CHECKIN_TIME", etCheckInTime.getText().toString().trim());
            reqObj.put("CHECKOUT_TIME", etCheckOutTime.getText().toString().trim());
            reqObj.put("EVENT_REG_ID", getIntent().getExtras().getString("EVENT_ID"));
            reqObj.put("MESSAGE", "80");
            jsonArray.put(reqObj);
            final String requestBody = jsonArray.toString();
            Log.i("!!!req", jsonArray.toString());
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("!!!Response->", response);
                            Toast.makeText(Nivritti.this, "Updated Sucessfully", Toast.LENGTH_SHORT).show();
                            Nivritti.this.finish();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("!!!response", error.toString());

                }
            }) {
                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        Log.i("!!!Request", url + "    " + requestBody.getBytes("utf-8"));
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
            };
            RequestQueue queue = Volley.newRequestQueue(this);
            // add it to the RequestQueue
            queue.add(postRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void RegisteredEventData() {
        String url = Constant.GET_REGISTERED_EVENT_DATA + "?event_id=" + EVENT_ID;
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject object) {
                        try {
                            if (object.getString("status").equals("true")) {
                                Log.d("!!!Responsereg", object.toString());
                                JSONArray jsonArray = object.getJSONArray("response");
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                //    checkin_date = jsonObject.getString("CHECKIN_DATE");
                                tvCkInTime.setText(jsonObject.getString("CHECKIN_DATE") + " , " + jsonObject.getString("CHECKIN_TIME"));
                                tvCkOutTime.setText(jsonObject.getString("CHECKOUT_DATE") + " , " + jsonObject.getString("CHECKOUT_TIME"));
                         //       calendar.setText(jsonObject.getString("CHECKIN_DATE") + " - " + jsonObject.getString("CHECKOUT_DATE"));
                                commentToApprover.setText(jsonObject.getString("COMMENT"));

                                //    checkout_date = jsonObject.getString("CHECKOUT_DATE");
                                //    checkin_time = jsonObject.getString("CHECKIN_TIME");
                                //    checkout_time = jsonObject.getString("CHECKOUT_TIME");
                                jsonArray.put(jsonObject);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        VolleySingleton.getInstance(this).addToRequestQueue(objectRequest);

    }

    public void UpdateData() {
        final String url = Constant.TOPIC_STATUS_UPDATE;
        Log.d("!!!url", url);
        JSONArray jsonArray = new JSONArray();
        JSONObject reqObj = new JSONObject();
        try {
            reqObj.put("TOPIC_ID", TOPIC_ID);
            reqObj.put("STATUS", "30");
            reqObj.put("TOPIC_STATUS", stat);
            reqObj.put("EVENT_REG_SYS_ID", EVENT_ID);
            reqObj.put("COMMENT_FOR_HOD", commentForHod.getText().toString().trim());
            jsonArray.put(reqObj);
            final String requestBody = jsonArray.toString();
            Log.i("!!!req", jsonArray.toString());
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("!!!Response->", response);
                            Toast.makeText(Nivritti.this, "Updated Sucessfully", Toast.LENGTH_SHORT).show();
                            Nivritti.this.finish();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("!!!response", error.toString());

                }
            }) {
                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        Log.i("!!!Request", url + "    " + requestBody.getBytes("utf-8"));
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
            };
            RequestQueue queue = Volley.newRequestQueue(this);
            // add it to the RequestQueue
            queue.add(postRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void SubjectAlloted() {
        String url = Constant.GET_CONTENT_DATA + "?event_reg_id=" + EVENT_ID;
        Log.d("!!!urlalloted",url);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject object) {
                        try {
                            if (object.getString("status").equals("true")) ;
                            Log.d("!!!Responseallo", object.toString());
                            JSONArray jsonArray = object.getJSONArray("response");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Log.d("!!! subject", object.toString());
                                JSONObject subject = jsonArray.getJSONObject(i);
                                final String SOURCE = subject.getString("CONTENT_SOURCE");
                                String SUBJECT = subject.getString("SUBJECT");
                                String TOPIC = subject.getString("TOPIC");
                                SUBJECT_ID = subject.getString("SUBJECT_ID");
                                TOPIC_ID = subject.getString("TOPIC_ID");
                                //    jsonArray.put(subject);
                                //=======================================================================================//
                               /* if (subject.length()==0){
                                    TextView tv = new TextView(Nivritti.this);
                                    tv.setText("No topic allocated yet");
                                    tv.setTextSize(20);
                                    subjectLayout.addView(tv);
                                    submit.setVisibility(View.GONE);
                                }*/

                                //#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#

                                alottedSubject.setText(SUBJECT + " ");

                                LayoutInflater inflater = getLayoutInflater();
                                final View v = inflater.inflate(R.layout.topic, null);
                                final ImageView statusIco = (ImageView) v.findViewById(R.id.statusIco);
                                TextView topicName = (TextView) v.findViewById(R.id.topicName);
                                ImageView meterial = (ImageView) v.findViewById(R.id.meterial);
                                Spinner action = (Spinner) v.findViewById(R.id.action);
                                //    Spinner action = (Spinner) v.findViewById(R.id.action);
                                final TextInputLayout reasonLayout = (TextInputLayout) v.findViewById(R.id.reasonLayout);
                                reasonLayout.setVisibility(View.GONE);
                                EditText reason = (EditText) v.findViewById(R.id.reason);
                                topicName.setText(SUBJECT);
                                meterial.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        /*Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(SOURCE));
                                        startActivity(i);*/
                                        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                                        Uri uri = Uri.parse(SOURCE);     //url to be put here
                                        DownloadManager.Request request = new DownloadManager.Request(uri);
                                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                        Long referece = downloadManager.enqueue(request);
                                    }
                                });
                                CustomSpinner(action, R.array.action);
                                // CustomSpin(action,statuses);
                                /*TopicCompletionStatus();
                                ArrayAdapter<TopicCompletionStatus> completStatus =new ArrayAdapter<TopicCompletionStatus>(Nivritti.this, R.layout.spinner_dropdown_item, statuses);
                                completStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                action.setAdapter(completStatus);*/
                                action.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        if (position == 1) {
                                            //    Toast.makeText(Nivritti.this, "position 1", Toast.LENGTH_SHORT).show();
                                            stat = "97";
                                            statusIco.setImageResource(R.drawable.check_icon);
                                            reasonLayout.setVisibility(View.GONE);
                                        } else if (position == 2) {
                                            stat = "98";
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
                                //    statuses.add();
                                subjectLayout.addView(v);
                                //#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        VolleySingleton.getInstance(this).addToRequestQueue(objectRequest);
    }

    public void SubjectFetched() {

        String url = Constant.GET_CONTENT_DATA + "?event_reg_id=" + EVENT_ID;
        Log.d("!!!urlFetched",url);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject object) {
                        try {
                            if (object.getString("status").equals("true")) ;
                            Log.d("!!!Responsefet", object.toString());
                            JSONArray jsonArray = object.getJSONArray("response");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Log.d("!!! subject", object.toString());
                                JSONObject subject = jsonArray.getJSONObject(i);
                                final String SOURCE = subject.getString("CONTENT_SOURCE");
                                String SUBJECT = subject.getString("SUBJECT");
                                String TOPIC = subject.getString("TOPIC");
                                SUBJECT_ID = subject.getString("SUBJECT_ID");
                                TOPIC_ID = subject.getString("TOPIC_ID");
                                //    jsonArray.put(subject);
                                //=======================================================================================//
                               /* if (subject.length()==0){
                                    TextView tv = new TextView(Nivritti.this);
                                    tv.setText("No topic allocated yet");
                                    tv.setTextSize(20);
                                    subjectLayout.addView(tv);
                                    submit.setVisibility(View.GONE);
                                }*/

                                //#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#

                                alottedSubject.setText(SUBJECT + " ");

                                LayoutInflater inflater = getLayoutInflater();
                                final View v = inflater.inflate(R.layout.topic_status, null);
                                TextView subjects = v.findViewById(R.id.topic_Name);
                                TextView status = v.findViewById(R.id.status);
                                subjects.setText(SUBJECT);
                                status.setText(TOPIC);

                                subjectStatusLayout.addView(v);
                                //#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        VolleySingleton.getInstance(this).addToRequestQueue(objectRequest);
    }

    public void TopicCompletionStatus() {
        String url = Constant.TOPIC_COMPLETION_STATUS;
        Log.d("url", url);
        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject object = response;
                            if (object.getString("status").equals("true")) {
                                JSONArray jsonArray = object.getJSONArray("response");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Log.d("!!!!CompletionStatus", response.toString());
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    TopicCompletionStatus completionStatus = new TopicCompletionStatus();
                                    completionStatus.setStatus_id(jsonObject.getString("LOV_ID"));
                                    completionStatus.setStatus_name(jsonObject.getString("LOV_NAME"));
                                    statuses.add(completionStatus);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        /*ArrayAdapter completStatus =new ArrayAdapter(Nivritti.this, R.layout.spinner_dropdown_item, statuses);
                        completStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        action.setAdapter(completStatus);*/
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );
        VolleySingleton.getInstance(this).addToRequestQueue(getRequest);
    }
}


   /* public class SubmitData extends AsyncTask<String,String,Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                JSONArray jsonArray = new JSONArray();
                JSONObject reqObj = new JSONObject();

                if (!status.equals("1"))
                 {
                     reqObj.put("USER_ID", app.getUserId());
                    reqObj.put("STATUS","26");
                    reqObj.put("CHECKIN_DATE", etCheckInDate.getText().toString().trim());
                    reqObj.put("CHECKOUT_DATE", etCheckOutDate.getText().toString().trim());
                    reqObj.put("CHECKIN_TIME", etCheckInTime.getText().toString().trim());
                    reqObj.put("CHECKOUT_TIME", etCheckOutTime.getText().toString().trim());
                     reqObj.put("MESSAGE","80");
                 //   reqObj.put("EVENT_ID","8");
                 //   reqObj.put("MESSAGE","80");
                    jsonArray.put(reqObj);
                } else if (status.equals("2")) {
                    reqObj.put("COMMENT_FOR_HOD", commentForHod.getText().toString().trim());
                }
                Log.d("!!!Request", reqObj.toString());
                String response = HttpClient.SendHttpPost(Constant.VOLUNTEER_EVENT_CHECKINOUT_UPDATE, reqObj.toString());
            //    String response = HttpPutHandler.SendHttpPut(Constant.VOLUNTEER_EVENT_CHECKINOUT_UPDATE , reqObj.toString());
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
    }*/