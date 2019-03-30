package in.jivanmuktas.www.marg.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import in.jivanmuktas.www.marg.R;
import in.jivanmuktas.www.marg.constant.Constant;
import in.jivanmuktas.www.marg.model.Country;
import in.jivanmuktas.www.marg.model.IdCard;
import in.jivanmuktas.www.marg.network.HttpClient;
import in.jivanmuktas.www.marg.network.HttpGetHandler;
import in.jivanmuktas.www.marg.singleton.VolleySingleton;

public class GitaDistribution extends BaseActivity {
    String EVENT_ID;
    Spinner idpprofSpinner;
    LinearLayout gitadisLayout,layout_button,layout_exit;
    ImageView picImg;
    private String userChoosenTask;
    File file;
    Uri uri;
    Intent CamIntent,GalIntent,CropIntent;
    final int RequestPermissionCode=1;
    TextView tvEventDate,tvNote,tvName,tvDob,tvGender,gitaDistribution,tvMessage,tvbookingContinue,status_show,accomodation,transport,set_id;
    CheckBox cbTransportArran,cbAccomodation;
    String img64code="";
    Button submit,exit;
    ArrayList<IdCard> idCards = new ArrayList<>();
    String id_card = "";
    String Status = "";
    String Message = "";
    String Transportation_arrangement = "";
    String Accomadation_arrangement = "";
    String card_type="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gita_distribution);
        try {
            EVENT_ID = getIntent().getExtras().getString("EVENT_ID");
        } catch (Exception e) {

        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

        idpprofSpinner = (Spinner) findViewById(R.id.idpprofSpinner);
        idpprofSpinner.setVisibility(View.INVISIBLE);
        gitadisLayout = (LinearLayout) findViewById(R.id.gitadisLayout);
        picImg = (ImageView) findViewById(R.id.picImg);
        tvEventDate = (TextView) findViewById(R.id.tvEventDate);
        tvNote = (TextView) findViewById(R.id.tvNote);
        tvName = (TextView) findViewById(R.id.tvName);
        tvDob = (TextView) findViewById(R.id.tvDob);
        tvGender = (TextView) findViewById(R.id.tvGender);
        cbTransportArran = (CheckBox) findViewById(R.id.cbTransportArran);
        cbTransportArran.setVisibility(View.INVISIBLE);
        cbAccomodation = (CheckBox) findViewById(R.id.cbAccomodation);
        cbAccomodation.setVisibility(View.INVISIBLE);
        submit = (Button) findViewById(R.id.submit);
        exit = (Button) findViewById(R.id.exit);
        gitaDistribution = (TextView) findViewById(R.id.gitaDistribution);
        tvMessage = (TextView) findViewById(R.id.tvMessage);
        tvbookingContinue = (TextView)findViewById(R.id.tvbookingContinue);
        tvbookingContinue.setVisibility(View.INVISIBLE);
        layout_button = (LinearLayout) findViewById(R.id.layout_button);
        layout_button.setVisibility(View.INVISIBLE);
        layout_exit =  (LinearLayout) findViewById(R.id.layout_button_exit);
        layout_exit.setVisibility(View.GONE);
        status_show = findViewById(R.id.Status_show);
        accomodation = findViewById(R.id.accomodation_arrangement);
        transport = findViewById(R.id.transport_arrangement);
        set_id = findViewById(R.id.set_id);
        set_id.setVisibility(View.INVISIBLE);


        /*ArrayAdapter<CharSequence> adapterPresonNo = ArrayAdapter.createFromResource(GitaDistribution.this,R.array.itiesidpinner, R.layout.spinner_item);
        adapterPresonNo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        idpprofSpinner.setAdapter(adapterPresonNo);*/

        int permissionCheck = ContextCompat.checkSelfPermission(GitaDistribution.this, Manifest.permission.CAMERA);
        if(permissionCheck == PackageManager.PERMISSION_DENIED) {
            RequestRuntimePermission();
        }

        picImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //////////////////////////////////////////
            //    if (Status.equals(Integer.parseInt("18"))) {
                    final Dialog dialog = new Dialog(GitaDistribution.this);
                    dialog.setContentView(R.layout.imgpickerlayout);
                    dialog.setTitle("Select Option");

// set the custom dialog components - text, image and button
                    CardView takepic = (CardView) dialog.findViewById(R.id.card1);
                    CardView choose = (CardView) dialog.findViewById(R.id.card2);
                    CardView cancel = (CardView) dialog.findViewById(R.id.card3);
// if button is clicked, close the custom dialog
                    takepic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            userChoosenTask = "Take Photo";
                            CameraOpen();
                            dialog.dismiss();
                        }
                    });
                    choose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            userChoosenTask = "Choose from Library";
                            GalleryOpen();
                            dialog.dismiss();
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    //the image icon set to false
                } /*else if (Status.equals(Integer.parseInt("28")) || Status.equals(Integer.parseInt("27"))) {
                    picImg.setVisibility(View.INVISIBLE);

                }*/
        //    }
        });
        //new GetAllData().execute();
        GetOtherData();
        Log.d("!!!This is",Status.toString());

        switch(Status){
            case "28":
                layout_button.setVisibility(View.INVISIBLE);
                layout_exit.setVisibility(View.VISIBLE);
                tvbookingContinue.setVisibility(View.INVISIBLE);
                idpprofSpinner.setVisibility(View.INVISIBLE);
                card_type = getIntent().getStringExtra("CARD_TYPE");
                set_id.setText(card_type);
                set_id.setVisibility(View.VISIBLE);
                picImg.setVisibility(View.INVISIBLE);
                cbAccomodation.setVisibility(View.INVISIBLE);
                cbTransportArran.setVisibility(View.INVISIBLE);
                Accomadation_arrangement = getIntent().getStringExtra("TRANSPORTAION_ARRANGEMENT");
                Transportation_arrangement = getIntent().getStringExtra("ACCOMODATION_ARRANGEMENT");

                if ( Accomadation_arrangement.toString().equalsIgnoreCase("Y"))
                {
                    accomodation.setText("You requested for accomadation arrangement.");
                }
                else {
                   accomodation.setText("No request for accomadation arrangement.");
                }
                if ( Transportation_arrangement.toString().equalsIgnoreCase("Y"))
                {
                    transport.setText("You requested for transportation arrangement.");
                } else {
                    transport.setText("No request for transportation arrangement.");
                }

                break;
            case "18":
                layout_button.setVisibility(View.VISIBLE);
                layout_exit.setVisibility(View.GONE);
                tvbookingContinue.setVisibility(View.VISIBLE);
                picImg.setVisibility(View.INVISIBLE);
                idpprofSpinner.setVisibility(View.VISIBLE);
                set_id.setVisibility(View.INVISIBLE);
                cbAccomodation.setVisibility(View.VISIBLE);
                cbTransportArran.setVisibility(View.VISIBLE);
                accomodation.setVisibility(View.INVISIBLE);
                transport.setVisibility(View.GONE);
                GetSpinner();
                idpprofSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position != 0) {
                            id_card = idCards.get(position).getId();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                break;
                default:

        }




        //the status set to 28 or 27
        /*if (Status.equals(Integer.parseInt("28")) || Status.equals(Integer.parseInt("27"))) {
            tvbookingContinue.setVisibility(View.GONE);
            idpprofSpinner.setVisibility(View.INVISIBLE);
            layout_button.setVisibility(View.INVISIBLE);
            picImg.setVisibility(View.INVISIBLE);
            status_show.setText(Status);
            GetAllData();
            }else if(Status.equals(Integer.parseInt("18"))){
        */
        GetAllData();
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                /*Snackbar snackbar = Snackbar
                        .make(gitadisLayout, "Information Saved Successfully!", Snackbar.LENGTH_LONG);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(getResources().getColor(R.color.colorTextLabel));
                snackbar.show();*/

               /* if (isValid()){
                    new SubmitData().execute();
                }
                  */  // new SubmitData().execute();
                    SubmitData();
                    finish();

                }
            });
            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
         //   GetSpinner();
            /*idpprofSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position != 0) {
                        id_card = idCards.get(position).getId();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });*/

    }
    //**************#******************#*****************#*******************#******************#
    public boolean isValid(){
        if (img64code.equals("")){
            Toast.makeText(GitaDistribution.this,"Please upload an Image.",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    //**************#******************#*****************#*******************#******************#
    private void RequestRuntimePermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(GitaDistribution.this, Manifest.permission.CAMERA))
            Toast.makeText(GitaDistribution.this,"CAMERA permission allows us to access CAMERA app",Toast.LENGTH_SHORT).show();
        else
        {
            ActivityCompat.requestPermissions(GitaDistribution.this,new String[]{Manifest.permission.CAMERA},RequestPermissionCode);
        }
    }

    private void GalleryOpen() {
        GalIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(GalIntent,"Select Image from Gallery"),2);
    }

    private void CameraOpen() {
        CamIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(Environment.getExternalStorageDirectory(),
                "file"+String.valueOf(System.currentTimeMillis())+".jpg");
        uri = Uri.fromFile(file);
        CamIntent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        CamIntent.putExtra("return-data",true);
        startActivityForResult(CamIntent,0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0 && resultCode == RESULT_OK)
            CropImage();
        else if(requestCode == 2)
        {
            if(data != null)
            {
                uri = data.getData();
                CropImage();
            }
        }
        else if (requestCode == 1)
        {
            if(data != null)
            {
                try {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = bundle.getParcelable("data");
                    picImg.setImageBitmap(bitmap);
                    img64code = getEncoded64ImageStringFromBitmap(bitmap);;
                } catch (Exception e) {

                }
            }
        }
    }

    private void CropImage() {
        try{
            CropIntent = new Intent("com.android.camera.action.CROP");
            CropIntent.setDataAndType(uri,"image/*");

            CropIntent.putExtra("crop","true");
            CropIntent.putExtra("outputX",180);
            CropIntent.putExtra("outputY",180);
            CropIntent.putExtra("aspectX",3);
            CropIntent.putExtra("aspectY",4);
            CropIntent.putExtra("scaleUpIfNeeded",true);
            CropIntent.putExtra("return-data",true);

            startActivityForResult(CropIntent,1);
        }
        catch (ActivityNotFoundException ex)
        {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case RequestPermissionCode:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(GitaDistribution.this,"Permission Granted",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(GitaDistribution.this,"Permission Canceled",Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }
    //***#***#***#***#***#***#***#***#***#***#***#***#***#***#***#***#***#***#
    //This Method contains the Name ,DOB,GENDER
    public void GetAllData(){
        final String url = Constant.ProfileView +"?user_id="+ app.getUserId();
        JsonObjectRequest getrequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject object) {
                        try {
                            if(object.getString("status").equals("true")){
                                JSONArray jsonArray = object.getJSONArray("response");
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                tvName.setText(jsonObject.getString("NAME"));
                                tvDob.setText(jsonObject.getString("DOB"));
                                tvGender.setText(jsonObject.getString("GENDER"));
                            //    idpprofSpinner.getSelectedItem().toString();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", error.toString());
            }
        });
        VolleySingleton.getInstance(this).addToRequestQueue(getrequest);
    }
    //This Method contains the ID spinner
    public void GetSpinner(){
        final String url = Constant.GET_ID_CARD_SPINNER ;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject object = response;
                            if(object.getString("status").equals("true")){
                                Log.i("!!!Request",object.toString());
                                JSONArray jsonArray = object.getJSONArray("response");
                                Log.d("!!!response",response.toString());
                                for(int i = 0;i < jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    IdCard idCard = new IdCard();
                                    idCard.setId(jsonObject.getString("LOV_ID"));
                                    idCard.setId_name(jsonObject.getString("LOV_NAME"));
                                    idCards.add(idCard);
                                    Log.d("!!!Data Entered",idCards.toString());

                                }
                                ArrayAdapter<IdCard> idcard = new ArrayAdapter<IdCard>(GitaDistribution.this,android.R.layout.simple_list_item_1,idCards);
                                idcard.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                idpprofSpinner.setAdapter(idcard);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", error.toString());
            }
        });
        VolleySingleton.getInstance(this).addToRequestQueue(getRequest);
    }
    //This Method contans EventName,StartDate,EndDate,Notes,Message
    public void GetOtherData(){
        gitaDistribution.setText(getIntent().getStringExtra("EVENT_NAME"));
        tvEventDate.setText(getIntent().getStringExtra("START_DATE") + " To " + getIntent().getStringExtra("END_DATE"));
        tvNote.setText(getIntent().getStringExtra("NOTES"));
        tvMessage.setText(getIntent().getStringExtra("MESSAGE"));
        Message = getIntent().getStringExtra("MESSAGE");
        Status = getIntent().getStringExtra("STATUS");
        Log.d("!!!!Status incomming", Status.toString());
        Log.d("!!!!Status message", getIntent().getStringExtra("MESSAGE"));

    //        DisableOnSubmission();


    }
    //This Method contains submission of ID_CARD,TRANSPORT AND ACCOMODATION ARRANGEMENT,EVENT_REG_ID
    public void SubmitData(){
        final String url = Constant.GITADISTRIBUTION_ITERARY_CONFIRMATION_UPDATE;
        JSONArray jsonArray =  new JSONArray();
        JSONObject JsonBody = new JSONObject();
        try {
            JsonBody.put("ID_CARD_TYPE",id_card);
            JsonBody.put("TRANSPORTAION_ARRANGEMENT",(cbTransportArran.isChecked()?"Y":"N"));
            JsonBody.put("ACCOMODATION_ARRANGEMENT",(cbAccomodation.isChecked()?"Y":"N"));
            JsonBody.put("EVENT_REG_ID",getIntent().getStringExtra("EVENT_ID"));
            JsonBody.put("USER_ID",app.getUserId());
            jsonArray.put(JsonBody);
            final String requestBody = jsonArray.toString();
            Log.i("!!!!! Update Activity", url + "  " + requestBody);
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("!!!Response->", response);
                            Toast.makeText(GitaDistribution.this, "Updated Sucessfully", Toast.LENGTH_SHORT).show();
                                                   }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("!!!!Error.Response", error.toString());
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

    /*public void DisableOnSubmission(){
        // EventRegistrationConfirmed = 27 | BookingConfirmationShared = 28
        //String Status = getIntent().getStringExtra("STATUS");
        //if(Status == "27" || Status == "28" ){
            //tvbookingContinue.setVisibility(View.GONE);
            //layout_button.setVisibility(View.GONE);
           // idpprofSpinner.setEnabled(false);

        Log.d("!!!!disable section", Status.toString());
        Log.d("!!!disable Message",Message.toString());
            //############################
     //       if (Status.toString() == "28") {
           if(Message.toString() == "Travel confirmation shared"){

            Log.d("!!!!disable section", Status.toString());
                tvbookingContinue.setVisibility(View.GONE);
                idpprofSpinner.setVisibility(View.INVISIBLE);
                layout_button.setVisibility(View.INVISIBLE);
               picImg.setVisibility(View.INVISIBLE);
                status_show.setText(Status);
            picImg.setEnabled(false);
            if(cbAccomodation.isChecked()){
                cbAccomodation.setChecked(true);
            }else{
                cbAccomodation.setChecked(false);
            }
            if (cbTransportArran.isChecked()){
                cbTransportArran.setChecked(true);
            }else {
                cbTransportArran.setChecked(false);
            }
            GetAllData();
        }
        //###############################
    }
*/

    /*public class GetAllData extends AsyncTask<String, String, Boolean> {
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
                //response = handler.makeServiceCall(Constant.VOLUNTEER_EVENT_APPROVE+"id="+app.getUserId()+"&eventid="+EVENT_ID);
                response = AssetJSONFile("gitaventview.json",GitaDistribution.this);
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
                //    String projectName = object.getString("PROJECT_NAME");
                //    String startDate = object.getString("START_DATE");
                //    String endDate = object.getString("END_DATE");
                    String projectName = getIntent().getStringExtra("EVENT_NAME");
                    String startDate = getIntent().getStringExtra("START_DATE");
                    String endDate = getIntent().getStringExtra("END_DATE");
                    String notes = getIntent().getStringExtra("NOTES");


                 //   String note = object.getString("NOTE");
                //    String name = object.getString("NAME");
                //    String dob = object.getString("DOB");
                //    String gender = object.getString("GENDER");

                    tvEventDate.setText(startDate+" To "+endDate);
                    tvNote.setText(notes);
                 //   tvName.setText(name);
                 //   tvDob.setText(dob);
                 //   tvGender.setText(gender);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else {

            }

        }
    }*/

    //***#***#***#***#***#***#***#***#***#***#***#***#***#***#***#***#***#***#
    /*public class SubmitData extends AsyncTask<String, String, Boolean> {
    //    JSONObject jsonResponse;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDailog();
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                JSONArray reqarr = new JSONArray();
                JSONObject reqObj = new JSONObject();

            //    reqObj.put("IMAGE",img64code);
             //   reqObj.put("ID_PROOF",idpprofSpinner.getSelectedItem().toString());
            //    reqObj.put("TRANSPORT_ARRANGEMENT",(cbTransportArran.isChecked()?"Y":"N"));
            //    reqObj.put("ACCOMMODATION",(cbAccomodation.isChecked()?"Y":"N"));
                reqObj.put("ID_CARD_TYPE",id_card);
                reqObj.put("TRANSPORTAION_ARRANGEMENT",(cbTransportArran.isChecked()?"Y":"N"));
                reqObj.put("ACCOMODATION_ARRANGEMENT",(cbAccomodation.isChecked()?"Y":"N"));
                reqObj.put("EVENT_REG_ID",getIntent().getStringExtra("EVENT_ID"));
                reqarr.put(reqObj);
                String response = HttpClient.SendHttpPost(Constant.GITADISTRIBUTION_ITERARY_CONFIRMATION_UPDATE, reqarr.toString());
            //    response = AssetJSONFile("gitaventview.json",GitaDistribution.this);
            //    jsonResponse = new JSONObject(response);
                Log.i("!!!Response", response);
                if (reqarr.getBoolean(Integer.parseInt("status"))) {
                    return true;
                } else
                    return false;
            } catch (JSONException e) {
                e.printStackTrace();
            } *//*catch (IOException e) {
                e.printStackTrace();
            }*//*
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (prsDlg.isShowing()) {
                prsDlg.dismiss();
            }

            if (aBoolean){
                Toast.makeText(GitaDistribution.this, "Updated Sucessfully", Toast.LENGTH_SHORT).show(); 
            }else {
                Toast.makeText(GitaDistribution.this, "Update Unsucessful", Toast.LENGTH_SHORT).show();
            }

        }
    }*/
    //***#***#***#***#***#***#***#***#***#***#***#***#***#***#***#***#***#***#
}
