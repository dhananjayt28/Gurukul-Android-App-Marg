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

import in.jivanmuktas.www.marg.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import in.jivanmuktas.www.marg.constant.Constant;
import in.jivanmuktas.www.marg.network.HttpPutHandler;

public class View1 extends BaseActivity {
    private SimpleDateFormat dateFormatter;
    private DatePickerDialog datePickerDialog;
    EditText tvCheckInDate,tvCheckInTime,tvCheckOutDate,tvCheckOutTime;
    TextView calendar1;
    LinearLayout choosecal1;
    EditText startDate1,endDate1;
    String EVENT_ID;
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
        try {
            EVENT_ID = getIntent().getExtras().getString("EVENT_ID");
            if(getIntent().getExtras().getString("KEY").equals("MODIFY")){
                calendar1.setVisibility(View.GONE);
                choosecal1.setVisibility(View.VISIBLE);
            }else{
                calendar1.setVisibility(View.VISIBLE);
                choosecal1.setVisibility(View.GONE);
            }
        } catch (Exception e) {

        }

        startDate1 =(EditText) findViewById(R.id.startDate1);
        endDate1 =(EditText) findViewById(R.id.endDate1);
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
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
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
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
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

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

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

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

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
                        tvCheckInTime.setText( selectedHour + ":" + selectedMinute+" "+format);
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
                        tvCheckOutTime.setText( selectedHour + ":" + selectedMinute+" "+format);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
    }
    public void submitview1(View view) {

        if(isNetworkAvailable()){
            new UpdateEvent().execute();
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
                reqObj.put("CHECKIN_DATE",tvCheckInDate.getText().toString().trim());
                reqObj.put("CHECKIN_TIME",tvCheckInTime.getText().toString().trim());
                reqObj.put("CHECKOUT_DATE",tvCheckOutDate.getText().toString().trim());
                reqObj.put("CHECKOUT_TIME",tvCheckOutTime.getText().toString().trim());
                reqObj.put("END_DATE",endDate1.getText().toString().trim());
                reqObj.put("EVENT_ID",EVENT_ID);
                reqObj.put("START_DATE",startDate1.getText().toString().trim());
                reqObj.put("USER_ID",app.getUserId());
            //    response = HttpPutHandler.SendHttpPut(Constant.EVENT_MODIFICATION,reqObj.toString());//Using Put Method
                response = HttpPutHandler.SendHttpPut("","");

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

            if (aBoolean){

            }else {

            }

        }

    }
}
