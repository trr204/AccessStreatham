package exeter.project.tobyreeve.execcessibility;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewFragment;

public class SimpleContentFragment extends WebViewFragment {
    private static final String KEY_FILE = "file";

    protected static SimpleContentFragment newInstance(String file) {
        SimpleContentFragment f = new SimpleContentFragment();
        Bundle args = new Bundle();
        args.putString(KEY_FILE, file);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = super.onCreateView(inflater, container, savedInstanceState);
        getWebView().getSettings().setJavaScriptEnabled(true);
        getWebView().getSettings().setSupportZoom(false);
        getWebView().getSettings().setBuiltInZoomControls(false);
        getWebView().loadUrl(getPage());
        return result;
    }

    private String getPage() {return getArguments().getString(KEY_FILE);}
}
