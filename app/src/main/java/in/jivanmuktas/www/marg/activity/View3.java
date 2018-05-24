package in.jivanmuktas.www.marg.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import in.jivanmuktas.www.marg.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class View3 extends AppCompatActivity {
    private SimpleDateFormat dateFormatter;
    private DatePickerDialog datePickerDialog;
    Spinner topic1,topic2;
    List<String> topicComNotCom = new ArrayList<String>();
    TextInputLayout topic1reason,topic2reason;
    ImageView img1,img2;
    TextView calendar3;
    LinearLayout choosecal3;
    EditText startDate3,endDate3;
    EditText starttime2,endtime2;
    TextView timeS2,timeE2;
    TextInputLayout intim2,outtim2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view3);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTopView3);
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

        calendar3 = (TextView) findViewById(R.id.calendar3);
        choosecal3 = (LinearLayout) findViewById(R.id.choosecal3);

        startDate3 =(EditText) findViewById(R.id.startDate3);
        endDate3 =(EditText) findViewById(R.id.endDate3);
        startDate3.setFocusable(false);
        endDate3.setFocusable(false);

        starttime2 =(EditText) findViewById(R.id.starttime2);
        endtime2 =(EditText) findViewById(R.id.endtime2);
        starttime2.setFocusable(false);
        endtime2.setFocusable(false);

        timeS2 = (TextView) findViewById(R.id.timeS2);
        timeE2 = (TextView) findViewById(R.id.timeE2);
        intim2 = (TextInputLayout) findViewById(R.id.intim2);
        outtim2 = (TextInputLayout) findViewById(R.id.outtim2);

        try {
            if(getIntent().getExtras().getString("KEY").equals("MODIFY")){
                calendar3.setVisibility(View.GONE);
                choosecal3.setVisibility(View.VISIBLE);
                intim2.setVisibility(View.VISIBLE);
                outtim2.setVisibility(View.VISIBLE);
                timeS2.setVisibility(View.GONE);
                timeE2.setVisibility(View.GONE);
            }else{
                calendar3.setVisibility(View.VISIBLE);
                choosecal3.setVisibility(View.GONE);
                intim2.setVisibility(View.GONE);
                outtim2.setVisibility(View.GONE);
                timeS2.setVisibility(View.VISIBLE);
                timeE2.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {

        }

        startDate3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                datePickerDialog = new DatePickerDialog(View3.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        startDate3.setText(dateFormatter.format(newDate.getTime()));

                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        endDate3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                datePickerDialog = new DatePickerDialog(View3.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        endDate3.setText(dateFormatter.format(newDate.getTime()));

                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

               // datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
        starttime2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(View3.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        starttime2.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        endtime2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(View3.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        endtime2.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        img1= (ImageView) findViewById(R.id.img1);
        img2= (ImageView) findViewById(R.id.img2);

        topic1 = (Spinner) findViewById(R.id.topic1);
        topic2 = (Spinner) findViewById(R.id.topic2);

        topic1reason = (TextInputLayout) findViewById(R.id.topic1reason);
        topic2reason = (TextInputLayout) findViewById(R.id.topic2reason);

        topicComNotCom.add("Select");
        topicComNotCom.add("Complete");
        topicComNotCom.add("Not Complete");

        ArrayAdapter<String> topic = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, topicComNotCom);
        topic.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        topic1.setAdapter(topic);
        topic2.setAdapter(topic);


        topic1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 1){
                    img1.setImageResource(R.drawable.check_icon);
                    topic1reason.setVisibility(View.GONE);
                }else if(position == 2){
                    img1.setImageResource(R.drawable.bullet);
                    topic1reason.setVisibility(View.VISIBLE);
                }else {
                    img1.setImageResource(R.drawable.bullet);
                    topic1reason.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        topic2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 1){
                    img2.setImageResource(R.drawable.check_icon);
                    topic2reason.setVisibility(View.GONE);
                }else if(position == 2){
                    img2.setImageResource(R.drawable.bullet);
                    topic2reason.setVisibility(View.VISIBLE);
                }else {
                    img2.setImageResource(R.drawable.bullet);
                    topic2reason.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
    public void topiclink3(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://drive.google.com/open?id=1rWzEN1IPfTN5OdVd9DleZmrXgp3DhPcXS25hCGjAUdQ"));
        startActivity(i);
    }

    public void topiclink4(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://drive.google.com/open?id=1Kf7O_HeAvYdNnKRhE4rGSOu-JpIrBSxmQ6gN9BaBEZU"));
        startActivity(i);
    }
    public void Updatetopic(View view) {
        finish();
    }
}
