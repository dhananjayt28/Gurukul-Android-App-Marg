package in.jivanmuktas.www.marg.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import in.jivanmuktas.www.marg.R;

import java.io.File;

public class View6 extends AppCompatActivity {

    Spinner idpprofSpinner;
    LinearLayout gitadisLayout;
    ImageView picImg;
    private String userChoosenTask;
    File file;
    Uri uri;
    Intent CamIntent,GalIntent,CropIntent;
    final int RequestPermissionCode=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view6);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTopView6);
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

        ArrayAdapter<CharSequence> adapterPresonNo = ArrayAdapter.createFromResource(View6.this,R.array.itiesidpinner, R.layout.spinner_item);
        adapterPresonNo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        idpprofSpinner.setAdapter(adapterPresonNo);

        int permissionCheck = ContextCompat.checkSelfPermission(View6.this, Manifest.permission.CAMERA);
        if(permissionCheck == PackageManager.PERMISSION_DENIED) {
            RequestRuntimePermission();
        }
        picImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //////////////////////////////////////////
                final Dialog dialog = new Dialog(View6.this);
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
    }
    private void RequestRuntimePermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(View6.this, Manifest.permission.CAMERA))
            Toast.makeText(View6.this,"CAMERA permission allows us to access CAMERA app",Toast.LENGTH_SHORT).show();
        else
        {
            ActivityCompat.requestPermissions(View6.this,new String[]{Manifest.permission.CAMERA},RequestPermissionCode);
        }
    }

    public void view6(View view) {
        Snackbar snackbar = Snackbar
                .make(gitadisLayout, "Information Saved Successfully!", Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(getResources().getColor(R.color.colorTextLabel));
        snackbar.show();
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
                    Toast.makeText(View6.this,"Permission Granted",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(View6.this,"Permission Canceled",Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }
}
