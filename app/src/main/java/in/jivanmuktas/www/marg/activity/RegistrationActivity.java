package in.jivanmuktas.www.marg.activity;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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
import in.jivanmuktas.www.marg.model.City;
import in.jivanmuktas.www.marg.model.Country;
import in.jivanmuktas.www.marg.model.CountrySetGet;
import in.jivanmuktas.www.marg.network.HttpClient;
import in.jivanmuktas.www.marg.network.HttpGetHandler;

public class RegistrationActivity extends BaseActivity {
    EditText etFirstName,etLastName,etDob,etAge,etPass,etRePass,etEmail,etPhoneNumber,etHelpAnother;
    RadioGroup title,rgGender;
    Spinner spinner_satsang,spinner_edu,spinner_country;
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
    ArrayList<String> select;
    ArrayList<Country> countries = new ArrayList<>();
    ArrayList<City> cities = new ArrayList<>();
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
        //////////////************************///////////////////

        jivanmuktasDB = JivanmuktasDB.getInstance(this);
        jivanmuktasDB.open();

        title = (RadioGroup) findViewById(R.id.title);
        etFirstName =(EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        rgGender = (RadioGroup) findViewById(R.id.rgGender);
        etDob = (EditText) findViewById(R.id.etDob);
        etAge = (EditText) findViewById(R.id.etAge);
        etPass = (EditText) findViewById(R.id.etPass);
        etRePass = (EditText) findViewById(R.id.etRePass);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        spinner_satsang = (Spinner) findViewById(R.id.spinner_satsang);
        spinner_edu = (Spinner) findViewById(R.id.spinner_edu);
        spinner_country = (Spinner) findViewById(R.id.spinner_country);
        spinner_city = (SearchableSpinner) findViewById(R.id.spinner_city);
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

        ////////// Spinner For Country
        //CustomSpinner(spinner_country, R.array.country);
        SetCountrySpinner();
        //new GetSatsangChapter().execute();
        ////////// Spinner For Education
        CustomSpinner(spinner_edu, R.array.education);

        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {
                    new GetSatsangChapter().execute("" + position);
                    SetCitySpinner(countries.get(position).getCountry_id());
                }else {
                    /////// If Country selected position is 0
                    satsang = new ArrayList<>();
                    satsang.add("Select Chapter");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_spinner_item, satsang);
                    adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    spinner_satsang.setAdapter(adapter);
                    /////// If Country selected position is 0
                    select = new ArrayList<>();
                    select.add("Select City");
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_spinner_item, select);
                    adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    spinner_city.setAdapter(adapter2);
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
        }else if (spinner_satsang.getSelectedItemPosition() == 0) {
            SnackbarRed(R.id.reglayout,"Please Choose Satsang Chapter");
            flag = false;
        }else if (spinner_edu.getSelectedItemPosition() == 0) {
            SnackbarRed(R.id.reglayout,"Please Choose Education");
            flag = false;
        }else if (etHelpAnother.getText().toString().trim().length() == 0 ) {
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
    //******************************
    public void SetCountrySpinner(){
        String url = Constant.GET_COUNTRY_LIST;

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
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Country country = new Country();
                                    country.setCountry_id(jsonObject.getString("country_id"));
                                    country.setCountry_name(jsonObject.getString("country_name"));
                                    countries.add(country);
                                }
                                ArrayAdapter aa = new ArrayAdapter(RegistrationActivity.this,android.R.layout.simple_spinner_item,countries);
                                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner_country.setAdapter(aa);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // display response
                        Log.d("!!!!!Response", response.toString());

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

        RequestQueue queue = Volley.newRequestQueue(this);
        // add it to the RequestQueue
        queue.add(getRequest);
    }
    /////***********************//////////////////
    public void SetCitySpinner(String id){
        String url =Constant.GET_CITY_LIST+id;

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
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    City city = new City();
                                    city.setCity_id(jsonObject.getString("city_id"));
                                    city.setCity_name(jsonObject.getString("city_name"));
                                    cities.add(city);
                                }
                                ArrayAdapter arr=new ArrayAdapter(RegistrationActivity.this, R.layout.spinner_dropdown_item, cities);
                                spinner_city.setTitle("Select City");
                                spinner_city.setAdapter(arr);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // display response
                        Log.d("!!!!!Response", response.toString());
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

        RequestQueue queue = Volley.newRequestQueue(this);
        // add it to the RequestQueue
        queue.add(getRequest);
    }

    ///////***********************//////////////////
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
                RadioButton rbTitle = (RadioButton) findViewById(title.getCheckedRadioButtonId());
                int indexTitle = title.indexOfChild(rbTitle)+1;
                reqObj.put("TITLE",String.valueOf(indexTitle));// 1 = Mr., 2 = Mrs., 3 = Miss.
                reqObj.put("NAME",etFirstName.getText().toString().trim()+" "+etLastName.getText().toString().trim());
                RadioButton rb = (RadioButton) findViewById(rgGender.getCheckedRadioButtonId());
                int indexGen = rgGender.indexOfChild(rb);
                reqObj.put("GENDER",String.valueOf(indexGen));// 0 = male, 1 = Female
                reqObj.put("DOB", etDob.getText().toString().trim());
                reqObj.put("EMAIL", etEmail.getText().toString().trim());
                reqObj.put("CONTACT", etPhoneNumber.getText().toString().trim());
                reqObj.put("PASSWORD", etPass.getText().toString().trim());
                reqObj.put("COUNTRY", String.valueOf(spinner_country.getSelectedItemPosition()));
                reqObj.put("CITY", String.valueOf(spinner_city.getSelectedItemPosition()));
                reqObj.put("COUNTRY_CODE", "+91");
                reqObj.put("CHAPTER", spinner_satsang.getSelectedItem().toString());
                reqObj.put("EDUCATION", String.valueOf(spinner_edu.getSelectedItemPosition()));
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
                    CustomToast("Server Busy! \nPlease try again later.");
                }
            }
        }
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

                    }
                    ////////// Spinner For satsang
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_spinner_item, satsang);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_satsang.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                CustomToast("Server Busy! \nPlease try again later.");
            }
        }
    }
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
    ///////////// Hide Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);*/
        return false;
    }
}
