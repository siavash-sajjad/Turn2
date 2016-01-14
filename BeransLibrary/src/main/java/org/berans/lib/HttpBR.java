package org.berans.lib;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class HttpBR extends AsyncTask<Void, Void, Void> {

    private String baseUrl = null;
    private String result = null;
    private OnFinishListener mOnFinishListener;
    private OnStartListener mOnStartListener;
    private OnNetworkErrorListener mOnNetworkErrorListener;
    private Context context;
    private String loadingMessage = null;
    private String loadingTitle = null;
    private ProgressDialog loadingDialog = null;
    private String getString = null;
    private String postString = null;

    public HttpBR(Context context){
        this.context = context;
    }
    public HttpBR(Context context, String url){
        this.context = context;
        setUrl(url);
    }

    public void parseGet(String... params){
        getString = parseRequest(params);
    }

    public void parsePost(String... params){
        postString = parseRequest(params);
    }

    public String parseRequest(String... params){
        String finalParams = "";
        try {
            for (String param : params) {
                String[] item = param.split("=",2);
                finalParams += (item.length > 1) ? "&" + item[0] + "=" + URLEncoder.encode(item[1], "UTF-8") : item[0] + "=";
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return finalParams.substring(1);
    }

    public String getHttpData(String baseUrl)
    {
        URL url;
        HttpURLConnection connection = null;
        try {
            if(getString != null){
                baseUrl += (baseUrl.indexOf("?") == -1) ? "?" : "&";
                baseUrl += getString;
            }
            //Create connection
            url = new URL(baseUrl);
            connection = (HttpURLConnection)url.openConnection();
            if(postString != null) {
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Length", "" + Integer.toString(postString.getBytes().length));
            } else {
                connection.setRequestMethod("GET");
            }
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Language", "fa-IR");

            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            if(postString != null) {
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                wr.writeBytes(postString);
                wr.flush();
                wr.close();
            }

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }
    }

    public void setUrl(String url){
        baseUrl = url;
    }

    public String getResult(){
        return result;
    }

    public void setLoading(String message, String title){
        loadingMessage = message;
        loadingTitle = title;
    }
    public void setLoading(String message){
        setLoading(message, "");
    }
    public void setLoading(){
        setLoading("در حال بارگذاری", "");
    }

    @Override
    protected Void doInBackground(Void... params) {
        result = getHttpData(baseUrl);
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(!networkCheck()){
            cancel(true);
            if(mOnNetworkErrorListener != null)
                mOnNetworkErrorListener.onNetworkError();
        }
        if (loadingMessage != null && !isCancelled()){
            loadingDialog = ProgressDialog.show(context, loadingTitle, loadingMessage, true);
        }
        if(mOnStartListener == null) return;
        mOnStartListener.onStart();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(!networkCheck()){
            cancel(true);
            if(mOnNetworkErrorListener != null)
                mOnNetworkErrorListener.onNetworkError();
            if (loadingMessage != null){
                loadingDialog = ProgressDialog.show(context, loadingTitle, loadingMessage, true);
            }
            return;
        }else {
            if (loadingDialog != null) {
                loadingDialog.dismiss();
            }
            if (mOnFinishListener == null) return;
            mOnFinishListener.onFinish(result);
        }
    }

    public boolean networkCheck() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    // InterFaces
    public interface OnFinishListener{
        void onFinish(String data);
    }

    public interface OnStartListener{
        void onStart();
    }

    public interface OnNetworkErrorListener{
        void onNetworkError();
    }

    public void setOnFinishListener (OnFinishListener l) {
        mOnFinishListener = l;
    }

    public void setOnStartListener (OnStartListener l) {
        mOnStartListener = l;
    }

    public void setOnNetworkErrorListener (OnNetworkErrorListener l){
        mOnNetworkErrorListener = l;
    }

}
