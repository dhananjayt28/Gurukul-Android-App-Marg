package in.jivanmuktas.www.marg.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import in.jivanmuktas.www.marg.R;

public class View5 extends AppCompatActivity {
    ImageView imgItie;
    Spinner topicItie;
    TextInputLayout modiReasonItie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view5);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTopView5);
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

        imgItie = (ImageView) findViewById(R.id.imgItie);
        topicItie = (Spinner) findViewById(R.id.topicItie);
        modiReasonItie = (TextInputLayout)findViewById(R.id.modiReasonItie);
        modiReasonItie.setVisibility(View.GONE);

        ArrayAdapter<CharSequence> adapterPresonNo = ArrayAdapter.createFromResource(View5.this, R.array.itiespinner, android.R.layout.simple_spinner_item);
        adapterPresonNo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        topicItie.setAdapter(adapterPresonNo);

        topicItie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 1){
                    imgItie.setImageResource(R.drawable.check_icon);
                    modiReasonItie.setVisibility(View.GONE);
                }else if(position == 2){
                    imgItie.setImageResource(R.drawable.bullet);
                    modiReasonItie.setVisibility(View.VISIBLE);
                }else {
                    imgItie.setImageResource(R.drawable.bullet);
                    modiReasonItie.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void submitview5(View view) {
        finish();
    }

    public void topiclinkItie(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://drive.google.com/open?id=1rWzEN1IPfTN5OdVd9DleZmrXgp3DhPcXS25hCGjAUdQ"));
        startActivity(i);
    }
}
