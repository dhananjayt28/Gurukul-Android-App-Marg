package in.jivanmuktas.www.marg.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.jivanmuktas.www.marg.R;
import in.jivanmuktas.www.marg.adapter.IASCoachingAdapter;
import in.jivanmuktas.www.marg.constant.Constant;
import in.jivanmuktas.www.marg.model.IasCoaching;
import in.jivanmuktas.www.marg.singleton.VolleySingleton;

public class IASCoaching extends BaseActivity {
    private static final String TAG = "IASCoaching";
    //String title;
    //ListView ias_List;
    //String date[]={"2-12-17","3-12-17","4-12-17","4-12-14"};
    //String subject[]={"English","Hindi","Statics","Financial Accounting"};
     RecyclerView recyclerView;
     ArrayList<IasCoaching> arrayList = new ArrayList<>();
     IASCoachingAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iascoaching);
        Log.i("!!!Activity:", TAG);
        Toolbar toolbar = (Toolbar) findViewById(R.id.top_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*title = getIntent().getExtras().getString("TITLE");
        setTitle(title);*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        recyclerView = findViewById(R.id.ias_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

       // ias_List =  (ListView) findViewById(R.id.ias_List);
       // ias_List.setAdapter(new ListAdapter());

        showTimeSubject();
    }

/*public class ListAdapter extends BaseAdapter{

    @Override
    public int getCount() {
        //return date.length;
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
}*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);*/
        return false;
    }

    public void showTimeSubject(){
        showProgressDailog();
        final String url = Constant.GET_COACHING_DATA;
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject object) {
                        dismissProgressDialog();
                        Log.d("!!!Date Subject",object.toString());
                        try {
                            if(object.getString("status").equals("true")){
                                JSONArray jsonArray = object.getJSONArray("response");
                                for (int i=0;i<jsonArray.length();i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    Log.i("!!final",obj.toString());
                                    IasCoaching iasCoaching = new IasCoaching();
                                    iasCoaching.setCoaching_date(obj.getString("COACHING_DATE"));
                                    iasCoaching.setSubject_name(obj.getString("SUBJECT_NAME"));
                                    arrayList.add(iasCoaching);
                                }
                                adapter = new IASCoachingAdapter(arrayList,IASCoaching.this);
                                recyclerView.setAdapter(adapter);
                            }else {
                                Toast.makeText(IASCoaching.this, "Data NOT Found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgressDialog();
                Log.d("Error",error.toString());
            }
        });
        VolleySingleton.getInstance(this).addToRequestQueue(objectRequest);
    }

}
