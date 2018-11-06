package in.jivanmuktas.www.marg.fragment;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;

import in.jivanmuktas.www.marg.R;
import in.jivanmuktas.www.marg.activity.CreateScheduleActivity;
import in.jivanmuktas.www.marg.constant.Constant;
import in.jivanmuktas.www.marg.network.HttpGetHandler;
import in.jivanmuktas.www.marg.utils.ExpandableHeightListView;

import static in.jivanmuktas.www.marg.activity.BaseActivity.AssetJSONFile;

public class AvailableFragment extends Fragment {
    //***************************************************************************************
    // Store instance variables
    private String title;
    private int page;
    JSONObject jsonResponse;
    JSONArray responseArray;
    ProgressDialog prsDlg;
    ExpandableHeightListView  workshopAvalList, gitaAvalList;
    LinearLayout nivrityAvalList;
    ImageView serchNivrity, serchWorkshop;
    LinearLayout availableLayout;
    CardView nrivityGurukul, workshop, gitaDistribution;
    int x = 0, y = 0, z = 0;
    LinearLayout nivlayout, worklayout, gitaLayout;

    private SimpleDateFormat dateFormatter;
    private DatePickerDialog datePickerDialog;
    Spinner avlilavlemon, avlilavleyear, workshopyear, workshopmon, workshopstate;
    String ckSearch = "";

    //***************************************************************************************
    // newInstance constructor for creating fragment with arguments
    public static AvailableFragment newInstance(int page, String title) {
        AvailableFragment fragmentFirst = new AvailableFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    //***************************************************************************************
    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    //***************************************************************************************
    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_available, container, false);

        availableLayout = (LinearLayout) view.findViewById(R.id.availableLayout);
        serchNivrity = (ImageView) view.findViewById(R.id.serchNivrity);
        serchWorkshop = (ImageView) view.findViewById(R.id.serchWorkshop);
        nivrityAvalList = (LinearLayout) view.findViewById(R.id.nivrityAvalList);
        workshopAvalList = (ExpandableHeightListView) view.findViewById(R.id.workshopAvalList);
        gitaAvalList = (ExpandableHeightListView) view.findViewById(R.id.gitaAvalList);

        nivlayout = (LinearLayout) view.findViewById(R.id.nivlayout);
        worklayout = (LinearLayout) view.findViewById(R.id.worklayout);
        gitaLayout = (LinearLayout) view.findViewById(R.id.gitaLayout);

        nivlayout.setVisibility(View.GONE);
        worklayout.setVisibility(View.GONE);
        gitaLayout.setVisibility(View.GONE);

        nrivityGurukul = (CardView) view.findViewById(R.id.nrivityGurukul);
        workshop = (CardView) view.findViewById(R.id.workshop);
        gitaDistribution = (CardView) view.findViewById(R.id.gitaDistribution);

        avlilavlemon = (Spinner) view.findViewById(R.id.avlilavlemon);
        avlilavleyear = (Spinner) view.findViewById(R.id.avlilavleyear);
        workshopyear = (Spinner) view.findViewById(R.id.workshopyear);
        workshopmon = (Spinner) view.findViewById(R.id.workshopmon);
        workshopstate = (Spinner) view.findViewById(R.id.workshopstate);
        final ArrayAdapter<CharSequence> adapterMonth = ArrayAdapter.createFromResource(getActivity(), R.array.monList, android.R.layout.simple_spinner_item);
        adapterMonth.setDropDownViewResource(R.layout.spinner_dropdown_item);

        ArrayAdapter<CharSequence> adapterYear = ArrayAdapter.createFromResource(getActivity(), R.array.yearList, android.R.layout.simple_spinner_item);
        adapterYear.setDropDownViewResource(R.layout.spinner_dropdown_item);

        ArrayAdapter<CharSequence> adapterState = ArrayAdapter.createFromResource(getActivity(), R.array.stateList, android.R.layout.simple_spinner_item);
        adapterState.setDropDownViewResource(R.layout.spinner_dropdown_item);

