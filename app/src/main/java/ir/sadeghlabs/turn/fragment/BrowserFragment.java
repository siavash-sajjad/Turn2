package ir.sadeghlabs.turn.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ir.sadeghlabs.turn.R;
import ir.sadeghlabs.turn.model.Constant;
import ir.sadeghlabs.turn.model.WebAppInterface;

/**
 * Created by Siavash on 1/10/2016.
 */
public class BrowserFragment extends Fragment {
    private FragmentActivity context;
    private View layoutView;
    private WebView browser;
    private String url;
    private ProgressDialog progressDialog;



    @Override
    public void onAttach(Activity activity) {
        context = (FragmentActivity) activity;
        super.onAttach(activity);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_web_layout, null);

        url = getArguments().getString(Constant.URL_LINK);

        initWebView();

        return layoutView;
    }

    private void initWebView(){
        browser = (WebView) layoutView.findViewById(R.id.browser);
        WebSettings webSettings = browser.getSettings();
        webSettings.setJavaScriptEnabled(true);
        browser.addJavascriptInterface(new WebAppInterface(context, context.getSupportFragmentManager()), "Android");
        browser.loadUrl(url);
        browser.setWebViewClient(new WebViewClient() {
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressDialog = new ProgressDialog(context);
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
