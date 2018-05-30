package pe.com.pucp.dti.sesiones.utils;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by DIA on 29/05/18.
 */

public class VolleyRequestQueue {
    private static VolleyRequestQueue instance = null;
    private static RequestQueue queue;

    protected VolleyRequestQueue(Context context) {
        queue = Volley.newRequestQueue(context);
    }
    public static VolleyRequestQueue getInstance(Context context) {
        if(instance == null) {
            instance = new VolleyRequestQueue(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue(){
        return queue;
    }
}
