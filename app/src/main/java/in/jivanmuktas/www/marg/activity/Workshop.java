package in.jivanmuktas.www.marg.activity;

import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import in.jivanmuktas.www.marg.R;
import in.jivanmuktas.www.marg.constant.Constant;
import in.jivanmuktas.www.marg.model.TopicCompletionStatus;
import in.jivanmuktas.www.marg.network.HttpGetHandler;
import in.jivanmuktas.www.marg.singleton.VolleySingleton;


public class Workshop extends BaseActivity {
    String EVENT_ID,status,Status,ORIGIN_PLACE,DESTINATION_PALACE,TRANSPORT_MODE_ORIGIN,TRANSPORT_MODE_END;
    private SimpleDateFormat dateFormatter;
    private DatePickerDialog datePickerDialog;
    TextView tvCalendar,tvalottedDistrict,tvorigin, tvend, tvairport, tvrailway, tvcommentToApprover,tvCheckin,tvCheckOut;
    EditText arrivalDate,arrivalTime,departureDate,departureTime;
    LinearLayout edDateTimeView,tvDateTimeView,itienaryView;
    Spinner itienaryAction;
    Button submit,Update,UpdateAgain;
    ImageView ImgItie,ItenaryUpload;
    TextInputLayout Moditext;
    DownloadManager downloadManager;
    String stat = "";
     ArrayList<TopicCompletionStatus> statuses = new ArrayList<>();
     ImageView originModeAir,originModeRail,destModeAir,destModeRail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshop);

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
            ORIGIN_PLACE = getIntent().getExtras().getString("ORIGIN_PLACE");
            DESTINATION_PALACE = getIntent().getExtras().getString("DESTINATION_PALACE");
            TRANSPORT_MODE_ORIGIN = getIntent().getExtras().getString("TRANSPORT_MODE_ORIGIN");
            TRANSPORT_MODE_END = getIntent().getExtras().getString("TRANSPORT_MODE_END");

            Log.d("!!!status",Status.toString());

        } catch (Exception e) {

        }
        tvCalendar = (TextView) findViewById(R.id.tvCalendar);
        tvalottedDistrict = (TextView) findViewById(R.id.alottedDistrict);
        tvorigin = (TextView) findViewById(R.id.tvorigin);
        tvend = (TextView) findViewById(R.id.tvend);
        tvairport = (TextView) findViewById(R.id.tvairport);
        tvrailway = (TextView) findViewById(R.id.tvrailway);
        tvcommentToApprover = (TextView) findViewById(R.id.tvcommentToApprover);
        tvCheckin = (TextView) findViewById(R.id.tvCheckin);
        tvCheckOut = (TextView) findViewById(R.id.tvCheckOut);
        arrivalDate = (EditText) findViewById(R.id.arrivalDate);
        arrivalTime = (EditText) findViewById(R.id.arrivalTime);
        departureDate = (EditText) findViewById(R.id.departureDate);
        departureTime = (EditText) findViewById(R.id.departureTime);
        edDateTimeView = (LinearLayout) findViewById(R.id.edDateTimeView);
        itienaryAction = (Spinner) findViewById(R.id.itienaryAction);
        tvDateTimeView = (LinearLayout) findViewById(R.id.tvDateTimeView);
        itienaryView = (LinearLayout) findViewById(R.id.itienaryView);
        tvDateTimeView.setVisibility(View.GONE);
        itienaryView.setVisibility(View.GONE);
        submit = (Button) findViewById(R.id.submit);
        submit.setVisibility(View.GONE);
        ImgItie = (ImageView) findViewById(R.id.imgItie);
        ItenaryUpload = (ImageView) findViewById(R.id.ItenaryUpload);
        Moditext = (TextInputLayout) findViewById(R.id.modiReasonItie);
        Moditext.setVisibility(View.GONE);
        Update = (Button) findViewById(R.id.Update);
        Update.setVisibility(View.GONE);
        UpdateAgain = findViewById(R.id.UpdateAgain);
        UpdateAgain.setVisibility(View.GONE);
        originModeAir = findViewById(R.id.aero_image1);
        originModeAir.setVisibility(View.GONE);
        originModeRail = findViewById(R.id.rail_image1);
        originModeRail.setVisibility(View.GONE);
        destModeAir = findViewById(R.id.aero_image2);
        destModeAir.setVisibility(View.GONE);
        destModeRail = findViewById(R.id.rail_image2);
        destModeRail.setVisibility(View.GONE);



        //CustomSpinner(itienaryAction,R.array.itiespinner);

        TopicCompletionStatus();
        itienaryAction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stat = statuses.get(position).getStatus_id();
                if(position == 1){
                //    stat = "97";
                    ImgItie.setImageResource(R.drawable.check_icon);
                    Moditext.setVisibility(View.GONE);
                }else if(position == 2){
                //    stat = "98";
                    ImgItie.setImageResource(R.drawable.bullet);
                    Moditext.setVisibility(View.VISIBLE);
                }else {
                    ImgItie.setImageResource(R.drawable.bullet);
                    Moditext.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if(Status.equals("18")  ){
            submit.setVisibility(View.VISIBLE);
            Update.setVisibility(View.GONE);
            UpdateAgain.setVisibility(View.GONE);

        }
        if( Status.equals("26")){
            submit.setVisibility(View.GONE);
            Update.setVisibility(View.GONE);
            UpdateAgain.setVisibility(View.GONE);
        }

        if (Status.equals("24")) {
            edDateTimeView.setVisibility(View.GONE);
            tvDateTimeView.setVisibility(View.VISIBLE);
            itienaryView.setVisibility(View.VISIBLE);
            Update.setVisibility(View.VISIBLE);
            submit.setVisibility(View.GONE);
            UpdateAgain.setVisibility(View.GONE);
            tvCheckin.setText(arrivalDate+", "+arrivalTime);
            tvCheckOut.setText(departureDate+", "+departureTime);
        }
        if(Status.equals("114")){
            edDateTimeView.setVisibility(View.GONE);
            tvDateTimeView.setVisibility(View.VISIBLE);
            itienaryView.setVisibility(View.GONE);
            Update.setVisibility(View.GONE);
            submit.setVisibility(View.GONE);
            UpdateAgain.setVisibility(View.VISIBLE);
            tvCheckin.setText(arrivalDate+", "+arrivalTime);
            tvCheckOut.setText(departureDate+", "+departureTime);
        }
        if(Status.equals("115")){
            edDateTimeView.setVisibility(View.GONE);
            tvDateTimeView.setVisibility(View.VISIBLE);
            itienaryView.setVisibility(View.GONE);
            Update.setVisibility(View.GONE);
            submit.setVisibility(View.GONE);
            UpdateAgain.setVisibility(View.GONE);
            tvCheckin.setText(arrivalDate+", "+arrivalTime);
            tvCheckOut.setText(departureDate+", "+departureTime);
        }




        arrivalDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                datePickerDialog = new DatePickerDialog(Workshop.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        arrivalDate.setText(dateFormatter.format(newDate.getTime()));

                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                datePickerDialog.show();

            }
        });
        departureDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                datePickerDialog = new DatePickerDialog(Workshop.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        departureDate.setText(dateFormatter.format(newDate.getTime()));

                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                datePickerDialog.show();

            }
        });
        arrivalTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Workshop.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        arrivalTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        departureTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Workshop.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        departureTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute,true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        new GetAllData().execute();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    new SubmitData().execute();
                if(isNetworkAvailable()){
                    if(isValid()){
                        SubmitData();
                    }
                } else {
                    Toast.makeText(Workshop.this, "Network Not Available. Please tur on Network Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()) {
                    if(isValiditenary()) {
                        SubmitDataItenary();
                    }
                }
            }
        });
        UpdateAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()) {

                        SubmitDataAgain();

                }
            }
        });
    }
    //**************#******************#*****************#*******************#******************#
    //changes done  on 16th Mar
    public boolean isValid() {
     //   if (Status == "18") {//State 1 validation check   //manually changed to 26
            if (arrivalDate.length() == 0) {
                editTextFocus(arrivalDate);
                SnackbarRed(R.id.layoutWorkshop, "Please choose Arrival Date");
                return false;
            } else if (departureDate.length() == 0) {
                editTextFocus(departureDate);
                SnackbarRed(R.id.layoutWorkshop, "Please choose Departure Time");
                return false;
            } else if (arrivalTime.length() == 0) {
                editTextFocus(arrivalTime);
                SnackbarRed(R.id.layoutWorkshop, "Please choose Arrival Date");
                return false;
            } else if (departureTime.length() == 0) {
                editTextFocus(departureTime);
                SnackbarRed(R.id.layoutWorkshop, "Please choose Departure Time");
                return false;
            }
        return true;
    }
    public boolean isValiditenary(){
        if (itienaryAction.getSelectedItemPosition() == 0) {
        //    Toast.makeText(Workshop.this, "Please select action.", Toast.LENGTH_LONG).show();
            SnackbarRed(R.id.layoutWorkshop, "Please choose Status of download");
            return false;
        }
        return true;
    }
    //**************#******************#*****************#*******************#******************#

    //***#***#***#***#***#***#***#***#***#***#***#***#***#***#***#***#***#***#

    //Getting the data

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
            //    response = handler.makeServiceCall(Constant.VOLUNTEER_EVENT_APPROVE+"id="+app.getUserId()+"&eventid="+EVENT_ID);
                response = handler.makeServiceCall(Constant.GET_ITINERARY_INFORMATION + "event_reg_id="+EVENT_ID+"&user_id="+app.getUserId());
                Log.d("!!!event_id_ite",EVENT_ID);
            //    response = AssetJSONFile("workshopeventview.json",Workshop.this);
                jsonResponse = new JSONObject(response);
                Log.i("!!!Response", response);
                if (jsonResponse.getBoolean("status")) {
                    return true;
                } else
                    return false;
            } catch (JSONException e) {
                e.printStackTrace();
            } /*catch (IOException e) {
                e.printStackTrace();
            }*/
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
                    /*String projectName = object.getString("PROJECT_NAME");
                    String startDate = object.getString("START_DATE");
                    String endDate = object.getString("END_DATE");
                    String allottedDistrict = object.getString("ALLOTTED_DISTRICT");
                    String origin = object.getString("ORIGIN");
                    String end = object.getString("END");
                    String airport = object.getString("AIRPORT");
                    String railway_station = object.getString("RAILWAY_STATION");
                    String comment = object.getString("COMMENT_TO_APPROVER");
                    String arrivalDate = object.getString("ARRIVAL_DATE");
                    String departureDate = object.getString("DEPARTURE_DATE");
                    String arrivalTime = object.getString("ARRIVAL_TIME");
                    String departureTime = object.getString("DEPARTURE_TIME");*/

                //    status = object.getString("STATUS");//Status for stage 1 or 2

                 /*   tvCalendar.setText(startDate+" - "+endDate);
                    tvalottedDistrict.setText(allottedDistrict);
                    tvorigin.setText(origin);
                    tvend.setText(end);
                    tvairport.setText(airport);
                    tvrailway.setText(railway_station);
                    tvcommentToApprover.setText(comment);*/

                    /*if(Status.equals("18") || Status.equals("26")){
                        submit.setVisibility(View.VISIBLE);
                        Update.setVisibility(View.GONE);

                    }

                    if (Status.equals("24")) {
                        edDateTimeView.setVisibility(View.GONE);
                        tvDateTimeView.setVisibility(View.VISIBLE);
                        itienaryView.setVisibility(View.VISIBLE);
                        Update.setVisibility(View.VISIBLE);
                        submit.setVisibility(View.GONE);
                        tvCheckin.setText(arrivalDate+", "+arrivalTime);
                        tvCheckOut.setText(departureDate+", "+departureTime);
                    }*/
                    String EVENT_START_DATE = object.getString("EVENT_START_DATE");
                    String EVENT_END_DATE = object.getString("EVENT_END_DATE");
                    final String ITINERARY_COMMENTS = object.getString("ITINERARY_COMMENTS");
                    String STATE_NAME = object.getString("STATE_NAME");
                    String ORIGIN_LOCATION = object.getString("ORIGIN_LOCATION");
                    String END_LOCATION = object.getString("END_LOCATION");
                    String TRANSPORT_MODE_ORIGIN = object.getString("TRANSPORT_MODE_ORIGIN");
                    String TRANSPORT_MODE_END = object.getString("TRANSPORT_MODE_END");
                    String COMMENT = object.getString("COMMENT");
                    String CHECKIN_DATE = object.getString("CHECKIN_DATE");
                    String CHECKIN_TIME = object.getString("CHECKIN_TIME");
                    String CHECKOUT_DATE = object.getString("CHECKOUT_DATE");
                    String CHECKOUT_TIME = object.getString("CHECKOUT_TIME");
                    final String ITINERARY_FILES = object.getString("ITINERARY_FILES");
                    final String URI = "http://uatappweb.jivanmuktas.org//Uploaded_files/";
                //    final String SOURCE = object.getString("SOURCE");

                    tvCalendar.setText(EVENT_START_DATE+" - "+EVENT_END_DATE);
                    STATE_NAME = STATE_NAME.replaceAll("\\{","");
                    STATE_NAME = STATE_NAME.replaceAll("\\}","");
                    Log.d("!!!mode origin",TRANSPORT_MODE_ORIGIN);
                    Log.d("!!!mode end",TRANSPORT_MODE_END);
                    if(TRANSPORT_MODE_ORIGIN.equals(String.valueOf(1))){
                        originModeRail.setVisibility(View.VISIBLE);
                        originModeAir.setVisibility(View.GONE);
                    } else {
                        originModeAir.setVisibility(View.VISIBLE);
                        originModeRail.setVisibility(View.GONE);
                    }
                    if(TRANSPORT_MODE_END.equals(String.valueOf(1))){
                        destModeRail.setVisibility(View.VISIBLE);
                        destModeAir.setVisibility(View.GONE);
                    } else {
                        destModeAir.setVisibility(View.VISIBLE);
                        destModeRail.setVisibility(View.GONE);
                    }
                    tvairport.setText(ORIGIN_PLACE);
                    tvrailway.setText(DESTINATION_PALACE);
                    tvalottedDistrict.setText(STATE_NAME);
                    Log.d("!!!alloted",STATE_NAME);


                    tvcommentToApprover.setText(COMMENT);
                    tvorigin.setText(ORIGIN_LOCATION);
                    tvend.setText(END_LOCATION);

                    tvCheckin.setText(CHECKIN_DATE +" , "+ CHECKIN_TIME);
                    tvCheckOut.setText(CHECKOUT_DATE +" , " +CHECKOUT_TIME );

                    //Download the data into the Mobile
                    ItenaryUpload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                         /*Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(SOURCE));
                         startActivity(i);*/
                            downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                         //   Uri uri = Uri.parse("http://foersom.com/net/HowTo/data/OoPdfFormExample.pdf");
                            Uri uri = Uri.parse(URI+ITINERARY_FILES);//url to be put here
                            Log.d("!!!uri", ITINERARY_FILES);
                            DownloadManager.Request request = new DownloadManager.Request(uri);
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            Long referece = downloadManager.enqueue(request);
                            Log.d("!!!itinery",ITINERARY_COMMENTS);
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else {

            }

        }
    }
    //@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%
    //First stage submission of the data
    public void SubmitData(){
        final String url = Constant.VOLUNTEER_EVENT_CHECKINOUT_UPDATE;

        JSONArray jsonArray =  new JSONArray();
        JSONObject reqObj = new JSONObject();
        try {
            reqObj.put("USER_ID", app.getUserId());
            reqObj.put("STATUS","26");
            reqObj.put("CHECKIN_DATE", arrivalDate.getText().toString().trim());
            reqObj.put("CHECKIN_TIME", arrivalTime.getText().toString().trim());
            reqObj.put("CHECKOUT_DATE", departureDate.getText().toString().trim());
            reqObj.put("CHECKOUT_TIME", departureTime.getText().toString().trim());
            reqObj.put("EVENT_REG_ID",getIntent().getExtras().getString("EVENT_ID"));
            reqObj.put("MESSAGE","80");
            jsonArray.put(reqObj);
            final String requestBody = jsonArray.toString();
            Log.i("!!!req",jsonArray.toString());
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("!!!Response->", response);
                            Toast.makeText(Workshop.this, "Updated Sucessfully", Toast.LENGTH_SHORT).show();
                            Workshop.this.finish();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("!!!response",error.toString());

                }
            })
            {
                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        Log.i("!!!Request", url+"    "+requestBody.getBytes("utf-8"));
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

    public class SubmitData extends AsyncTask<String,String,Boolean>{
        JSONObject jsonResponse;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            try{
                JSONObject reqObj = new JSONObject();
                reqObj.put("USER_ID",app.getUserId());
                reqObj.put("EVENT_ID",EVENT_ID);
                if (status.equals("1")) {
                    /*reqObj.put("ARRIVAL_DATE", arrivalDate.getText().toString().trim());
                    reqObj.put("ARRIVAL_TIME", arrivalTime.getText().toString().trim());
                    reqObj.put("DEPARTURE_DATE", departureDate.getText().toString().trim());
                    reqObj.put("DEPARTURE_TIME", departureTime.getText().toString().trim());*/
                    reqObj.put("USER_ID", app.getUserId());
                    reqObj.put("STATUS","26");
                    reqObj.put("CHECKIN_DATE", arrivalDate.getText().toString().trim());
                    reqObj.put("CHECKOUT_DATE", arrivalTime.getText().toString().trim());
                    reqObj.put("CHECKIN_TIME", departureDate.getText().toString().trim());
                    reqObj.put("CHECKOUT_TIME", departureTime.getText().toString().trim());
                    reqObj.put("EVENT_REG_ID",getIntent().getExtras().getString("EVENT_ID"));
                    reqObj.put("MESSAGE","80");
                }else if(status.equals("2")){
                    reqObj.put("ITIENARY", itienaryAction);
                }
                Log.d("!!!Request", reqObj.toString());

                //String response = HttpClient.SendHttpPost(Constant., reqObj.toString());
                String response = AssetJSONFile("submit.json",Workshop.this);
                Log.d("!!!Response", response.toString());
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
            if (aBoolean){
                CustomToast("Submit Successful!");
            }else {
                CustomToast("Submit Failed!");
            }
        }
    }

    //Final submission of the data of Itenary ststus
    public void SubmitDataItenary(){
        final String url = Constant.UPDATE_ITENARY_STATUS;
        JSONArray jsonArray =  new JSONArray();
        JSONObject reqObj = new JSONObject();
        try {
            reqObj.put("STATUS","114");
            reqObj.put("EVENT_REG_SYS_ID",getIntent().getExtras().getString("EVENT_ID"));
            reqObj.put("MESSAGE","112");
            reqObj.put("ITINERARY_STATUS",stat);
            reqObj.put("ITINERARY_COMMENTS",Moditext.getEditText().getText().toString());
            jsonArray.put(reqObj);
            final String requestBody = jsonArray.toString();
            Log.i("!!!req",jsonArray.toString());
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("!!!Response->", response);
                            Toast.makeText(Workshop.this, "Updated Sucessfully", Toast.LENGTH_SHORT).show();
                            Workshop.this.finish();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("!!!response",error.toString());
                }
            })
            {
                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        Log.i("!!!Request", url+"    "+requestBody.getBytes("utf-8"));
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
    public void SubmitDataAgain(){
        final String url = Constant.UPDATE_ITENARY_STATUS;
        JSONArray jsonArray =  new JSONArray();
        JSONObject reqObj = new JSONObject();
        try {
            reqObj.put("STATUS","115");
            reqObj.put("EVENT_REG_SYS_ID",getIntent().getExtras().getString("EVENT_ID"));
            reqObj.put("MESSAGE","113");
            jsonArray.put(reqObj);
            final String requestBody = jsonArray.toString();
            Log.i("!!!req",jsonArray.toString());
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("!!!Response->", response);
                            Toast.makeText(Workshop.this, "Updated Sucessfully", Toast.LENGTH_SHORT).show();
                            Workshop.this.finish();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("!!!response",error.toString());
                }
            })
            {
                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        Log.i("!!!Request", url+"    "+requestBody.getBytes("utf-8"));
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

    //Topic Status Completion status for receiving the itenary status
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
                        ArrayAdapter completStatus =new ArrayAdapter(Workshop.this, R.layout.spinner_dropdown_item, statuses);
                        completStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        itienaryAction.setAdapter(completStatus);


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
