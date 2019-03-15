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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import in.jivanmuktas.www.marg.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import in.jivanmuktas.www.marg.constant.Constant;
import in.jivanmuktas.www.marg.database.JivanmuktasDB;
import in.jivanmuktas.www.marg.dataclass.UserData;
import in.jivanmuktas.www.marg.model.Country;
import in.jivanmuktas.www.marg.model.Education;
import in.jivanmuktas.www.marg.network.HttpGetHandler;
import in.jivanmuktas.www.marg.network.HttpPutHandler;
import in.jivanmuktas.www.marg.singleton.VolleySingleton;

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
    ArrayList<Country> countries = new ArrayList<>();
    ArrayList<Education> edu = new ArrayList<>();
    String chapter;
    String countryId="";
    String EducationId="";
    HashMap<String, String> educationMap = new HashMap<>();
    HashMap<String, String> countryMap = new HashMap<>();
    HashMap<String, String> chapterMap = new HashMap<>();
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


        SetCountrySpinner();
        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0 ){
                    countryId = countries.get(position).getCountry_id();
                    Log.d("!!!countries",countryId.toString());
                    new GetSatsangChapter().execute("" + countries.get(position).getCountry_id());
                    /// Set Country code
                    String[] array = getResources().getStringArray(R.array.country_code);
                }
                else {
                    /////// If Country selected position is 0
                    satsang = new ArrayList<>();
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

        SetEducation();
        spinner_edu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    EducationId = edu.get(position).getEducation_id();
                    //    Toast.makeText(RegistrationActivity.this, "edu.get(position).getEducation_id()", Toast.LENGTH_SHORT).show();
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
                    //    new ProfileUpdate().execute();
                        CurentProfileUpdate();
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
        }/*else if(etPostalCode.getText().toString().trim().length()==0){
            etPostalCode.setError("Please enter your Postal Code ");
            editTextFocus(etPostalCode);
            return false;
        }*/


        return flag;
    }

    /////***********************//////////////////

    public void CurentProfileUpdate() {
        final String url = Constant.ProfileUpdate;

        JSONArray jsonArray = new JSONArray();
        JSONObject reqObj = new JSONObject();
        try {
            reqObj.put("USER_ID", app.getUserId());
            RadioButton rbTitle = (RadioButton) findViewById(rgTitle.getCheckedRadioButtonId());
            int indexTitle = rgTitle.indexOfChild(rbTitle)+1;
            reqObj.put("STATUS", "26");
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
            jsonArray.put(reqObj);
            final String requestBody = jsonArray.toString();
            Log.i("!!!req", jsonArray.toString());
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("!!!Response->", response);
                            Toast.makeText(ViewProfile.this, "Updated Sucessfully", Toast.LENGTH_SHORT).show();
                            ViewProfile.this.finish();
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
    /*public class GetSatsangChapter extends AsyncTask<String, String, Boolean> {

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
    }*/
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
                    Log.i("!!!country",country);
                    String City  = object.getString("CITY");
                 //   String status = object.getString("STATUS");
                 //   String BusinessProfile = object.getString("ROLE");
                    chapter = object.getString("SATSANG_CHAPTER");
                    String education = object.getString("EDUCATION");
                    Log.i("!!!education",education);
                 //   String help_in_other_activity = object.getString("HELP_IN_OTHER_ACTIVITY");
                //    String postal_code = "0";
                    ////////////////////
                   /* title = "1";// Please Remove this line
                    gender="1";// Please Remove this line
                    country = "2";
                    education="2";*/
                    /////********
            //        userName.setText(UserData.getTitle(title)+" "+username);
                    userName.setText(title + " " + username);
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

                    ///////////**********************
                    etName.setText(username);
                    etDob.setText(dateOfBirth);
                    etEmail.setText(emailId);
                    etPhoneNumber.setText(contactNo);
                    if(title.equalsIgnoreCase("Mr")){
                        mr.setChecked(true);
                    }else if(title.equalsIgnoreCase("Mrs")){
                        mrs.setChecked(true);
                    }else {
                       miss.setChecked(true);
                    }
                    if (gender.equalsIgnoreCase("MALE")) {
                        rbMale.setChecked(true);
                    } else {
                        rbFemale.setChecked(true);
                    }

                    String selectPos1 = countryMap.get(country);
                    spinner_country.setSelection(Integer.parseInt(selectPos1));

                    String selectPos2 = chapterMap.get(chapter);
                    spinner_satsang.setSelection(Integer.parseInt(selectPos2));

                    String selectPos3 = educationMap.get(education);
                    Log.i("!!!selectpos",selectPos3);
                    spinner_edu.setSelection(Integer.parseInt(selectPos3));


                    //       ((RadioButton)rgTitle.getChildAt(Integer.parseInt(title)-1)).setChecked(true);
             //       etName.setText(username);

             //       ((RadioButton)rgGender.getChildAt(Integer.parseInt(gender))).setChecked(true);
             //       etDob.setText(dateOfBirth);
             //       etEmail.setText(emailId);
             //       etPhoneNumber.setText(contactNo);
                    //etPostalCode.setText(postal_code);

                    ////////// Spinner For Country
             //       CustomSpinner(spinner_country, R.array.country);

             //       spinner_country.setSelection(Integer.parseInt(country));
                    /*CustomSpinner(spinner_edu,Integer.parseInt(education));
                    spinner_edu.setSelection(Integer.parseInt(education));*/

                    ////////// Spinner For Education
            //        CustomSpinner(spinner_edu, R.array.education);

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

    /*private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }*/
    /////////////COUNTRIES///////////////
    public void SetCountrySpinner(){
        String url = Constant.GET_COUNTRY_LIST;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject object = response;
                            if (object.getString("status").equals("true")){
                                Log.d("!!! countries",object.toString());
                                JSONArray jsonArray = object.getJSONArray("response");
                                Log.d("!!! countries",response.toString());
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                //    Country country = new Country();
                                //    country.setCountry_id(jsonObject.getString("LOV_ID"));
                                //    country.setCountry_name(jsonObject.getString("LOV_NAME"));
                                //    countries.add(country);

                                    String countryId = jsonObject.getString("LOV_ID");
                                    String countryName = jsonObject.getString("LOV_NAME");
                                    countries.add(new Country(countryId,countryName));
                                    countryMap.put(countryName,"" + i);
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ArrayAdapter<Country> country = new ArrayAdapter<Country>(ViewProfile.this,android.R.layout.simple_list_item_1,countries);
                        country.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_country.setAdapter(country);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );
        VolleySingleton.getInstance(this).addToRequestQueue(getRequest);
    }

    public class GetSatsangChapter extends AsyncTask<String, String, Boolean> {
        JSONObject jsonObject1;
        JSONArray jsonArray1;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDailog();
        }
        //?id=0&eventid=1
        @Override
        protected Boolean doInBackground(String... params) {
            String s = params[0];
            HttpGetHandler handler = new HttpGetHandler();
            try {
                Log.i("!!!!URL",Constant.GET_SATSANG_CHAPTER+s);
                String response = handler.makeServiceCall(Constant.GET_SATSANG_CHAPTER+s);
                //String response = handler.makeServiceCall(Constant.GET_SATSANG_CHAPTER);
                // response = AssetJSONFile("SatsangChapter.json",RegistrationActivity.this);
                Log.d("!!!Response", response.toString());
                jsonObject1 = new JSONObject(response);

                jsonArray1 = jsonObject1.getJSONArray("response");
                Log.i("!!!Response",response);
                if(jsonObject1.getBoolean("status"))
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

            if(s) {
                ArrayList<String> satsang = new ArrayList<>();
                satsang.add("Select Chapter");
                try {
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject object = jsonArray1.getJSONObject(i);
                        satsang.add(object.getString("CHAPTER_NAME"));

                        chapterMap.put(object.getString("CHAPTER_NAME"),""+i); //chages for spinner position

                    }
                    ////////// Spinner For satsang
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ViewProfile.this, android.R.layout.simple_spinner_item, satsang);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_satsang.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                CustomToast("No Data Found.");
            }
        }
    }

    public void SetEducation(){
        String url =Constant.GET_EDUCATION_LIST;
        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject object = response;
                            if (object.getString("status").equals("true")){
                                JSONArray jsonArray = object.getJSONArray("response");
                                for (int i=0;i<jsonArray.length();i++){
                                    Log.d("!!!!Education",response.toString());
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                        //            Education education = new Education();
                                    String educationId = jsonObject.getString(("LOV_ID"));
                                    String educationName = jsonObject.getString("LOV_NAME");
                         //           education.setEducation_id(jsonObject.getString("LOV_ID"));
                        //            education.setEducation_name(jsonObject.getString("LOV_NAME"));
                                    edu.add(new Education(educationName,educationId));
                        //            edu.add(education);
                                    educationMap.put(educationName,"" + i);

                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ArrayAdapter educate=new ArrayAdapter(ViewProfile.this, R.layout.spinner_dropdown_item, edu);
                        educate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_edu.setAdapter(educate);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );

        VolleySingleton.getInstance(this).addToRequestQueue(getRequest);
    }
}
