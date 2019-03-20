package in.jivanmuktas.www.marg.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import in.jivanmuktas.www.marg.R;
import in.jivanmuktas.www.marg.activity.BaseActivity;
import in.jivanmuktas.www.marg.activity.MainActivity;

public class QRscanner extends BaseActivity {

    private static final String TAG = "QRscanner";
    private CompoundBarcodeView barcodeView;
    private BeepManager beepManager;
    private boolean torchOn = false;
    MenuItem light;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setTitle("Scan QR Code");

        if(app.isSession()){
            CustomIntent(MainActivity.class);
            finish();
        }

        barcodeView = (CompoundBarcodeView) findViewById(R.id.barcode_scanner);

        barcodeView.setTorchListener(new CompoundBarcodeView.TorchListener() {
            @Override
            public void onTorchOn() {
                torchOn = true;
            }

            @Override
            public void onTorchOff() {
                torchOn = false;
            }
        });

        barcodeView.decodeContinuous(callback);

        beepManager = new BeepManager(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_capture, menu);
        light = menu.findItem(R.id.action_light);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_light) {
            toggleLight();
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleLight() {
        if (hasFlash())
            if (torchOn) {
                barcodeView.setTorchOff();
                light.setIcon(R.drawable.light_on);
            } else {
                barcodeView.setTorchOn();
                light.setIcon(R.drawable.light_off);
            }
    }

    @Override
    public void onResume() {
        barcodeView.resume();
        super.onResume();
    }

    @Override
    public void onPause() {
        barcodeView.pause();
        torchOn = false;
        super.onPause();
    }

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            //Do something with code result
            if (result.getText() != null) {
                barcodeView.pause();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                boolean shouldBeep = prefs.getBoolean("beep", false);
                beepManager.setBeepEnabled(shouldBeep);
                beepManager.setVibrateEnabled(false);
                if (shouldBeep)
                    beepManager.playBeepSoundAndVibrate();
                //((MainActivity)getActivity()).switchToFragment(FragmentGenerator.getFragment(result),false);
                Log.i("!!! ReqObject", result + "");
                //Toast.makeText(QRscanner.this, result + "", Toast.LENGTH_LONG).show();
    //            new Login().execute(result.toString());
                Login(result.toString());
            }
        }
        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    private boolean hasFlash() {
        return getApplication().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public void Login(String UserInfo){

        try {
            if(!UserInfo.equals("") ) {
                JSONObject jsonObject = new JSONObject(UserInfo);
                String userId = jsonObject.getString("EMAIL_ID");
                String password = jsonObject.getString("USER_ID");

                Log.i("!!!Activity",userId);

                if(userId.equalsIgnoreCase("deb@tts.com") && password.equalsIgnoreCase("106")){
                    app.setSession(true);
                    Intent intent = new Intent(QRscanner.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
