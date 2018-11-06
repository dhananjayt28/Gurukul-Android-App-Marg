package in.jivanmuktas.www.marg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import in.jivanmuktas.www.marg.R;

public class Project extends BaseActivity {
    LinearLayout nrivityGurukulProj,workshopProj,gitaDistributionProj;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        Toolbar toolbar = (Toolbar) findViewById(R.id.top_toolbarproject);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title = getIntent().getExtras().getString("TITLE");
        setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nrivityGurukulProj=(LinearLayout) findViewById(R.id.nrivityGurukulProj);
        workshopProj=(LinearLayout)findViewById(R.id.workshopProj);
        gitaDistributionProj=(LinearLayout)findViewById(R.id.gitaDistributionProj);


        nrivityGurukulProj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.equals("FAQ")) {
                    Intent faq = new Intent(Project.this, FAQ.class);
                    faq.putExtra("TITLE","Nivritti Gurukul");
                    startActivity(faq);
                }else{
                    Intent faq = new Intent(Project.this, Guidelines.class);
                    faq.putExtra("TITLE","Nivritti Gurukul");
                    startActivity(faq);
                }

            }
        });

        workshopProj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.equals("FAQ")) {
                Intent faq = new Intent(Project.this, FAQ.class);
                faq.putExtra("TITLE","Workshop");
                startActivity(faq);
                }else{
                    Intent faq = new Intent(Project.this, Guidelines.class);
                    faq.putExtra("TITLE","Workshop");
                    startActivity(faq);
                }
            }
        });

        gitaDistributionProj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.equals("FAQ")) {
                Intent faq = new Intent(Project.this, FAQ.class);
                faq.putExtra("TITLE","Gita Distribution");
                startActivity(faq);
                }else{
                    Intent faq = new Intent(Project.this, Guidelines.class);
                    faq.putExtra("TITLE","Gita Distribution");
                    startActivity(faq);
                }
            }
        });
    }
}