        avlilavlemon.setAdapter(adapterMonth);
        avlilavleyear.setAdapter(adapterYear);
        workshopyear.setAdapter(adapterYear);
        workshopmon.setAdapter(adapterMonth);
        workshopstate.setAdapter(adapterState);
        //***************************************************************************************
        nrivityGurukul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (x == 0) {
                    nivlayout.setVisibility(View.VISIBLE);
                    if (isNetworkAvailable()) {
                        new GetAvailableEvent().execute("1");
                    }
                    x++;
                    worklayout.setVisibility(View.GONE);
                    y = 0;
                    gitaLayout.setVisibility(View.GONE);
                    y = 0;
                } else {
                    nivlayout.setVisibility(View.GONE);
                    x--;
                }
            }
        });
        //***************************************************************************************
        workshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (y == 0) {
                    nivlayout.setVisibility(View.GONE);
                    x = 0;
                    worklayout.setVisibility(View.VISIBLE);
                    if (isNetworkAvailable()) {
                        new GetAvailableEvent().execute("2");
                    }
                    y++;
                    gitaLayout.setVisibility(View.GONE);
                    z = 0;
                } else {
                    worklayout.setVisibility(View.GONE);
                    y--;
                }

            }
        });
        //***************************************************************************************
        gitaDistribution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (z == 0) {
                    nivlayout.setVisibility(View.GONE);
                    x = 0;
                    worklayout.setVisibility(View.GONE);
                    y = 0;
                    gitaLayout.setVisibility(View.VISIBLE);
                    if (isNetworkAvailable()) {
                        new GetAvailableEvent().execute("3");
                    }
                    z++;
                } else {
                    gitaLayout.setVisibility(View.GONE);
                    z--;
                }

            }
        });
        //***************************************************************************************
        serchNivrity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (avlilavleyear.getSelectedItemPosition() == 0) {
                    SnackbarRed(availableLayout, "Please Select Year");
                } else if (avlilavlemon.getSelectedItemPosition() == 0) {
                    SnackbarRed(availableLayout, "Please Select Month");
                } else if (avlilavleyear.getSelectedItemPosition() > 0 && avlilavlemon.getSelectedItemPosition() > 0) {
                    if (isNetworkAvailable()) {
                        new GetAvailableEvent().execute("1&year=" + avlilavleyear.getSelectedItem() + "&month=" + avlilavlemon.getSelectedItemPosition());
                        ckSearch = "GURUKUL";
                    }
                }
            }
        });
        //***************************************************************************************
        serchWorkshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (workshopyear.getSelectedItemPosition() == 0) {
                    SnackbarRed(availableLayout, "Please Select Year");
                } else if (workshopmon.getSelectedItemPosition() == 0) {
                    SnackbarRed(availableLayout, "Please Select Month");
                } else if (workshopstate.getSelectedItemPosition() == 0) {
                    SnackbarRed(availableLayout, "Please Select State");
                } else if (workshopyear.getSelectedItemPosition() > 0 && workshopmon.getSelectedItemPosition() > 0 && workshopstate.getSelectedItemPosition() > 0) {
                    if (isNetworkAvailable()) {
                        new GetAvailableEvent().execute("2&year=" + workshopyear.getSelectedItem().toString() + "&month=" + workshopmon.getSelectedItemPosition() + "&stateid=" + workshopstate.getSelectedItemPosition());
                        ckSearch = "WORKSHOP";
                    }
                }
            }
        });
        //***************************************************************************************
        return view;
    }

    //***************************************************************************************
    public class GetAvailableEvent extends AsyncTask<String, String, Boolean> {

        String ckEvent;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prsDlg = new ProgressDialog(getActivity());
            prsDlg.setMessage("Please wait...");
            prsDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prsDlg.setIndeterminate(true);
            prsDlg.setCancelable(false);
            prsDlg.show();

            nivrityAvalList.removeAllViews();//Clear List View
            workshopAvalList.setAdapter(null);
            gitaAvalList.setAdapter(null);
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            ckEvent = strings[0];//SubLink
            Log.i("!!!SubLink", ckEvent);
            HttpGetHandler handler = new HttpGetHandler();
            String response;
            try {
                response = handler.makeServiceCall(Constant.AvailableEvent + ckEvent);
                Log.i("!!!RequestAvailable", Constant.AvailableEvent + ckEvent);
                jsonResponse = new JSONObject(response);
                Log.i("!!!ResponseAvailable", response);
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
            if (aBoolean) {
                if (ckEvent.equals("1")) {//// Set Nivrity List
                    SetNivrittyList();
                }
                if (ckEvent.equals("2")) {//// Set WorkShop List
                    WorkshopListAdapter Adapter = new WorkshopListAdapter();
                    workshopAvalList.setAdapter(Adapter);
                }
                if (ckEvent.equals("3")) {//// Set GitaDistribution List
                    GitaListAdapter Adapter = new GitaListAdapter();
                    gitaAvalList.setAdapter(Adapter);
                }
                if (ckSearch.equals("GURUKUL")) {///Search for Gurukul
                    SetNivrittyList();
                }
                if (ckSearch.equals("WORKSHOP")) {///Search for Workshop
                    WorkshopListAdapter Adapter = new WorkshopListAdapter();
                    workshopAvalList.setAdapter(Adapter);
                }
            } else {
                NodataFound Adapter = new NodataFound();
                if (ckEvent.equals("1")) {
                    SetNoDataFound();
                } else if (ckEvent.equals("2")) {
                    workshopAvalList.setAdapter(Adapter);// Set list view no data found
                } else if (ckEvent.equals("3")) {
                    gitaAvalList.setAdapter(Adapter);
                } else if (ckSearch.equals("GURUKUL")) {
                    SetNoDataFound();
                } else if (ckSearch.equals("WORKSHOP")) {
                    workshopAvalList.setAdapter(Adapter);
                }
            }
        }
    }

    //***************************************************************************************
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean f = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        if (f) {
            return true;
        } else {
            Toast.makeText(getActivity(), "You Device Doesn’t Have Internet Connectivity, Please Connect To Internet And Try Accessing The App", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    //***************************************************************************************
    ////////// It is Custom Red Background Snake Bar
    public void SnackbarRed(LinearLayout layout, String message) {
        Snackbar snackbar = Snackbar
                .make(layout, message, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(Color.RED);
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    //***************************************************************************************
    public void SetNivrittyList() {
            JSONObject object;
            try {
                for (int i=0; i<responseArray.length();i++) {
                    object = responseArray.getJSONObject(i);
                    final String startDate, endDate, female, male, event;
                    startDate = object.getString("START_DATE");
                    endDate = object.getString("END_DATE");
                    female = object.getString("FEMALE");
                    male = object.getString("MALE");
                    event = object.getString("EVENT_ID");
                    LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                    View view = layoutInflater.inflate(R.layout.nivritylist, null);

                    TextView dt = (TextView) view.findViewById(R.id.date);
                    //TextView f = (TextView) view.findViewById(R.id.female);
                    //TextView m = (TextView) view.findViewById(R.id.male);
                    TextView register = (TextView) view.findViewById(R.id.register);
                    final TextView requireVolunteers = (TextView) view.findViewById(R.id.requireVolunteers);
                    requireVolunteers.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0);
                    final LinearLayout report_header = (LinearLayout) view.findViewById(R.id.report_header);
                    report_header.setVisibility(View.GONE);
                    final LinearLayout report_view = (LinearLayout) view.findViewById(R.id.report_view);
                    report_view.setVisibility(View.GONE);

                    //dt.setText(startDate + "   -   " + endDate);
                    dt.setText("July 2018");
                    //f.setText(female);
                    // m.setText(male);
                    register.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(getActivity(), CreateScheduleActivity.class);
                            i.putExtra("PROJECT", "Nivritti Gurukul");
                            i.putExtra("START_DATE", startDate);
                            i.putExtra("END_DATE", endDate);
                            i.putExtra("EVENT", event);
                            startActivity(i);
                        }
                    });
                    requireVolunteers.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            requireVolunteers.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_up, 0);
                            if (report_view.getVisibility() == View.GONE) {
                                report_view.setVisibility(View.VISIBLE);
                                report_header.setVisibility(View.VISIBLE);

                                try {
                                    String s = AssetJSONFile("req_report.json", getActivity());
                                    JSONObject obj = new JSONObject(s);
                                    JSONArray arr = obj.getJSONArray("response");
                                    report_view.removeAllViews();
                                    for (int i = 0; i < arr.length(); i++) {
                                        JSONObject data = arr.getJSONObject(i);
                                        View v = SetReportView(data.getString("DATE"), data.getString("MALE_VOLUNTEERS_REQUIRED"), data.getString("FEMALE_VOLUNTEERS_REQUIRED"));
                                        report_view.addView(v);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                requireVolunteers.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0);
                                report_view.setVisibility(View.GONE);
                                report_header.setVisibility(View.GONE);
                            }
                        }
                    });
                    nivrityAvalList.addView(view);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        public void SetNoDataFound() {
        nivrityAvalList.removeAllViews();
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                View view = layoutInflater.inflate(R.layout.nodatafound, null);
                nivrityAvalList.addView(view);
    }

    public View SetReportView(String dt, String m, String f) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View v = layoutInflater.inflate(R.layout.report, null);
        TextView date = (TextView) v.findViewById(R.id.date);
        TextView male = (TextView) v.findViewById(R.id.male);
        TextView female = (TextView) v.findViewById(R.id.female);

        date.setText(dt);
        male.setText(m);
        female.setText(f);
        return v;
    }
    //***************************************************************************************
    public class WorkshopListAdapter extends BaseAdapter {
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
            final String startDate, endDate, holiDay, event;
            JSONObject object;
            try {
                object = responseArray.getJSONObject(position);
                startDate = object.getString("START_DATE");
                endDate = object.getString("END_DATE");
                holiDay = object.getString("HOLY_START_DATE") + "   -   " + object.getString("HOLY_END_DATE");
                event = object.getString("EVENT_ID");
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                view = layoutInflater.inflate(R.layout.workshoplist, null);
                TextView dt = (TextView) view.findViewById(R.id.date);
                TextView holiday = (TextView) view.findViewById(R.id.holiday);
                CardView cardView = (CardView) view.findViewById(R.id.card2);
                dt.setText(startDate + "   -   " + endDate);
                holiday.setText(holiDay);

                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), CreateScheduleActivity.class);
                        i.putExtra("PROJECT", "Workshop");
                        i.putExtra("START_DATE", startDate);
                        i.putExtra("END_DATE", endDate);
                        i.putExtra("EVENT", event);
                        startActivity(i);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return view;
        }
    }

    //***************************************************************************************
    public class GitaListAdapter extends BaseAdapter {
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
            final String startDate, endDate, location, message, event;
            JSONObject object;
            try {
                object = responseArray.getJSONObject(position);
                startDate = object.getString("START_DATE");
                endDate = object.getString("END_DATE");
                location = object.getString("LOCATION");
                message = object.getString("MESSAGE");
                event = object.getString("EVENT_ID");
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                view = layoutInflater.inflate(R.layout.gitadistlist, null);
                TextView dt = (TextView) view.findViewById(R.id.date);
                TextView tvlocation = (TextView) view.findViewById(R.id.location);
                CardView cardView = (CardView) view.findViewById(R.id.card3);

                dt.setText(startDate + "   -   " + endDate);
                tvlocation.setText(location);
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), CreateScheduleActivity.class);
                        i.putExtra("PROJECT", "Gita Distribution");
                        i.putExtra("START_DATE", startDate);
                        i.putExtra("END_DATE", endDate);
                        i.putExtra("MESSAGE", message);
                        i.putExtra("EVENT", event);
                        startActivity(i);
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
