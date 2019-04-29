package exeter.project.tobyreeve.execcessibility;

import android.app.Application;
import android.content.Context;

public class MyApp extends Application {
    private static MyApp mAppInstance=null;
    public static Context appContext;
    public static String serverIP = "192.168.43.122"; //129
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