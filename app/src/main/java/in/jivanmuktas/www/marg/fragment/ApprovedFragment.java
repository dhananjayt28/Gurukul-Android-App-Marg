package in.jivanmuktas.www.marg.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import in.jivanmuktas.www.marg.R;
import in.jivanmuktas.www.marg.activity.GitaDistribution;
import in.jivanmuktas.www.marg.activity.MyApplication;
import in.jivanmuktas.www.marg.activity.Nivritti;
import in.jivanmuktas.www.marg.activity.Workshop;
import in.jivanmuktas.www.marg.constant.Constant;
import in.jivanmuktas.www.marg.network.HttpGetHandler;
import in.jivanmuktas.www.marg.network.HttpPutHandler;
import in.jivanmuktas.www.marg.utils.ExpandableHeightListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static in.jivanmuktas.www.marg.activity.BaseActivity.AssetJSONFile;

public class ApprovedFragment extends Fragment {
    // Store instance variables
    ProgressDialog prsDlg;
    JSONObject jsonResponse;
    JSONArray responseArray;
    private String title;
    private int page;
    LinearLayout approvedLayout;
    ExpandableHeightListView approveList;
    SwipeRefreshLayout swipeApprove;

    // newInstance constructor for creating fragment with arguments
    public static ApprovedFragment newInstance(int page, String title) {
        ApprovedFragment fragmentFirst = new ApprovedFragment();
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
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);


        approvedLayout = (LinearLayout) view.findViewById(R.id.approvedLayout);
        swipeApprove = (SwipeRefreshLayout) view.findViewById(R.id.swipeApprove);
        approveList = (ExpandableHeightListView) view.findViewById(R.id.approveList);


