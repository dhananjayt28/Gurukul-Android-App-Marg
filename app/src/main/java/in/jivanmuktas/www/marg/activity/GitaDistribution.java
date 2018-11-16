package in.jivanmuktas.www.marg.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import in.jivanmuktas.www.marg.R;
import in.jivanmuktas.www.marg.constant.Constant;
import in.jivanmuktas.www.marg.network.HttpClient;
import in.jivanmuktas.www.marg.network.HttpGetHandler;
import in.jivanmuktas.www.marg.singleton.VolleySingleton;

public class GitaDistribution extends BaseActivity {
    String EVENT_ID;
    Spinner idpprofSpinner;
    LinearLayout gitadisLayout;
    ImageView picImg;
    private String userChoosenTask;
    File file;
    Uri uri;
    Intent CamIntent,GalIntent,CropIntent;
    final int RequestPermissionCode=1;
    TextView tvEventDate,tvNote,tvName,tvDob,tvGender;
    CheckBox cbTransportArran,cbAccomodation;
    String img64code="";
    Button submit;
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
        gitadisLayout = (LinearLayout) findViewById(R.id.gitadisLayout);
        picImg = (ImageView) findViewById(R.id.picImg);
        tvEventDate = (TextView) findViewById(R.id.tvEventDate);
        tvNote = (TextView) findViewById(R.id.tvNote);
        tvName = (TextView) findViewById(R.id.tvName);
        tvDob = (TextView) findViewById(R.id.tvDob);
        tvGender = (TextView) findViewById(R.id.tvGender);
        cbTransportArran = (CheckBox) findViewById(R.id.cbTransportArran);
        cbAccomodation = (CheckBox) findViewById(R.id.cbAccomodation);
        submit = (Button) findViewById(R.id.submit);

        ArrayAdapter<CharSequence> adapterPresonNo = ArrayAdapter.createFromResource(GitaDistribution.this,R.array.itiesidpinner, R.layout.spinner_item);
        adapterPresonNo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        idpprofSpinner.setAdapter(adapterPresonNo);

        int permissionCheck = ContextCompat.checkSelfPermission(GitaDistribution.this, Manifest.permission.CAMERA);
        if(permissionCheck == PackageManager.PERMISSION_DENIED) {
            RequestRuntimePermission();
        }
        picImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //////////////////////////////////////////
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
                        userChoosenTask="Take Photo";
                        CameraOpen();
                        dialog.dismiss();
                    }
                });
                choose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userChoosenTask="Choose from Library";
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

            }
        });
        //new GetAllData().execute();
        GetAllData();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar
                        .make(gitadisLayout, "Information Saved Successfully!", Snackbar.LENGTH_LONG);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(getResources().getColor(R.color.colorTextLabel));
                snackbar.show();

                /*if (isValid()){
                    new SubmitData().execute();
                }*/
            }
        });
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
                                idpprofSpinner.getSelectedItem().toString();

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
                    String projectName = object.getString("PROJECT_NAME");
                    String startDate = object.getString("START_DATE");
                    String endDate = object.getString("END_DATE");
                    String note = object.getString("NOTE");
                    String name = object.getString("NAME");
                    String dob = object.getString("DOB");
                    String gender = object.getString("GENDER");

                    tvEventDate.setText(startDate+" To "+endDate);
                    tvNote.setText(note);
                    tvName.setText(name);
                    tvDob.setText(dob);
                    tvGender.setText(gender);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else {

            }

        }
    }

    //***#***#***#***#***#***#***#***#***#***#***#***#***#***#***#***#***#***#
    public class SubmitData extends AsyncTask<String, String, Boolean> {
        JSONObject jsonResponse;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDailog();
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                JSONObject reqObj = new JSONObject();

                reqObj.put("IMAGE",img64code);
                reqObj.put("ID_PROOF",idpprofSpinner.getSelectedItem().toString());
                reqObj.put("TRANSPORT_ARRANGEMENT",(cbTransportArran.isChecked()?"Y":"N"));
                reqObj.put("ACCOMMODATION",(cbAccomodation.isChecked()?"Y":"N"));
                String response = HttpClient.SendHttpPost("", reqObj.toString());
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


            }else {

            }

        }
    }
    //***#***#***#***#***#***#***#***#***#***#***#***#***#***#***#***#***#***#
}
