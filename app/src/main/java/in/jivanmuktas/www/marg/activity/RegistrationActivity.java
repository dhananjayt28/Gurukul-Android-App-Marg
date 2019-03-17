package in.jivanmuktas.www.marg.activity;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import in.jivanmuktas.www.marg.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import in.jivanmuktas.www.marg.constant.Constant;
import in.jivanmuktas.www.marg.database.JivanmuktasDB;
import in.jivanmuktas.www.marg.model.Chapter;
import in.jivanmuktas.www.marg.model.City;
import in.jivanmuktas.www.marg.model.Country;
import in.jivanmuktas.www.marg.model.CountrySetGet;
import in.jivanmuktas.www.marg.model.Education;
import in.jivanmuktas.www.marg.model.Title;
import in.jivanmuktas.www.marg.network.HttpClient;
import in.jivanmuktas.www.marg.network.HttpGetHandler;
import in.jivanmuktas.www.marg.singleton.VolleySingleton;

public class RegistrationActivity extends BaseActivity {
    EditText etFirstName,etLastName,etDob,etAge,etPass,etRePass,etEmail,etPhoneNumber,etHelpAnother;
    RadioGroup title,rgGender;
    Spinner spinner_satsang,spinner_edu,spinner_country,spinner_title,spinner_gender;
    SearchableSpinner spinner_city;



    private SimpleDateFormat dateFormatter;
    private DatePickerDialog datePickerDialog;
    Button button_Sign_Up;
    String countryCode;