        swipeApprove.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isNetworkAvailable()) {
                    new GetApproveEvent().execute();
                }
            }
        });
        /*cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), View1.class);
                intent.putExtra("KEY", "TEST");
                startActivity(intent);
                //getActivity().finish();
            }
        });

        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), View2.class);
                intent.putExtra("KEY", "TEST");
                startActivity(intent);
                //getActivity().finish();
            }
        });

        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), View3.class);
                intent.putExtra("KEY", "TEST");
                startActivity(intent);
                //getActivity().finish();
            }
        });
        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), View4.class);
                intent.putExtra("KEY", "TEST");
                startActivity(intent);
                //getActivity().finish();
            }
        });

        cardView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), View5.class);
                intent.putExtra("KEY", "TEST");
                startActivity(intent);
                //getActivity().finish();
            }
        });
        cardView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), View6.class);
                intent.putExtra("KEY", "TEST");
                startActivity(intent);
                //getActivity().finish();
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar.make(approvedLayout, "Your volunteering request cancel successfully.", Snackbar.LENGTH_LONG);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(getResources().getColor(R.color.colorFloatingButton));
                snackbar.show();
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), View1.class);
                intent.putExtra("KEY", "MODIFY");
                startActivity(intent);
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar.make(approvedLayout, "Sorry! You can not cancel request due to start day falls within 7 days.Please contact to the Admin further.", Snackbar.LENGTH_LONG);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(getResources().getColor(R.color.Red));
                snackbar.show();
            }
        });

        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), View2.class);
                intent.putExtra("KEY", "MODIFY");
                startActivity(intent);
            }
        });
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar.make(approvedLayout, "Sorry! You can not cancel approval if days are remaining less than 7.Please contact to the Admin.", Snackbar.LENGTH_LONG);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(getResources().getColor(R.color.Red));
                snackbar.show();
            }
        });

        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), View3.class);
                intent.putExtra("KEY", "MODIFY");
                startActivity(intent);
            }
        });*/

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isNetworkAvailable()) {
            new GetApproveEvent().execute();
        }
    }

    public class GetApproveEvent extends AsyncTask<String, String, Boolean> {
    String User_id = MyApplication.getInstance().getUserId();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prsDlg = new ProgressDialog(getActivity());
            prsDlg.setMessage("Please wait...");
            prsDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prsDlg.setIndeterminate(true);
            prsDlg.setCancelable(false);
            prsDlg.show();
            approveList.setAdapter(null);//Clear List View
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            HttpGetHandler handler = new HttpGetHandler();
            String response;
            try {
                response = handler.makeServiceCall(Constant.ApprovedEvent + User_id);
                //response = AssetJSONFile("ApprovedEvent.json",getActivity());
                Log.i("!!!RequestApproved", Constant.ApprovedEvent + User_id);
                jsonResponse = new JSONObject(response);
                Log.i("!!!ResponseApproved", response);
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
            swipeApprove.setRefreshing(false);
            if (aBoolean) {
                ListAdapter Adapter = new ListAdapter();
                approveList.setAdapter(Adapter);
            } else {
                NodataFound Adapter = new NodataFound();
                approveList.setAdapter(Adapter);
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
                final String EVENT_NAME, START_DATE, END_DATE, TODAY, EVENT_TYPE, EVENT_REG_ID, EVENT_CALNDER_ID, EVENT_MASTER_ID;
                JSONObject object;
                try {
                    object = responseArray.getJSONObject(position);
                    EVENT_TYPE = object.getString("EVENT_TYPE");
                    EVENT_NAME = object.getString("EVENT_NAME");
                    START_DATE = object.getString("START_DATE");
                    END_DATE = object.getString("END_DATE");
                    TODAY = object.getString("TODAY");
                    EVENT_REG_ID = object.getString("EVENT_REG_ID");
                    EVENT_CALNDER_ID = object.getString("EVENT_CALNDER_ID");
                    EVENT_MASTER_ID = object.getString("EVENT_MASTER_ID");

                    LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                    view = layoutInflater.inflate(R.layout.approvedlist, null);
                    ImageView ivEvent = (ImageView) view.findViewById(R.id.ivEvent);
                    TextView tvEvent = (TextView) view.findViewById(R.id.tvEvent);
                    ImageView ivDate = (ImageView) view.findViewById(R.id.ivDate);
                    TextView tvDate = (TextView) view.findViewById(R.id.tvDate);
                    LinearLayout btView = (LinearLayout) view.findViewById(R.id.btView);
                    Button cancel = (Button) view.findViewById(R.id.cancel);
                    Button modify = (Button) view.findViewById(R.id.modify);
                    CardView card = (CardView) view.findViewById(R.id.card);

                    tvEvent.setText(EVENT_NAME);
                    tvDate.setText(START_DATE+" - "+END_DATE);

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(daysBetween(StringToDate(TODAY),StringToDate(START_DATE)) <= 7){
                                Snackbar snackbar = Snackbar.make(approvedLayout, "Sorry! You can not cancel request due to start day falls within 7 days.Please contact to the Admin.", Snackbar.LENGTH_LONG);
                                View sbView = snackbar.getView();
                                sbView.setBackgroundColor(getResources().getColor(R.color.Red));
                                snackbar.show();
                            }else {
                                new CancelApproveEvent().execute(EVENT_REG_ID);
                            }
                        }
                    });

                    modify.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /*Intent intent=new Intent(getActivity(), View1.class);
                            intent.putExtra("KEY", "MODIFY");
                            intent.putExtra("EVENT_ID",EVENT_REG_ID);
                            startActivity(intent);*/
                            Snackbar snackbar = Snackbar.make(approvedLayout, "Work-in-Progress", Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            sbView.setBackgroundColor(getResources().getColor(R.color.Red));
                            snackbar.show();
                        }
                    });

                    if(EVENT_TYPE.equals("1")){
                        btView.setVisibility(View.VISIBLE);
                        ivEvent.setImageDrawable(getResources().getDrawable(R.drawable.gurukul));
                    }else if(EVENT_TYPE.equals("2")){
                        ivEvent.setImageDrawable(getResources().getDrawable(R.drawable.workshop));
                    }else if(EVENT_TYPE.equals("3")){
                        modify.setVisibility(View.GONE);
                        ivEvent.setImageDrawable(getResources().getDrawable(R.drawable.distribution));
                    }

                    card.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent;
                            if (EVENT_TYPE.equals("1")){
                                intent=new Intent(getActivity(), Nivritti.class);
                                intent.putExtra("EVENT_ID", EVENT_REG_ID);
                                startActivity(intent);
                            }else if (EVENT_TYPE.equals("2")){
                                intent=new Intent(getActivity(), Workshop.class);//Change line later
                                intent.putExtra("EVENT_ID", EVENT_REG_ID);
                                startActivity(intent);
                            }else if (EVENT_TYPE.equals("3")){
                                intent=new Intent(getActivity(), GitaDistribution.class);//Change line later
                                intent.putExtra("EVENT_ID", EVENT_REG_ID);
                                startActivity(intent);
                            }

                        }
                    });

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
                view = layoutInflater.inflate(R.layout.nodatafound, null);//If Position 0 is selected Msg will show
                return view;
            }
        }
    }
