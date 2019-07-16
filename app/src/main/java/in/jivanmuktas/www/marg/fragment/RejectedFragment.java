package in.jivanmuktas.www.marg.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.jivanmuktas.www.marg.R;
import in.jivanmuktas.www.marg.activity.MyApplication;
import in.jivanmuktas.www.marg.constant.Constant;
import in.jivanmuktas.www.marg.network.HttpGetHandler;
import in.jivanmuktas.www.marg.utils.ExpandableHeightListView;

public class RejectedFragment extends Fragment {
    // Store instance variables
    private String title;
    private int page;
    ExpandableHeightListView rejectList;
    ProgressDialog prsDlg;
    JSONObject jsonResponse;
    JSONArray responseArray;
    SwipeRefreshLayout swipeReject;
    // newInstance constructor for creating fragment with arguments
    public static RejectedFragment newInstance(int page, String title) {
        RejectedFragment fragmentFirst = new RejectedFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rejected, container, false);
        rejectList = (ExpandableHeightListView) view.findViewById(in.jivanmuktas.www.marg.R.id.rejectList);
        swipeReject = (SwipeRefreshLayout) view.findViewById(in.jivanmuktas.www.marg.R.id.swipeReject);
        swipeReject.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isNetworkAvailable()) {
                    new GetRejectEvent().execute("");
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isNetworkAvailable()) {
            new GetRejectEvent().execute("");
        }
    }

    public class GetRejectEvent extends AsyncTask<String, String, Boolean> {
        String id = MyApplication.getInstance().getUserId();        // to get the userId using instance method
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prsDlg = new ProgressDialog(getActivity());
            prsDlg.setMessage("Please wait...");
            prsDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prsDlg.setIndeterminate(true);
            prsDlg.setCancelable(false);
            prsDlg.show();
            rejectList.setAdapter(null);//Clear List View
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            HttpGetHandler handler = new HttpGetHandler();
            String response;
            try {
                response = handler.makeServiceCall(Constant.RejectEvent + id);
                Log.i("!!!RequestReject", Constant.RejectEvent + id);
                jsonResponse = new JSONObject(response);
                Log.i("!!!ResponseReject", response);
                if (jsonResponse.getBoolean("status")) {
                    responseArray = jsonResponse.getJSONArray("response");
                    return true;
                } else
                    return false;
            } catch (JSONException e) {
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
            swipeReject.setRefreshing(false);
            if (aBoolean) {
                ListAdapter Adapter = new ListAdapter();
                rejectList.setAdapter(Adapter);
            } else {
                NodataFound Adapter = new NodataFound();
                rejectList.setAdapter(Adapter);
            }
        }


        //***************************************************************************************
        public class ListAdapter extends BaseAdapter {
            @Override
            public int getCount() {
                return responseArray.length();
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
                final String EVENT_NAME, START_DATE, END_DATE, EVENT_TYPE,EVENT_REG_ID,EVENT_CALNDER_ID,EVENT_MASTER_ID,MESSAGE;
                JSONObject object;
                try {
                    object = responseArray.getJSONObject(position);
                    EVENT_TYPE = object.getString("EVENT_TYPE");
                    EVENT_NAME = object.getString("EVENT_NAME");
                    START_DATE = object.getString("START_DATE");
                    END_DATE = object.getString("END_DATE");
                    EVENT_REG_ID = object.getString("EVENT_REG_ID");
                    EVENT_CALNDER_ID = object.getString("EVENT_CALNDER_ID");
                    EVENT_MASTER_ID = object.getString("EVENT_MASTER_ID");
                    MESSAGE = object.getString("MESSAGE");

                    LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                    view = layoutInflater.inflate(in.jivanmuktas.www.marg.R.layout.rejectlist, null);
                    ImageView ivEvent = (ImageView) view.findViewById(in.jivanmuktas.www.marg.R.id.ivEvent);
                    TextView tvEvent = (TextView) view.findViewById(in.jivanmuktas.www.marg.R.id.tvEvent);
                    ImageView ivDate = (ImageView) view.findViewById(in.jivanmuktas.www.marg.R.id.ivDate);
                    TextView tvDate = (TextView) view.findViewById(in.jivanmuktas.www.marg.R.id.tvDate);
                    TextView tvReason = (TextView) view.findViewById(in.jivanmuktas.www.marg.R.id.tvReason);

                    tvEvent.setText(EVENT_NAME);
                    tvDate.setText(START_DATE+" - "+END_DATE);
                    tvReason.setText(MESSAGE);

                    if(EVENT_TYPE.equals("1")){
                        ivEvent.setImageDrawable(getResources().getDrawable(in.jivanmuktas.www.marg.R.drawable.gurukul));
                    }else if(EVENT_TYPE.equals("2")){
                        ivEvent.setImageDrawable(getResources().getDrawable(in.jivanmuktas.www.marg.R.drawable.workshop));
                    }else if(EVENT_TYPE.equals("3")){
                        ivEvent.setImageDrawable(getResources().getDrawable(in.jivanmuktas.www.marg.R.drawable.distribution));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return view;
            }
        }


        //***************************************************************************************
        public class NodataFound extends BaseAdapter {

            @Override
            public int getCount() {
                return 1;//1 for Only one time use
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

                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                view = layoutInflater.inflate(in.jivanmuktas.www.marg.R.layout.nodatafound, null);//If Position 0 is selected Msg will show
                return view;
            }
        }
    }
    //***************************************************************************************
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean f =  activeNetworkInfo != null && activeNetworkInfo.isConnected();
        if(f){
            return true;
        }else{
            Toast.makeText(getActivity(),"You Device Doesn’t Have Internet Connectivity, Please Connect To Internet And Try Accessing The App", Toast.LENGTH_SHORT).show();
            return  false;
        }
    }
}

