package org.techtown.capture.intent;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

public class AppController {
    private static AppController mInstance;
    private RequestQueue mRequestQueue;
    private Context mCtx;

    public AppController(Context mCtx) {
        this.mCtx = mCtx;
        mRequestQueue = getmRequestQueue();
    }

    public RequestQueue getmRequestQueue() {
        if (mRequestQueue == null) {
            Cache cache = new DiskBasedCache(mCtx.getCacheDir(), 1024*1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public static synchronized AppController getmInstance(Context context) {
        if (mInstance == null) {
            mInstance = new AppController(context);
        }

        return mInstance;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        mRequestQueue.add(request);
    }
}
