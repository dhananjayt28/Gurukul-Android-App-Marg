package in.jivanmuktas.www.marg.activity;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import in.jivanmuktas.www.marg.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import in.jivanmuktas.www.marg.constant.Constant;
import in.jivanmuktas.www.marg.database.JivanmuktasDB;
import in.jivanmuktas.www.marg.dataclass.UserData;
import in.jivanmuktas.www.marg.network.HttpGetHandler;
import in.jivanmuktas.www.marg.network.HttpPutHandler;

public class ViewProfile extends BaseActivity {
    private static final String TAG = "ViewProfile";
    TextView userName,userEdu,userDob,userAge,userGender,userContact,userEmail,userCountry,userChapter,userPin,userCity;
    RadioGroup rgTitle,rgGender;
    RadioButton mr,mrs,miss,rbMale,rbFemale;
    EditText etName,etDob,etAge,etEmail,etPhoneNumber,etPostalCode;
    Spinner spinner_country,spinner_satsang,spinner_edu;
    Button btEdit,btUpdate,btCancel;
    CardView view1,view2;

    private SimpleDateFormat dateFormatter;
    private DatePickerDialog datePickerDialog;

    JSONObject jsonResponse;
    JSONArray responseArray;

    JivanmuktasDB jivanmuktasDB;
    Cursor result;
    ArrayList<String> satsang;
    LinearLayout profileLayout;

    String chapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_top);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Profile");
        Log.i("!!!Activity",TAG);

/////////////////////************************////////////////////////////
        jivanmuktasDB = JivanmuktasDB.getInstance(this);
        jivanmuktasDB.open();

        profileLayout = (LinearLayout) findViewById(R.id.profileLayout);

        view1 = (CardView) findViewById(R.id.view1);
        view2 = (CardView) findViewById(R.id.view2);
        view1.setVisibility(View.GONE);
        view2.setVisibility(View.GONE);

        //View 1

        userName = (TextView) findViewById(R.id.userName);
        userEdu = (TextView) findViewById(R.id.userEdu);
        userDob = (TextView) findViewById(R.id.userDob);
        userAge = (TextView) findViewById(R.id.userAge);
        userGender = (TextView) findViewById(R.id.userGender);
        userContact = (TextView) findViewById(R.id.userContact);
        userEmail = (TextView) findViewById(R.id.userEmail);
        userCountry = (TextView) findViewById(R.id.userCountry);
        userChapter = (TextView) findViewById(R.id.userChapter);
        userCity = (TextView) findViewById(R.id.userCity);
        userPin = (TextView) findViewById(R.id.userPin);
        btEdit = (Button) findViewById(R.id.btEdit);
        ////#########################################################////

        //View 2

        rgTitle = (RadioGroup) findViewById(R.id.title);
        rgGender = (RadioGroup) findViewById(R.id.rgGender);
        mr = (RadioButton) findViewById(R.id.mr);
        mrs = (RadioButton) findViewById(R.id.mrs);
        miss = (RadioButton) findViewById(R.id.miss);
        rbMale = (RadioButton) findViewById(R.id.rbMale);
        rbFemale = (RadioButton) findViewById(R.id.rbFemale);
        etName = (EditText) findViewById(R.id.etName);
        etDob = (EditText) findViewById(R.id.etDob);
        etAge = (EditText) findViewById(R.id.etAge);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        spinner_country = (Spinner) findViewById(R.id.spinner_country);
        spinner_satsang = (Spinner) findViewById(R.id.spinner_satsang);
        spinner_edu = (Spinner) findViewById(R.id.spinner_edu);
        etPostalCode = (EditText) findViewById(R.id.etPostalCode);
        btUpdate = (Button) findViewById(R.id.btUpdate);
        btCancel = (Button) findViewById(R.id.btCancel);
////#########################################################////

if(isNetworkAvailable()){
    new GetUserProfile().execute();
}else {
    finish();
}

            spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position!=0) {
                        new GetSatsangChapter().execute("" + position);
                    }else {
                        /////// If Country selected position is 0
                        ArrayList<String> satsang = new ArrayList<>();
                        satsang.add("Select Chapter");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ViewProfile.this, android.R.layout.simple_spinner_item, satsang);
                        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        spinner_satsang.setAdapter(adapter);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        etDob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
