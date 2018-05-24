package in.jivanmuktas.www.marg.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class VolleyHandler {

    String mUrl,type;
    HashMap<String,String> params;
    Context ctx;
    int method;
    VolleyListener mListener;


    public VolleyHandler(Context ctx, String mUrl, HashMap<String,String> map, int method, VolleyListener mListener, String type)
    {
        this.ctx=ctx;
        this.mUrl=mUrl;
        this.params=map;
        this.method=method;
        this.mListener=mListener;
        this.type=type;

        Log.d("PARAMS", params.toString());
    }

    public void makeHttpReq()
    {
        StringRequest stringRequest = new StringRequest(method, mUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(ctx,response,Toast.LENGTH_LONG).show();
                        mListener.onResponse(response,type);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(ctx,error.toString(),Toast.LENGTH_LONG).show();
                        mListener.onErrorResponse(error);
                    }
                }){
            @Override
            protected Map<String,String> getParams(){

                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        requestQueue.add(stringRequest);
    }

}