//********************************************************************

//********************************************************************
    public int daysBetween(Date entryDate, Date todayDate) {
        Calendar sDate = getDatePart(entryDate);
        Calendar eDate = getDatePart(todayDate);

        int daysBetween = 0;
        while (sDate.before(eDate)) {
            sDate.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        Log.i("!!!DaysBetween", String.valueOf(daysBetween));
        return daysBetween;
    }
    public static Calendar getDatePart(Date date){
        Calendar cal = Calendar.getInstance();       // get calendar instance
        cal.setTime(date);                           //
        cal.set(Calendar.HOUR_OF_DAY, 0);            // set hour to midnight
        cal.set(Calendar.MINUTE, 0);                 // set minute in hour
        cal.set(Calendar.SECOND, 0);                 // set second in minute
        cal.set(Calendar.MILLISECOND, 0);            // set millisecond in second
        return cal;                                  // return the date part
    }
    public Date StringToDate(String date){
        Date d=null;
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); // here set the pattern as you date in string was containing like month/date/year
            d = sdf.parse(date);
        }catch(ParseException ex){
            // handle parsing exception if date string was different from the pattern applying into the SimpleDateFormat contructor
        }
        Log.i("!!!Date", String.valueOf(d));
        return d;
    }
    //***************************************************************************************

    public class CancelApproveEvent extends AsyncTask<String, String, Boolean> {
        String USER_ID = MyApplication.getInstance().getUserId();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prsDlg = new ProgressDialog(getActivity());
            prsDlg.setMessage("Please wait...");
            prsDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prsDlg.setIndeterminate(true);
            prsDlg.setCancelable(false);
            prsDlg.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            String EVENT_ID = strings[0];
            String response;
            JSONObject json;
            JSONObject reqObj = new JSONObject();

            try {
                reqObj.put("USER_ID",USER_ID);
                reqObj.put("EVENT_REG_ID",EVENT_ID);
                response = HttpPutHandler.SendHttpPut(Constant.CANCEL_EVENT,reqObj.toString());//Using Put Method

                json = new JSONObject(response);
                Log.i("!!!ResApprovedCancel", response);
                if (json.getBoolean("status")) {
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

            if (aBoolean){
                Snackbar snackbar = Snackbar.make(approvedLayout, "Your volunteering request cancel successfully.", Snackbar.LENGTH_LONG);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(getResources().getColor(R.color.colorFloatingButton));
                snackbar.show();
                if (isNetworkAvailable()) {
                    new GetApproveEvent().execute();
                }
            }else {
                Snackbar snackbar = Snackbar.make(approvedLayout, "Your volunteering request cancel failed.", Snackbar.LENGTH_LONG);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(getResources().getColor(R.color.Red));
                snackbar.show();

            }

        }

    }
    //***************************************************************************************
    public boolean isNetworkAvailable() {// Network Check
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