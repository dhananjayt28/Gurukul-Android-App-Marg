package in.jivanmuktas.www.marg.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;

import in.jivanmuktas.www.marg.R;
import in.jivanmuktas.www.marg.activity.BaseActivity;
import in.jivanmuktas.www.marg.activity.MainActivity;

public class LoginViaQRScanner extends BaseActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_CAMERA_PERMISSION =1;
    private static final int REQUEST_READ_PERMISSION = 2;
    private static final int SELECT_IMAGE = 3;
    private BarcodeDetector detector;
    private Uri imageUri;
    TextView scanQr,scanQrFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_via_qrscanner);

        if(app.isSession()){
            CustomIntent(MainActivity.class);
            finish();
        }

        detector = new BarcodeDetector.Builder(getApplicationContext())
                .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                .build();

        scanQr = (TextView) findViewById(R.id.scanQr);
        scanQrFile = (TextView) findViewById(R.id.scanQrFile);
        scanQr.setOnClickListener(this);
        scanQrFile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.scanQr:
                ActivityCompat.requestPermissions(LoginViaQRScanner.this, new
                        String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                break;
            case R.id.scanQrFile:
                ActivityCompat.requestPermissions(LoginViaQRScanner.this, new
                        String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_PERMISSION);
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(LoginViaQRScanner.this, QRscanner.class));
                } else {
                    Toast.makeText(LoginViaQRScanner.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_READ_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ScanQRfromFile();// Scan file and get QR code
                } else {
                    Toast.makeText(LoginViaQRScanner.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private void ScanQRfromFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = decodeBitmapUri(this, imageUri);

                if (detector.isOperational() && bitmap != null) {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<Barcode> barcodes = detector.detect(frame);
                    /*for (int index = 0; index < barcodes.size(); index++) {
                        Barcode code = barcodes.valueAt(index);
                        //scanResults.setText(scanResults.getText() + code.displayValue + "\n");
                        Toast.makeText(LoginActivity.this,code.displayValue + "\n",Toast.LENGTH_LONG).show();
                    }*/

                    if (barcodes.size() == 0) {
                        Toast.makeText(LoginViaQRScanner.this,"Scan Failed: Found nothing to scan", Toast.LENGTH_LONG).show();
                    }else{
                        if(isNetworkAvailable()) {
                            Barcode code = barcodes.valueAt(0);
                            Login(code.displayValue);
                        }
                    }
                } else {
                    Toast.makeText(LoginViaQRScanner.this,"Could not set up the detector!", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Failed to load Image", Toast.LENGTH_SHORT).show();
                Log.e(TAG, e.toString());
            }
        }
    }
    private Bitmap decodeBitmapUri(Context ctx, Uri uri) throws FileNotFoundException {
        int targetW = 600;
        int targetH = 600;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(uri), null, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeStream(ctx.getContentResolver()
                .openInputStream(uri), null, bmOptions);
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
                        Intent intent = new Intent(LoginViaQRScanner.this, MainActivity.class);
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
