package in.jivanmuktas.www.marg.network;

import com.android.volley.VolleyError;

public interface VolleyListener {

    String onResponse(String response, String type);
    VolleyError onErrorResponse(VolleyError error);
}
