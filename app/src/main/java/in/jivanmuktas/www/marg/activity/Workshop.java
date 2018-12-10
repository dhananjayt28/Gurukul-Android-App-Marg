package in.jivanmuktas.www.marg.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import in.jivanmuktas.www.marg.R;
import in.jivanmuktas.www.marg.constant.Constant;
import in.jivanmuktas.www.marg.network.HttpGetHandler;

public class Workshop extends BaseActivity {
    String EVENT_ID,status,Status;
    private SimpleDateFormat dateFormatter;
    private DatePickerDialog datePickerDialog;
    TextView tvCalendar,tvalottedDistrict,tvorigin, tvend, tvairport, tvrailway, tvcommentToApprover,tvCheckin,tvCheckOut;
    EditText arrivalDate,arrivalTime,departureDate,departureTime;
    LinearLayout edDateTimeView,tvDateTimeView,itienaryView;
    Spinner itienaryAction;
    Button submit;
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
                SubmitData();
            }
        });
    }
    //**************#******************#*****************#*******************#******************#
    /*public boolean isValid(){
        if (){

        }else if (){

        }
        return true;
    }*/
    //**************#******************#*****************#*******************#******************#

    //***#***#***#***#***#***#***#***#***#***#***#***#***#***#***#***#***#***#

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
                response = AssetJSONFile("workshopeventview.json",Workshop.this);
                jsonResponse = new JSONObject(response);
                Log.i("!!!Response", response);
                if (jsonResponse.getBoolean("status")) {
                    return true;
                } else
                    return false;
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
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
                    String allottedDistrict = object.getString("ALLOTTED_DISTRICT");
                    String origin = object.getString("ORIGIN");
                    String end = object.getString("END");
                    String airport = object.getString("AIRPORT");
                    String railway_station = object.getString("RAILWAY_STATION");
                    String comment = object.getString("COMMENT_TO_APPROVER");
                    String arrivalDate = object.getString("ARRIVAL_DATE");
                    String departureDate = object.getString("DEPARTURE_DATE");
                    String arrivalTime = object.getString("ARRIVAL_TIME");
                    String departureTime = object.getString("DEPARTURE_TIME");

                    status = object.getString("STATUS");//Status for stage 1 or 2

                    tvCalendar.setText(startDate+" - "+endDate);
                    tvalottedDistrict.setText(allottedDistrict);
                    tvorigin.setText(origin);
                    tvend.setText(end);
                    tvairport.setText(airport);
                    tvrailway.setText(railway_station);
                    tvcommentToApprover.setText(comment);

                    if (status.equals("2")) {
                        edDateTimeView.setVisibility(View.GONE);
                        tvDateTimeView.setVisibility(View.VISIBLE);
                        itienaryView.setVisibility(View.VISIBLE);
                        tvCheckin.setText(arrivalDate+", "+arrivalTime);
                        tvCheckOut.setText(departureDate+", "+departureTime);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else {

            }

        }
    }
    //@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%@@@%%%
    public void SubmitData(){
        final String url = Constant.VOLUNTEER_EVENT_CHECKINOUT_UPDATE;

        JSONArray jsonArray =  new JSONArray();
        JSONObject reqObj = new JSONObject();
        try {
            reqObj.put("USER_ID", app.getUserId());
            reqObj.put("STATUS","26");
            reqObj.put("CHECKIN_DATE", arrivalDate.getText().toString().trim());
            reqObj.put("CHECKOUT_DATE", arrivalTime.getText().toString().trim());
            reqObj.put("CHECKIN_TIME", departureDate.getText().toString().trim());
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
                    reqObj.put("ARRIVAL_DATE", arrivalDate.getText().toString().trim());
                    reqObj.put("ARRIVAL_TIME", arrivalTime.getText().toString().trim());
                    reqObj.put("DEPARTURE_DATE", departureDate.getText().toString().trim());
                    reqObj.put("DEPARTURE_TIME", departureTime.getText().toString().trim());
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
}
