package in.jivanmuktas.www.marg.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import in.jivanmuktas.www.marg.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    public ProgressDialog prsDlg;
    public MyApplication app;
    public int height, width;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MyApplication) getApplication();
        Display display = getWindowManager().getDefaultDisplay();
        height = display.getHeight();
        width = display.getWidth();
    }

    @Override
    public void onClick(View view) {

    }

    public void showProgressDailog() {
        prsDlg = new ProgressDialog(this);
        prsDlg.setMessage("Please wait...");
        prsDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prsDlg.setIndeterminate(true);
        prsDlg.setCancelable(false);
        prsDlg.show();
    }

    public void dismissProgressDialog() {
        if (prsDlg.isShowing()) {
            prsDlg.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_notification) {
            /*Intent intent = new Intent(getApplicationContext(), Notification.class);
            startActivity(intent);*/
            return true;
        }
        if (id == R.id.action_logout) {
            /*app.setSession(false);
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);*/
            return true;
        }
        if (id==R.id.action_home){
            /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);*/
            return true;
        }
        if (id==R.id.action_user_profile){
           /* Intent intent = new Intent(getApplicationContext(), ViewProfile.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);*/
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getCurrentdate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return  df.format(c.getTime());
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean f =  activeNetworkInfo != null && activeNetworkInfo.isConnected();
        if(f){
            return true;
        }else{
            CustomToast("You Device Doesn’t Have Internet Connectivity, Please Connect To Internet And Try Accessing The App");
            return  false;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
    //////////****************Get Current Date*******///////////
    public String CurrentDate(){
        int crrYear;
        int crrMonth;
        int crrDay;
        Calendar c=Calendar.getInstance();
        crrYear=c.get(Calendar.YEAR);
        crrMonth=c.get(Calendar.MONTH);
        crrMonth++;
        crrDay=c.get(Calendar.DAY_OF_MONTH);
        return crrDay+"-"+crrMonth+"-"+crrYear;
    }


    /////////************* Age Calculation Start*************/////////////
    public String CalculateAge(String dob) {
        String[] parts = dob.split("-");
        int startDay = Integer.parseInt(parts[0]);
        int startMonth = Integer.parseInt(parts[1]);
        int startYear = Integer.parseInt(parts[2]);

        parts = CurrentDate().split("-");
        int crrDay = Integer.parseInt(parts[0]);
        int crrMonth = Integer.parseInt(parts[1]);
        int crrYear = Integer.parseInt(parts[2]);
        
        int resYear;
        int resMonth;
        int resDay;

   ////////////Age/////////////////
        /////////Day calculation
        if (startDay>crrDay){
            resDay = (crrDay+30) - startDay;
            crrMonth--;
        }else {
            resDay = crrDay - startDay;
        }
        /////////Month calculation
        if (startMonth>crrMonth){
            resMonth = (crrMonth+12) - startMonth;
            crrYear--;
        }else {
            resMonth = crrMonth - startMonth;
        }
        //////////Year calculation
        resYear = crrYear - startYear;

        return (""+resYear);
    }

    /////////************* Age Calculation tvend*************/////////////

    /////////////////******* Custom SnackBar With Red Background  **********////////////
public void SnackbarRed(int layout, String message){
    Snackbar snackbar = Snackbar
            .make(findViewById(layout), message, Snackbar.LENGTH_LONG);
    snackbar.setActionTextColor(Color.RED);
    View sbView = snackbar.getView();
    sbView.setBackgroundColor(Color.RED);
    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
    textView.setTextColor(Color.WHITE);
    snackbar.show();
}
public void CustomToast(String text){
    Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
}

    ///////////// Set Edittext Focusable
    public void editTextFocus(EditText editText){
        editText.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
///////////////////////************* Custom Intent
    public void CustomIntent(Class intentClass){
        Intent intent=new Intent(getApplicationContext(),intentClass);
        startActivity(intent);
        finish();
    }
    ///////////////////////************* Custom Intent Without closing current Activity
    public void CustomOnlyIntent(Class intentClass){
        Intent intent=new Intent(getApplicationContext(),intentClass);
        startActivity(intent);

    }
    /////////////////////***********************Custom Spinner***********///////////
    public void CustomSpinner(Spinner spinner,int arratList){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),arratList, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
    //Email Validation Check
        public boolean mailValidationCk(String emailId){
            // Email Regex java
            String EMAIL_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
            // static Pattern object, since pattern is fixed
            Pattern pattern = Pattern.compile(EMAIL_REGEX);
            // non-static Matcher object because it's created from the input String
            Matcher matcher = pattern.matcher(emailId);

            return matcher.matches();
        }

    //************ Read Json File************
    public static String AssetJSONFile (String filename, Context context) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream file = manager.open(filename);
        byte[] formArray = new byte[file.available()];
        file.read(formArray);
        file.close();
        return new String(formArray);
    }
    // ****** Convert  image to base 64 code
    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.DEFAULT);
        //String imgString = Base64.encodeBase64URLSafeString(byteFormat);
        return imgString;
    }
}