package exeter.project.tobyreeve.execcessibility;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestQueueHandler {
    private static RequestQueueHandler instance;
    private RequestQueue requestQueue;
    private static Context mCtx;

    private RequestQueueHandler() {
        requestQueue = getRequestQueue();
    }

    public static synchronized RequestQueueHandler getInstance() {
        if (instance == null) {
            instance = new RequestQueueHandler();
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(MyApp.get());
        }
        return requestQueue;
    }

    <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}