    JSONObject jsonResponse;
    JSONArray responseArray;
    JivanmuktasDB jivanmuktasDB;
    Cursor result;
    ArrayList<String> satsang;
    ArrayList<Country> countries = new ArrayList<>();
    ArrayList<City> cities = new ArrayList<>();
    ArrayList<Education> edu = new ArrayList<>();
    ArrayList<Title> titles = new ArrayList<>();
    ArrayList<Chapter> chapters = new ArrayList<>();
    String countryId="";
    String CityId="";
    String EducationId="";
    String TitleId = "";
    String[] Gender = {"Select","MALE","FEMALE"};
    String GenderId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTop);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Registration");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final String countr;
        //////////////************************///////////////////

        jivanmuktasDB = JivanmuktasDB.getInstance(this);
        jivanmuktasDB.open();

        title = (RadioGroup) findViewById(R.id.title);
        etFirstName =(EditText) findViewById(R.id.etFirstName);
    //    etFirstName.setHint(Html.fromHtml(getString(R.string.first_name)));
        etLastName = (EditText) findViewById(R.id.etLastName);
    //    etLastName.setHint(Html.fromHtml(getString(R.string.last_name)));
        rgGender = (RadioGroup) findViewById(R.id.rgGender);
        etDob = (EditText) findViewById(R.id.etDob);
    //    etDob.setHint(Html.fromHtml(getString(R.string.enterdob)));
        etAge = (EditText) findViewById(R.id.etAge);
        etPass = (EditText) findViewById(R.id.etPass);
        etRePass = (EditText) findViewById(R.id.etRePass);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        spinner_satsang = (Spinner) findViewById(R.id.spinner_satsang);
        spinner_edu = (Spinner) findViewById(R.id.spinner_edu);  //education
        spinner_country = (Spinner) findViewById(R.id.spinner_country);
        spinner_city = (SearchableSpinner) findViewById(R.id.spinner_city);
        spinner_title = (Spinner)findViewById(R.id.spinner_title);
        spinner_gender = (Spinner)findViewById(R.id.spinner_gender);
        etHelpAnother = (EditText) findViewById(R.id.etHelpAnother);
        button_Sign_Up = (Button)findViewById(R.id.button_Sign_Up);
        /////////////////////************************///////////////////
        button_Sign_Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()){
                    new SubmitRegistration().execute();
                }
            }
        });

        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                datePickerDialog = new DatePickerDialog(RegistrationActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        etDob.setText(dateFormatter.format(newDate.getTime()));
                        /////////*********  Calculate Age **********///////
                        String age = CalculateAge( dayOfMonth+"-"+(monthOfYear+1)+"-"+year);
                        etAge.setText( age +" Years " );// set age to edit text
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });


        SetCountrySpinner();
        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0 ){
                    countryId = countries.get(position).getCountry_id();

                    Log.d("!!!countries",countryId.toString());
                //    new GetSatsangChapter().execute("" + countries.get(position).getCountry_id());
                    SetSatsangChapterSpinner(countries.get(position).getCountry_id());
                    /// Set Country code
                    String[] array = getResources().getStringArray(R.array.country_code);
                    }
                else {
                    /////// If Country selected position is 0

                    satsang = new ArrayList<>();
                    satsang.add("Select Chapter");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_spinner_item, satsang);
                    adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    spinner_satsang.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        SetCitySpinner();
        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    CityId = cities.get(position).getCity_id();
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

        SetTitle();
        spinner_title.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    TitleId = titles.get(position).getTitle_name();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        getGender();
        spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    GenderId = Gender[position];
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        spinner_satsang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    public boolean isValid() {
        boolean flag = true;
        if (etFirstName.getText().toString().trim().length() == 0) {
            etFirstName.setError("Please enter your First Name");
            editTextFocus(etFirstName);
            flag = false;
        } else if (etLastName.getText().toString().trim().length() == 0) {
            etLastName.setError("Please enter your Last Name");
            editTextFocus(etLastName);
            flag = false;
        }else if (etDob.getText().toString().trim().length() == 0) {
            SnackbarRed(R.id.reglayout,"Please enter your Date of Birth");
            flag = false;
        }else if (etEmail.getText().toString().trim().length() == 0) {
            etEmail.setError("Please enter your Email ID");
            editTextFocus(etEmail);
            flag = false;
        }else if(etEmail.getText().toString().trim().length() > 0 && !mailValidationCk(etEmail.getText().toString().trim())){
                etEmail.setError("Please enter valid Email ID");
                flag = false;
            editTextFocus(etEmail);
        }
        else if (etPhoneNumber.getText().toString().trim().length() == 0 ) {
            etPhoneNumber.setError("Please enter valid Phone number");
            editTextFocus(etPhoneNumber);
            flag = false;
        }else if (etPass.getText().toString().trim().length() == 0) {
            etPass.setError("Please enter Password");
            editTextFocus(etPass);
            flag = false;
        }else if (!etRePass.getText().toString().trim().equals(etPass.getText().toString().trim())) {
            etRePass.setError("Please Re-Enter Password Correctly");
            editTextFocus(etRePass);
            flag = false;
        }else if (spinner_country.getSelectedItemPosition() == 0) {
            SnackbarRed(R.id.reglayout,"Please Choose Country Name");
            flag = false;
        }else if (spinner_city.getSelectedItemPosition() == 0) {
            SnackbarRed(R.id.reglayout,"Please Choose City Name");
            flag = false;
            Log.d("!!!sayan",String.valueOf(spinner_satsang.getSelectedItemPosition()));
        }/*else if (spinner_satsang.getSelectedItemPosition() == 0) {
            SnackbarRed(R.id.reglayout,"Please Choose Satsang Chapter");
            flag = false;
        }*/else if (spinner_title.getSelectedItemPosition() == 0) {
            SnackbarRed(R.id.reglayout,"Please Choose Title");
            flag = false;
        } else if (spinner_gender.getSelectedItemPosition() == 0) {
            SnackbarRed(R.id.reglayout,"Please Choose gender");
            flag = false;
        } else if (spinner_edu.getSelectedItemPosition() == 0) {
            SnackbarRed(R.id.reglayout,"Please Choose Education");
            flag = false;
        } else if (etHelpAnother.getText().toString().trim().length() == 0 ) {
            etHelpAnother.setError("Please enter Other Activity");
            editTextFocus(etHelpAnother);
            flag = false;
        }
        return flag;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

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
                                    Country country = new Country();
                                    country.setCountry_id(jsonObject.getString("LOV_ID"));
                                    country.setCountry_name(jsonObject.getString("LOV_NAME"));
                                    countries.add(country);
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ArrayAdapter<Country> country = new ArrayAdapter<Country>(RegistrationActivity.this,android.R.layout.simple_list_item_1,countries);
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
    /////////CITY//////////////////
    public void SetCitySpinner(){
        String url = Constant.GET_CITY_LIST;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject object = response;
                            if (object.getString("status").equals("true")){
                                Log.d("!!! city",object.toString());
                                JSONArray jsonArray = object.getJSONArray("response");
                                Log.d("!!! city",response.toString());
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    City city = new City();
                                    city.setCity_id(jsonObject.getString("LOV_ID"));
                                    city.setCity_name(jsonObject.getString("LOV_NAME"));
                                    cities.add(city);
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ArrayAdapter city = new ArrayAdapter(RegistrationActivity.this,android.R.layout.simple_list_item_1,cities);
                        city.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_city.setAdapter(city);
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
                                    Education education = new Education();
                                    education.setEducation_id(jsonObject.getString("LOV_ID"));
                                    education.setEducation_name(jsonObject.getString("LOV_NAME"));
                                    edu.add(education);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ArrayAdapter educate=new ArrayAdapter(RegistrationActivity.this, R.layout.spinner_dropdown_item, edu);
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

    public void SetTitle(){
        String url =Constant.GET_TITLE_LIST;
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
                                    Log.d("!!!!Title",response.toString());
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Title title = new Title();
                                    title.setTitle_id(jsonObject.getString("LOV_ID"));
                                    title.setTitle_name(jsonObject.getString("LOV_NAME"));
                                    titles.add(title);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ArrayAdapter title=new ArrayAdapter(RegistrationActivity.this, R.layout.spinner_dropdown_item, titles);
                        title.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_title.setAdapter(title);
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
    public void getGender(){
        ArrayAdapter gender = new ArrayAdapter(RegistrationActivity.this,R.layout.spinner_dropdown_item,Gender);
        gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_gender.setAdapter(gender);
    }

    public void SetSatsangChapterSpinner(final String chapter){
        chapters.clear();
        String url = Constant.GET_SATSANG_CHAPTER + chapter;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject object = response;
                            if (object.getString("status").equals("true")){
                                Log.d("!!! chapter",object.toString());
                                JSONArray jsonArray = object.getJSONArray("response");
                                Log.d("!!! chapter",response.toString());
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Chapter chapter1 = new Chapter();
                                    chapter1.setChapterId(jsonObject.getString("CHAPTER_ID"));
                                    chapter1.setChapterName(jsonObject.getString("CHAPTER_NAME"));
                                    chapters.add(chapter1);
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ArrayAdapter<Chapter> chapter = new ArrayAdapter<Chapter>(RegistrationActivity.this,android.R.layout.simple_list_item_1,chapters);
                        chapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_satsang.setAdapter(chapter);
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

    ///////***********************//////////////////

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
                ;
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
                satsang.clear();
                satsang.add("Select Chapter");
                try {
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject object = jsonArray1.getJSONObject(i);
                        satsang.add(object.getString("CHAPTER_NAME"));

                    }
                    ////////// Spinner For satsang
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_spinner_item, satsang);
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
    /////***********************//////////////////
    public class SubmitRegistration extends AsyncTask<String, String, Boolean> {

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
            //    RadioButton rbTitle = (RadioButton) findViewById(title.getCheckedRadioButtonId());
            //    int indexTitle = title.indexOfChild(rbTitle)+1;
            //    reqObj.put("TITLE",String.valueOf(indexTitle));// 1 = Mr., 2 = Mrs., 3 = Miss.
                reqObj.put("TITLE",TitleId);
                reqObj.put("NAME",etFirstName.getText().toString().trim()+" "+etLastName.getText().toString().trim());
            //    RadioButton rb = (RadioButton) findViewById(rgGender.getCheckedRadioButtonId());
            //    int indexGen = rgGender.indexOfChild(rb);
            //    reqObj.put("GENDER",String.valueOf(indexGen));// 0 = male, 1 = Female
                reqObj.put("GENDER",GenderId);
                reqObj.put("DOB", etDob.getText().toString().trim());
                reqObj.put("EMAIL", etEmail.getText().toString().trim());
                reqObj.put("CONTACT", etPhoneNumber.getText().toString().trim());
                reqObj.put("PASSWORD", etPass.getText().toString().trim());
                reqObj.put("COUNTRY", countryId);
                reqObj.put("CITY", CityId);
                reqObj.put("COUNTRY_CODE", "+91");
                reqObj.put("CHAPTER", spinner_satsang.getSelectedItem().toString());
                reqObj.put("EDUCATION", EducationId);
            //    reqObj.put("EDUCATION",spinner_edu.getSelectedItem().toString());
                reqObj.put("ACTIVITY", etHelpAnother.getText().toString().trim());

                reqArr.put(reqObj);
                System.out.println("!!reqArr  " + reqArr);

                String response = HttpClient.SendHttpPost(Constant.Registration, reqObj.toString());

                Log.d("!!!Response", response.toString());
                jsonResponse = new JSONObject(response);
                if (jsonResponse.getString("status").equals("true")){
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
            if (s) {
                       CustomToast("Registration Successful !\nYour account is now pending for approval.");
                        CustomIntent(LoginActivity.class);
            }else {
                try {
                    CustomToast(jsonResponse.getString("response")+"\n Registration failed. ");
                }catch (Exception e){
                //    CustomToast("Server Busy! \nPlease try again later.");
                    Toast.makeText(RegistrationActivity.this, "Please Enter a country which has satsang Chapter", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    ///////////// Hide Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);*/
        return false;
    }

    /////////////////***********////************//////////////////
   /* public class GetSatsangChapter extends AsyncTask<String, String, Boolean> {

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
                        String chapterName = result.getString(0);
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_spinner_item, satsang);
                    adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    spinner_satsang.setAdapter(adapter);
            }else {
                CustomToast("Server Busy! \nPlease try again later.");
            }
        }
    }*/


//************************#****************************#*************************#**************************#
/*public class GetCountryList extends AsyncTask<String, String, Boolean> {
    JSONObject jsonObject;
    JSONArray jsonArray;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        showProgressDailog();
    }
    @Override
    protected Boolean doInBackground(String... params) {
        //String response;

        country = new ArrayList<CountrySetGet>();
        HttpGetHandler handler = new HttpGetHandler();
        try {
            String response = handler.makeServiceCall(Constant.GET_COUNTRY_LIST);
            //response = AssetJSONFile("Country.json",RegistrationActivity.this);
            Log.d("!!!Response", response.toString());
            jsonObject = new JSONObject(response);

            jsonArray = jsonObject.getJSONArray("response");
            Log.i("!!!Response",response);
            if(jsonObject.getBoolean("status"))
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
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    satsang.add(object.getString("COUNTRY_NAME"));

                    CountrySetGet setGet = new CountrySetGet();
                    setGet.setCOUNTRY_ID(object.getString("COUNTRY_ID"));
                    setGet.setCOUNTRY_NAME(object.getString("COUNTRY_NAME"));
                    country.add(setGet);

                }
                ////////// Spinner For satsang
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_spinner_item, satsang);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_country.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            CustomToast("Server Busy! \nPlease try again later.");
        }
    }
}*/

    //************************#****************************#*************************#**************************#

}
