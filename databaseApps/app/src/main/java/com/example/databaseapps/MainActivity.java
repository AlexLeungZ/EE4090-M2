package com.example.databaseapps;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

public class MainActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Connection().execute();
    }

    @SuppressLint("StaticFieldLeak")
    class Connection extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String result;
            String host = "http://192.168.1.193/html/";
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(host));
                HttpResponse response = client.execute(request);

                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuilder stringBuffer = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line);
                }
                reader.close();
                result = stringBuffer.toString();
            } catch (Exception e) {
                return "There exception: " + e.getMessage();
            }

            return result;

        }

        @SuppressLint("ResourceType")
        @Override
        protected void onPostExecute(String result) {
            //parsing json data here
            try {
                JSONObject jsonResult = new JSONObject(result);

                //int success = jsonResult.getInt("success");
                //if(success == 1){
                if (jsonResult.length() != 0) {
                    JSONArray data = jsonResult.getJSONArray("data");
                    TableLayout table = findViewById(R.id.maintable);
                    cleanTable(table);
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject info = data.getJSONObject(i);

                        /*(2) Store the data into variable separately*/
                        /*your code*/
                        String Id = info.getString("ID");
                        String dt = info.getString("rec_time");
                        String temp = info.getString("rec_temp");
                        String humi = info.getString("rec_humi");
                        String press = info.getString("rec_press");

                        /*(3) Create a new row, and put the varible into textview one by one*/
                        /*your code*/
                        TableRow row = new TableRow(getApplicationContext());

                        TextView textViewId = new TextView(getApplicationContext());
                        textViewId.setText(Id);
                        textViewId.setTextSize(12);
                        textViewId.setTextColor(Color.WHITE);
                        textViewId.setGravity(Gravity.CENTER);

                        TextView textViewDt = new TextView(getApplicationContext());
                        textViewDt.setText(dt);
                        textViewDt.setTextSize(12);
                        textViewDt.setTextColor(Color.WHITE);
                        textViewDt.setGravity(Gravity.CENTER);

                        TextView textViewTemp = new TextView(getApplicationContext());
                        textViewTemp.setText(temp);
                        textViewTemp.setTextSize(12);
                        textViewTemp.setTextColor(Color.WHITE);
                        textViewTemp.setGravity(Gravity.CENTER);

                        TextView textViewHumi = new TextView(getApplicationContext());
                        textViewHumi.setText(humi);
                        textViewHumi.setTextSize(12);
                        textViewHumi.setTextColor(Color.WHITE);
                        textViewHumi.setGravity(Gravity.CENTER);

                        TextView textViewPress = new TextView(getApplicationContext());
                        textViewPress.setText(press);
                        textViewPress.setTextSize(12);
                        textViewPress.setTextColor(Color.WHITE);
                        textViewPress.setGravity(Gravity.CENTER);

                        switch (i % 3) {
                            case 0:
                                row.setBackgroundColor(0xFF525280);
                                break;
                            case 1:
                                row.setBackgroundColor(0xFF824545);
                                break;
                            case 2:
                                row.setBackgroundColor(0xFF006269);
                                break;
                        }

                        row.addView(textViewId);
                        row.addView(textViewDt);
                        row.addView(textViewTemp);
                        row.addView(textViewHumi);
                        row.addView(textViewPress);
                        table.addView(row);

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "There is no data yet!", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void cleanTable(TableLayout table) {

            int childCount = table.getChildCount();

            // Remove all rows except the first one
            if (childCount > 1) {
                table.removeViews(1, childCount - 1);
            }
        }
    }

}