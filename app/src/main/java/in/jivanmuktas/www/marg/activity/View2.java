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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import in.jivanmuktas.www.marg.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class View2 extends AppCompatActivity {
    private SimpleDateFormat dateFormatter;
    private DatePickerDialog datePickerDialog;
    TextView calendar2;
    LinearLayout choosecal2;
    EditText startDate2,endDate2;
    EditText starttime1,endtime1;
    TextView timeS1,timeE1;
    TextInputLayout intim1,outtim1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTopView2);
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

        calendar2 = (TextView) findViewById(R.id.calendar2);
        choosecal2 = (LinearLayout) findViewById(R.id.choosecal2);

        startDate2 =(EditText) findViewById(R.id.startDate2);
        endDate2 =(EditText) findViewById(R.id.endDate2);
        startDate2.setFocusable(false);
        endDate2.setFocusable(false);

        starttime1 =(EditText) findViewById(R.id.starttime1);
        endtime1 =(EditText) findViewById(R.id.endtime1);
        starttime1.setFocusable(false);
        endtime1.setFocusable(false);

        timeS1 = (TextView) findViewById(R.id.timeS1);
        timeE1 = (TextView) findViewById(R.id.timeE1);
        intim1 = (TextInputLayout) findViewById(R.id.intim1);
        outtim1 = (TextInputLayout) findViewById(R.id.outtim1);

        try {
            if(getIntent().getExtras().getString("KEY").equals("MODIFY")){
                calendar2.setVisibility(View.GONE);
                choosecal2.setVisibility(View.VISIBLE);
                intim1.setVisibility(View.VISIBLE);
                outtim1.setVisibility(View.VISIBLE);
                timeS1.setVisibility(View.GONE);
                timeE1.setVisibility(View.GONE);
            }else{
                calendar2.setVisibility(View.VISIBLE);
                choosecal2.setVisibility(View.GONE);
                intim1.setVisibility(View.GONE);
                outtim1.setVisibility(View.GONE);
                timeS1.setVisibility(View.VISIBLE);
                timeE1.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {

        }

        startDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                datePickerDialog = new DatePickerDialog(View2.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        startDate2.setText(dateFormatter.format(newDate.getTime()));

                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

               // datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        endDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                datePickerDialog = new DatePickerDialog(View2.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        endDate2.setText(dateFormatter.format(newDate.getTime()));

                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

               // datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        starttime1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(View2.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        starttime1.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        endtime1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(View2.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        endtime1.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
    }



    public void topiclink1(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://drive.google.com/open?id=1rWzEN1IPfTN5OdVd9DleZmrXgp3DhPcXS25hCGjAUdQ"));
        startActivity(i);
    }

    public void topiclink2(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://drive.google.com/open?id=1Kf7O_HeAvYdNnKRhE4rGSOu-JpIrBSxmQ6gN9BaBEZU"));
        startActivity(i);
    }
}
