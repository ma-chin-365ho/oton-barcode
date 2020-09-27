package com.example.otonproject2;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import android.os.AsyncTask;
import java.net.HttpURLConnection;
import java.net.URL;

import java.net.URL;

import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // 非同期処理の実行
        MyAsyncTask task = new MyAsyncTask(this);
        task.execute("https://www.example.com");
        // task.execute("https://www.example.com", "hi"); // POST の場合
        Log.d(TAG, "created");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                 */
                /*
                Log.d("HTTP-GET", "start");
                try {
                    url = new URL("http://example.com");
                    //url = new URL("https://example.com");
                    Log.d("HTTP-GET", "start2");
                    connection = (HttpURLConnection) url.openConnection();
                    Log.d("HTTP-GET", "start3");
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    Log.d("HTTP-GET", "start4");

                    // use a string builder to bufferize the response body
                    // read from the input strea.
                    StringBuilder sb = new StringBuilder();
                    String line;
                    Log.d("HTTP-GET", "start5");
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append('\n');
                    }
                    Log.d("HTTP-GET", "start6");

                    // use the string builder directly,
                    // or convert it into a String
                    String body = sb.toString();
                    Log.d("HTTP-GET", "start7");

                    Log.d("HTTP-GET", body);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }

                 */
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}