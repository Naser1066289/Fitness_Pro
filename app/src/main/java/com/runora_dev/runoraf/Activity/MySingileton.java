package com.runora_dev.runoraf.Activity;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingileton {
    private static MySingileton mInstance;
    private RequestQueue requestQueue;
    private static Context mCtx;

    private MySingileton(Context context) {
        mCtx = context;
        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized MySingileton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MySingileton(context);
        }
        return mInstance;
    }

    public <T> void addRequestQueue(Request<T> request) {
        requestQueue.add(request);

    }
}
