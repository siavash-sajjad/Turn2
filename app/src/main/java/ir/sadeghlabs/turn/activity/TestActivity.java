package ir.sadeghlabs.turn.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import ir.sadeghlabs.turn.R;
import ir.sadeghlabs.turn.model.WebAppInterface;

public class TestActivity extends FragmentActivity {
    GoogleMap map;
    double lat;
    double lan;
    boolean flag = false;
    private WebView browser;
    private String url = "http://p30download.com/";
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_web_layout);

/*        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

        if(map!=null){
            map.setMyLocationEnabled(true);
        }*/

        initWebView();
    }

    private void initWebView(){
        browser = (WebView) findViewById(R.id.browser);
        WebSettings webSettings = browser.getSettings();
        webSettings.setJavaScriptEnabled(true);
        browser.addJavascriptInterface(new WebAppInterface(TestActivity.this, getSupportFragmentManager()), "Android");
        browser.loadUrl(url);
        browser.setWebViewClient(new WebViewClient() {
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressDialog = new ProgressDialog(TestActivity.this);
                progressDialog.setMessage("در حال بارگذاری...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressDialog.dismiss();
                super.onPageFinished(view, url);
            }
        });
    }
}
