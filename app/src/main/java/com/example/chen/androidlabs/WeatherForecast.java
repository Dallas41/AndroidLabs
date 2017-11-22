package com.example.chen.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecast extends Activity {

    protected static final String ACTIVITY_NAME = "WeatherForecast";
    protected TextView currentT;
    protected TextView minT;
    protected TextView maxT;
    protected ImageView imageView;
    protected ProgressBar pBar;
//    private static final String URLString = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        imageView=(ImageView)findViewById(R.id.forecastImage);
        imageView.setVisibility(View.VISIBLE);
        currentT=(TextView)findViewById(R.id.currentTemperature);
        minT=(TextView)findViewById(R.id.minTemperature);
        maxT=(TextView)findViewById(R.id.maxTemperature);
        pBar= (ProgressBar)findViewById(R.id.progressBar);
        pBar.setVisibility(View.VISIBLE);

        ForecastQuery app = new ForecastQuery();
        app.execute();
    }

    class ForecastQuery extends AsyncTask<String, Integer, String> {
        protected String min;
        protected String max;
        protected String current;
        protected String iconName;
        protected Bitmap weatherPicture;

        @Override
        protected String doInBackground(String... args) {
            HttpURLConnection conn = null;
            try {
                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric");
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();

                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(conn.getInputStream(), null);
                parser.nextTag();

                while (parser.next() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                       continue;
                    }

                    if (parser.getName().equals("temperature")) {
                        current = parser.getAttributeValue(null, "value");
                        publishProgress(25);
                        min = parser.getAttributeValue(null, "min");
                        publishProgress(50);
                        max = parser.getAttributeValue(null, "max");
                        publishProgress(75);
                    }
                    if (parser.getName().equals("weather")) {
                        iconName = parser.getAttributeValue(null, "icon");
                    }
                }

                if(fileExistance(iconName + ".png")){
                    weatherPicture = readImage(iconName + ".png");
                    Log.i(ACTIVITY_NAME,"found image locally");
                }
                else{
                    weatherPicture = getImage(new URL("http://openweathermap.org/img/w/" + iconName + ".png"));
                    Log.i(ACTIVITY_NAME, "downloading image");
                }
                publishProgress(100);
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            return null;
        }

        public Bitmap getImage(URL url) {
            HttpURLConnection connection = null;
            weatherPicture = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    weatherPicture = BitmapFactory.decodeStream(connection.getInputStream());
                    FileOutputStream outputStream = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);
                    weatherPicture.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (Exception e) {
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return weatherPicture;
        }

        private Bitmap readImage(String image) {
            Bitmap bm = null;
            try {
                FileInputStream fis = openFileInput(image);
                bm = BitmapFactory.decodeStream(fis);
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bm;
        }

        private boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        @Override
        protected void onProgressUpdate(Integer ...value){
            pBar.setProgress(value[0]);
            pBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s){
            currentT.setText("Current Temperature: "+ current + "°C");
            minT.setText("Minimum Temperature: "+ min + "°C");
            maxT.setText("Maximum Temperature: "+ max + "°C");
            imageView.setImageBitmap(weatherPicture);
            pBar.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
}