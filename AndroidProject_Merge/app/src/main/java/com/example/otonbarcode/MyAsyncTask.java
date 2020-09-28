package com.example.otonbarcode;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

// POST の場合
// import java.io.BufferedWriter;
// import java.io.OutputStream;
// import java.io.OutputStreamWriter;

// doInBackground の引数の型: String
// publishProgress の引数の型: Void (今回は使用しません)
// doInBackground の返り値の型: String

public class MyAsyncTask extends AsyncTask<String, Void, String> {

    // ロガーのタグ
    private static final String TAG = "MyAsyncTask";

    // UI スレッドから操作するビュー
    //private TextView textView;

    public MyAsyncTask(Context context) {
        // 本メソッドは UI スレッドで処理されます。
        super();
        MainActivity mainActivity = (MainActivity)context;
        // textView = (TextView)mainActivity.findViewById(R.id.myTextView);
    }

    @Override
    protected String doInBackground(String... params) {

        // 本メソッドは background 用のスレッドで処理されます。
        // そのため、UI のビューを操作してはいけません。

        // Java の文字列連結では StringBuilder を利用します。
        // https://www.qoosky.io/techs/05a157a3e0
        StringBuilder sb = new StringBuilder();

        // finally 内で利用するため try の前に宣言します。
        InputStream inputStream = null;
        HttpsURLConnection connection = null;

        try {
            disableSSLCertificateChecking();

            // URL 文字列をセットします。
            URL url = new URL(params[0]);
            connection = (HttpsURLConnection)url.openConnection();
            connection.setConnectTimeout(3000); // タイムアウト 3 秒
            connection.setReadTimeout(3000);

            // GET リクエストの実行
            connection.setRequestMethod("GET");
            connection.connect();

            // // POST リクエストの実行
            // connection.setRequestMethod("POST");
            // connection.setRequestProperty("Content-Type", "text/plain");
            // OutputStream outputStream = connection.getOutputStream();
            // BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            // writer.write(params[1]);
            // writer.close();
            // connection.connect();

            // レスポンスコードの確認します。
            int responseCode = connection.getResponseCode();
            if(responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP responseCode: " + responseCode);
            }

            // 文字列化します。
            inputStream = connection.getInputStream();
            if(inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null) {
                connection.disconnect();
            }
        }
        return sb.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        // 本メソッドは UI スレッドで処理されるため、ビューを操作できます。
        Log.d(TAG, result);
        //textView.setText(result);
    }

    // ***For development use only (This code contains some security issues) ***
    public static void disableSSLCertificateChecking() throws Exception {
        System.out.println("[WARN] *** SSLCertificate Checking DISABLED ***");

        // ホスト名の検証を行わない
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String s, SSLSession ses) {
                System.out.println("[WARN] *** HostnameVerifier DISABLED *** ");
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
        // 証明書の検証を行わない
        KeyManager[] km = null;
        TrustManager[] tm = { new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            }
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            }
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        } };
        SSLContext sslcontext = SSLContext.getInstance("SSL");
        sslcontext.init(km, tm, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
    }
}
