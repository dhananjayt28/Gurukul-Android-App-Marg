package in.jivanmuktas.www.marg.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import in.jivanmuktas.www.marg.R;
import in.jivanmuktas.www.marg.activity.CreateScheduleActivity;
import in.jivanmuktas.www.marg.constant.Constant;
import in.jivanmuktas.www.marg.singleton.VolleySingleton;

import static in.jivanmuktas.www.marg.activity.BaseActivity.AssetJSONFile;

public class NivrittyListAdapter extends RecyclerView.Adapter<NivrittyListAdapter.ViewHolder> {
    // Declare Variables
    Context context;
    JSONArray responseArray;

    public NivrittyListAdapter(Context context, JSONArray responseArray) {
        this.context = context;
        this.responseArray = responseArray;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.nivritylist, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String startDate, endDate, female, male, event;
        final JSONObject object;
        try {
            object = responseArray.getJSONObject(position);
            startDate = object.getString("START_DATE");
            endDate = object.getString("END_DATE");
            female = object.getString("FEMALE");
            male = object.getString("MALE");
            event = object.getString("EVENT_ID");


        holder.report_header.setVisibility(View.GONE);
        holder.report_view.setVisibility(View.GONE);
        holder.dt.setText("July 2018");
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, CreateScheduleActivity.class);
                i.putExtra("PROJECT", "Nivritti Gurukul");
                i.putExtra("START_DATE", startDate);
                i.putExtra("END_DATE", endDate);
                i.putExtra("EVENT", event);
                context.startActivity(i);
            }
        });
            holder.requireVolunteers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.report_view.getVisibility() == View.GONE) {
                        holder.report_view.setVisibility(View.VISIBLE);
                        holder.report_header.setVisibility(View.VISIBLE);
                        final String url = Constant.GET_EVENT_CALENDAR_BREAKUP_DATA;
                        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject object) {
                                        try {
                                            if(object.getString("status").equals("true")){
                                                Log.d("",object.toString());
                                                JSONArray arr = object.getJSONArray("response");
                                                holder.report_view.removeAllViews();
                                                for (int i = 0; i < arr.length(); i++) {
                                                    JSONObject data = arr.getJSONObject(i);
                                                    View v = SetReportView(data.getString("DATE"), data.getString("EVENT_REGISTERED_MALE"), data.getString("EVENT_REGISTERED_FEMALE"));
                                                    holder.report_view.addView(v);
                                                }
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        VolleySingleton.getInstance(context).addToRequestQueue(objectRequest);

                        /*try {
                            String s = AssetJSONFile("req_report.json",context);
                            JSONObject obj = new JSONObject(s);
                            JSONArray arr = obj.getJSONArray("response");
                            holder.report_view.removeAllViews();
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject data = arr.getJSONObject(i);
                                View v = SetReportView(data.getString("DATE"), data.getString("EVENT_REGISTERED_MALE"), data.getString("EVENT_REGISTERED_FEMALE"));
                                holder.report_view.addView(v);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                    } else {
                        holder.report_view.setVisibility(View.GONE);
                        holder.report_header.setVisibility(View.GONE);
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return responseArray.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dt;
        CardView cardView;
        TextView requireVolunteers;
        LinearLayout report_header;
        LinearLayout report_view;

        public ViewHolder(View view) {
            super(view);
            dt = (TextView) view.findViewById(R.id.date);
            cardView = (CardView) view.findViewById(R.id.card1);
            requireVolunteers = (TextView) view.findViewById(R.id.requireVolunteers);
            report_header = (LinearLayout) view.findViewById(R.id.report_header);
            report_view = (LinearLayout) view.findViewById(R.id.report_view);

        }
    }
    public View SetReportView(String dt, String m, String f) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.report, null);
        TextView date = (TextView) v.findViewById(R.id.date);
        TextView male = (TextView) v.findViewById(R.id.male);
        TextView female = (TextView) v.findViewById(R.id.female);

        date.setText(dt);
        male.setText(m);
        female.setText(f);
        return v;
    }
}
