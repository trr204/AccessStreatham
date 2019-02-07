package exeter.project.tobyreeve.execcessibility;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyApp extends Application {
    private static MyApp mAppInstance=null;
    public static Context appContext;
    public static MyApp getInstance() {
        return mAppInstance;
    }
    public static MyApp get() {
        return get(appContext);
    }
    public static MyApp get(Context context) {
        return (MyApp) context.getApplicationContext();
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mAppInstance=this;
        appContext=getApplicationContext();

    }
}