/////////*********  Calculate Age **********///////
                String age = CalculateAge(String.valueOf(s));
                etAge.setText( age +" Years " );// set age to edit text
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btEdit.setOnClickListener(this);
        btUpdate.setOnClickListener(this);
        btCancel.setOnClickListener(this);
        etDob.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btEdit:
                view1.setVisibility(View.GONE);
                view2.setVisibility(View.VISIBLE);
                break;
            case R.id.btUpdate:
                if (isNetworkAvailable()){
                    if (isValid()){
                        new ProfileUpdate().execute();
                    }
                }
                break;
            case R.id.btCancel:
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.GONE);
                break;
            case R.id.etDob:
                Calendar newCalendar = Calendar.getInstance();
                dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                datePickerDialog = new DatePickerDialog(ViewProfile.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        etDob.setText(dateFormatter.format(newDate.getTime()));

                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                datePickerDialog.show();
                break;
        }
    }

    public boolean isValid() {
        boolean flag = true;

        if(etName.getText().toString().trim().length()==0){
            etName.setError("Please enter your Name ");
            editTextFocus(etName);
            return false;
        }else if(etDob.getText().toString().trim().length()==0){
            etDob.setError("Please enter your D.O.B ");
            editTextFocus(etDob);
            return false;
        }else if(etEmail.getText().toString().trim().length()==0){
            etEmail.setError("Please enter your Email Id ");
            editTextFocus(etEmail);
            return false;
        }else if(!mailValidationCk(etEmail.getText().toString().trim())){
            etEmail.setError("Please enter valid Email ID");
            flag = false;
        }else if(etPhoneNumber.getText().toString().trim().length()==0){
            etPhoneNumber.setError("Please enter your Phone Number ");
            editTextFocus(etPhoneNumber);
            return false;
        }else if(spinner_country.getSelectedItemPosition()==0){
            SnackbarRed(R.id.profileLayout,"Please Select your country.");
            return false;
        }else if(spinner_satsang.getSelectedItemPosition()==0){
            SnackbarRed(R.id.profileLayout,"Please Select Satsang Chapter.");
            return false;
        }else if(spinner_edu.getSelectedItemPosition()==0){
            SnackbarRed(R.id.profileLayout,"Please Select Education. ");
            return false;
        }else if(etPostalCode.getText().toString().trim().length()==0){
            etPostalCode.setError("Please enter your Postal Code ");
            editTextFocus(etPostalCode);
            return false;
        }


        return flag;
    }

    /////***********************//////////////////
    public class ProfileUpdate extends AsyncTask<String, String, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDailog();
        }
        @Override
        protected Boolean doInBackground(String... params) {
            try {
                JSONArray reqArr = new JSONArray();
                JSONObject reqObj = new JSONObject();
                reqObj.put("USER_ID",app.getUserId());
                RadioButton rbTitle = (RadioButton) findViewById(rgTitle.getCheckedRadioButtonId());
                int indexTitle = rgTitle.indexOfChild(rbTitle)+1;
                reqObj.put("TITLE",String.valueOf(indexTitle));// 0 = Mr., 1 = Mrs., 2 = Miss.
                reqObj.put("NAME",etName.getText().toString().trim());
                RadioButton rb = (RadioButton) findViewById(rgGender.getCheckedRadioButtonId());
                int indexGen = rgGender.indexOfChild(rb);
                reqObj.put("GENDER",String.valueOf(indexGen));// 0 = male, 1 = Female
                reqObj.put("DOB", etDob.getText().toString().trim());
                reqObj.put("COUNTRY_CODE", " ");
                reqObj.put("CONTACT", etPhoneNumber.getText().toString().trim());
                reqObj.put("EMAIL", etEmail.getText().toString().trim());
                reqObj.put("POSTAL_CODE", etPostalCode.getText().toString().trim());
                reqObj.put("COUNTRY", String.valueOf(spinner_country.getSelectedItemPosition()));
                reqObj.put("CHAPTER", spinner_satsang.getSelectedItem().toString().trim());
                reqObj.put("EDUCATION", String.valueOf(spinner_edu.getSelectedItemPosition()));

                reqArr.put(reqObj);
                System.out.println("!!reqArr  " + reqArr);

                String response = HttpPutHandler.SendHttpPut(Constant.ProfileUpdate, reqObj.toString());


                Log.d("!!!Response", response.toString());
                jsonResponse = new JSONObject(response);
                if(jsonResponse.getString("status").equals("true")){
                    return true;
                }else {
                    return false;
                }


            } catch (Exception e) {
                System.out.println("!! Reach here error " + e.getMessage());
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            dismissProgressDialog();

            if(s){
                Toast.makeText(ViewProfile.this,"Profile Update Successfull!",Toast.LENGTH_LONG).show();
                CustomIntent(ViewProfile.class);
            }else {
                Toast.makeText(ViewProfile.this,"Profile Update Failed!",Toast.LENGTH_LONG).show();
            }

        }
    }

    /////***********************//////////////////
    public class GetSatsangChapter extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDailog();
        }
        @Override
        protected Boolean doInBackground(String... params) {
            String s = params[0];
            try {
                result =jivanmuktasDB.GetSatsangChapter(s);
                if(result.getCount()!=0){
                    result.moveToFirst();
                    satsang = new ArrayList<>();
                    satsang.add("Select Chapter");
                    do {
                        String chapterName = result.getString(0).trim();
                        satsang.add(chapterName);
                    }while(result.moveToNext());
                    return true;
                }
            } catch (Exception e) {
                System.out.println("!! Reach here error " + e.getMessage());
                e.printStackTrace();
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            dismissProgressDialog();
            if(s) {
                ////////// Spinner For satsang
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ViewProfile.this, android.R.layout.simple_spinner_item, satsang);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinner_satsang.setAdapter(adapter);
                spinner_satsang.setSelection(satsang.indexOf(chapter));// select position
            }else {
                CustomToast("Server Busy! \nPlease try again later.");
            }
        }
    }
    /////***********************//////////////////
   /* public class GetSatsangChapter extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDailog();
        }
        @Override
        protected Boolean doInBackground(String... params) {
            String s = params[0];
            HttpGetHandler handler = new HttpGetHandler();
            try {
                String response = handler.makeServiceCall(Constant.GET_SATSANG_CHAPTER+s);

                jsonResponse = new JSONObject(response);

                responseArray = jsonResponse.getJSONArray("response");
                Log.i("!!!Response",response);
                if(jsonResponse.getBoolean("status"))
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

        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            dismissProgressDialog();
            ArrayList<String> satsang = new ArrayList<>();
            satsang.add("Select Chapter");
            try {
                for(int i = 0;i<responseArray.length();i++){
                    JSONObject object = responseArray.getJSONObject(i);
                    satsang.add(object.getString("CHAPTER_NAME"));
                }
                ////////// Spinner For satsang
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ViewProfile.this, android.R.layout.simple_spinner_item, satsang);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinner_satsang.setAdapter(adapter);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
*/
    ///**************************************************************////

    public class GetUserProfile extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDailog();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            HttpGetHandler handler = new HttpGetHandler();
            try {
                JSONArray reqArr = new JSONArray();
                JSONObject reqObj = new JSONObject();

                reqArr.put(reqObj);
                System.out.println("!!!reqArr  " + reqArr);

            //    String response = handler.makeServiceCall(Constant.ProfileView+"?id="+app.getUserId());
                String response = handler.makeServiceCall(Constant.ProfileView+"?user_id="+app.getUserId());
             //   String response = handler.makeServiceCall(Constant.GET_USER_DATA+"?id="+app.getUserId());

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
        protected void onPostExecute(Boolean response) {
            super.onPostExecute(response);
            dismissProgressDialog();

            if (response) {

                try {
                    view1.setVisibility(View.VISIBLE);
                    JSONArray array = jsonResponse.getJSONArray("response");

                    JSONObject object = array.getJSONObject(0);
                    String title = object.getString("TITLE"); //na
                    String username = object.getString("NAME");
                    String gender = object.getString("GENDER"); // value
                    String dateOfBirth = object.getString("DOB"); // proper formatting
                //    String countryCode = object.getString("COUNTRY_CODE"); // country code
                    String contactNo = object.getString("MOBILE_NO"); // only mobile no.
                    String emailId = object.getString("EMAIL_ID");
                    String country = object.getString("COUNTRY");
                    String City  = object.getString("CITY");
                 //   String status = object.getString("STATUS");
                 //   String BusinessProfile = object.getString("ROLE");
                    chapter = object.getString("SATSANG_CHAPTER");
                    String education = object.getString("EDUCATION");
                 //   String help_in_other_activity = object.getString("HELP_IN_OTHER_ACTIVITY");
                //    String postal_code = "0";
                    ////////////////////
                   /* title = "1";// Please Remove this line
                    gender="1";// Please Remove this line
                    country = "2";
                    education="2";*/
                    /////********
                    userName.setText(UserData.getTitle(title)+" "+username);
                    userDob.setText(dateOfBirth);
                    userAge.setText(CalculateAge(dateOfBirth));
                    userGender.setText(gender);
                    userCity.setText(City);
                    userContact.setText(contactNo);
                    userEmail.setText(emailId);
                    userCountry.setText(country);
                    userChapter.setText(chapter);
                    //userEdu.setText(status); //check
                    userEdu.setText(education);
                    //userPin.setText(help_in_other_activity); //check
                    //userPin.setText(BusinessProfile);  //check

                    ///////////***********************

                    ((RadioButton)rgTitle.getChildAt(Integer.parseInt(title)-1)).setChecked(true);
                    etName.setText(username);

                    ((RadioButton)rgGender.getChildAt(Integer.parseInt(gender))).setChecked(true);
                    etDob.setText(dateOfBirth);
                    etEmail.setText(emailId);
                    etPhoneNumber.setText(contactNo);
                    //etPostalCode.setText(postal_code);

                    ////////// Spinner For Country
                    CustomSpinner(spinner_country, R.array.country);

                    spinner_country.setSelection(Integer.parseInt(country));

                    ////////// Spinner For Education
                //    CustomSpinner(spinner_edu, R.array.education);

                //    spinner_edu.setSelection(Integer.parseInt(education));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                view1.setVisibility(View.GONE);
                CustomToast("Something went wrong!\nPlease try again later.");
            }
        }
    }
}
