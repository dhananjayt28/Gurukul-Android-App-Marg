package in.jivanmuktas.www.marg.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;


import in.jivanmuktas.www.marg.R;

public class ContactUs extends AppCompatActivity {

    CardView call1,call2,map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        Toolbar toolbar = (Toolbar) findViewById(R.id.top_toolbarContact);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Contact Us");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        call1 = (CardView) findViewById(R.id.call1);
        call2 = (CardView) findViewById(R.id.call2);
        map = (CardView) findViewById(R.id.map);

        call1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "07899912071", null));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(ContactUs.this,"Sorry! your device not support this features.",Toast.LENGTH_LONG).show();
                }
            }
        });
        call2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "07899242070", null));
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(ContactUs.this,"Sorry! your device not support this features.",Toast.LENGTH_LONG).show();
            }
            }
        });
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=37.0625,-95.677068"));
                startActivity(intent);
            }
        });
    }

}
