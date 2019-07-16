package in.jivanmuktas.www.marg.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import in.jivanmuktas.www.marg.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import in.jivanmuktas.www.marg.constant.Constant;
import in.jivanmuktas.www.marg.network.HttpPutHandler;

public class View1 extends BaseActivity {
    private SimpleDateFormat dateFormatter;
    private DatePickerDialog datePickerDialog;
    EditText tvCheckInDate, tvCheckInTime, tvCheckOutDate, tvCheckOutTime;
    TextView calendar1,commentApprover;
    LinearLayout choosecal1;
    EditText startDate1, endDate1;
    String EVENT_ID,START_DATE,END_DATE,NOTES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTopView1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("View Schedule");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        calendar1 = (TextView) findViewById(R.id.calendar1);
        choosecal1 = (LinearLayout) findViewById(R.id.choosecal1);
        commentApprover = (TextView) findViewById(R.id.tvcommentApprover);
        try {
            EVENT_ID = getIntent().getExtras().getString("EVENT_ID");
            START_DATE = getIntent().getExtras().getString("START_DATE");
            END_DATE = getIntent().getExtras().getString("END_DATE");
            NOTES = getIntent().getExtras().getString("NOTES");
            if (getIntent().getExtras().getString("KEY").equals("MODIFY")) {
                calendar1.setText(START_DATE+" - "+END_DATE);
                /*calendar1.setVisibility(View.GONE);
                choosecal1.setVisibility(View.VISIBLE);
            } else {
                calendar1.setVisibility(View.VISIBLE);
                choosecal1.setVisibility(View.GONE);*/
            }
        } catch (Exception e) {

        }
        //commentApprover.setText(NOTES);

        startDate1 = (EditText) findViewById(R.id.startDate1);
        endDate1 = (EditText) findViewById(R.id.endDate1);
        startDate1.setFocusable(false);
        endDate1.setFocusable(false);
        startDate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                datePickerDialog = new DatePickerDialog(View1.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        startDate1.setText(dateFormatter.format(newDate.getTime()));

                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        //        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();

            }
        });

        endDate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                datePickerDialog = new DatePickerDialog(View1.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        endDate1.setText(dateFormatter.format(newDate.getTime()));

                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
         //       datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        tvCheckInDate = (EditText) findViewById(R.id.tvCheckInDate);
        tvCheckInTime = (EditText) findViewById(R.id.tvCheckInTime);
        tvCheckOutDate = (EditText) findViewById(R.id.tvCheckOutDate);
        tvCheckOutTime = (EditText) findViewById(R.id.tvCheckOutTime);

        tvCheckInDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                datePickerDialog = new DatePickerDialog(View1.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        tvCheckInDate.setText(dateFormatter.format(newDate.getTime()));

                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                //    datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                datePickerDialog.show();
            }
        });
        tvCheckOutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                datePickerDialog = new DatePickerDialog(View1.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        tvCheckOutDate.setText(dateFormatter.format(newDate.getTime()));

                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                //    datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                datePickerDialog.show();

            }
        });
        tvCheckInTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(View1.this, new TimePickerDialog.OnTimeSetListener() {
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
                        tvCheckInTime.setText(selectedHour + ":" + selectedMinute + " " + format);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        tvCheckOutTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(View1.this, new TimePickerDialog.OnTimeSetListener() {
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
                        tvCheckOutTime.setText(selectedHour + ":" + selectedMinute + " " + format);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
    }

    public void submitview1(View view) {

        if (isNetworkAvailable()) {
            // new UpdateEvent().execute();
            if(isValid()) {
                SubmitData();
            }
        }
    }

    public boolean isValid() {
        if(startDate1.length() == 0){
            editTextFocus(startDate1);
            SnackbarRed(R.id.layoutView1,"Please Enter Start Date");
            return false;
        } else if(endDate1.length() == 0){
            editTextFocus(endDate1);
            SnackbarRed(R.id.layoutView1, "Please Enter End Date");
            return false;
        } else if(tvCheckInDate.length() == 0){
            editTextFocus(tvCheckInDate);
            SnackbarRed(R.id.layoutView1,"Please Enter Check In Date");
            return false;
        } else if(tvCheckOutDate.length() == 0){
            editTextFocus(tvCheckOutDate);
            SnackbarRed(R.id.layoutView1,"Please Enter Check Out Date");
            return false;
        } else if(tvCheckInTime.length() == 0){
            editTextFocus(tvCheckInTime);
            SnackbarRed(R.id.layoutView1,"Please Enter Check In Time");
            return false;
        } else  if(tvCheckOutTime.length() == 0) {
            editTextFocus(tvCheckOutTime);
            SnackbarRed(R.id.layoutView1,"Please Enter Check Out Time");
        }
        return true;
    }

    public void SubmitData() {
        final String url = Constant.VOLUNTEER_EVENT_CHECKINOUT_UPDATE;
        Log.d("!!!urlmodify", url);
        JSONArray jsonArray = new JSONArray();
        JSONObject reqObj = new JSONObject();
        try {
            reqObj.put("USER_ID", app.getUserId());
            reqObj.put("STATUS", "24");
            reqObj.put("REG_START_DATE",startDate1.getText().toString().trim());
            reqObj.put("REG_END_DATE",endDate1.getText().toString().trim());
            reqObj.put("CHECKIN_DATE", tvCheckInDate.getText().toString().trim());
            reqObj.put("CHECKIN_TIME", tvCheckInTime.getText().toString().trim());
            reqObj.put("CHECKOUT_DATE", tvCheckOutDate.getText().toString().trim());
            reqObj.put("CHECKOUT_TIME", tvCheckOutTime.getText().toString().trim());
            reqObj.put("EVENT_REG_ID", getIntent().getExtras().getString("EVENT_ID"));
            reqObj.put("MESSAGE", "78");
            jsonArray.put(reqObj);
            final String requestBody = jsonArray.toString();
            Log.i("!!!req", jsonArray.toString());
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("!!!Response->", response);
                            Toast.makeText(View1.this, "Updated Sucessfully", Toast.LENGTH_SHORT).show();
                            View1.this.finish();
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

    public class UpdateEvent extends AsyncTask<String, String, Boolean> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDailog();
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            String response;
            JSONObject json;
            JSONObject reqObj = new JSONObject();

            try {
                reqObj.put("CHECKIN_DATE", tvCheckInDate.getText().toString().trim());
                reqObj.put("CHECKIN_TIME", tvCheckInTime.getText().toString().trim());
                reqObj.put("CHECKOUT_DATE", tvCheckOutDate.getText().toString().trim());
                reqObj.put("CHECKOUT_TIME", tvCheckOutTime.getText().toString().trim());
                //    reqObj.put("END_DATE",endDate1.getText().toString().trim());
                reqObj.put("EVENT_ID", EVENT_ID);
                //  reqObj.put("START_DATE",startDate1.getText().toString().trim());
                reqObj.put("USER_ID", app.getUserId());
                //    response = HttpPutHandler.SendHttpPut(Constant.EVENT_MODIFICATION,reqObj.toString());//Using Put Method
                response = HttpPutHandler.SendHttpPut("", "");

                json = new JSONObject(response);
                Log.i("!!!ResApprovedCancel", response);
                if (json.getBoolean("status")) {
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
            } else {
            }
        }
    }
}
