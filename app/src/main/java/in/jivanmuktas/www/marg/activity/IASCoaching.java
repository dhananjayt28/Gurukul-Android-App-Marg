package in.jivanmuktas.www.marg.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import in.jivanmuktas.www.marg.R;

public class IASCoaching extends BaseActivity {
    private static final String TAG = "IASCoaching";
    String title;
    ListView ias_List;
    String date[]={"2-12-17","3-12-17","4-12-17","4-12-14"};
    String subject[]={"English","Hindi","Statics","Financial Accounting"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iascoaching);
        Log.i("!!!Activity:", TAG);
        Toolbar toolbar = (Toolbar) findViewById(R.id.top_toolbar);
        setSupportActionBar(toolbar);

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

        ias_List =  (ListView) findViewById(R.id.ias_List);
        ias_List.setAdapter(new ListAdapter());
    }

public class ListAdapter extends BaseAdapter{

    @Override
    public int getCount() {
        return date.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.iaslistview, null);
        TextView t1 = (TextView) view.findViewById(R.id.date);
        TextView t2 = (TextView) view.findViewById(R.id.subject);

        try {
            String s1 = date[position];
            String s2 = subject[position];
            t1.setText(s1);
            t2.setText(s2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);*/
        return false;
    }
}
