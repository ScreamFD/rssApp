package de.lamber.sascha.rss;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Sascha on 06.01.2016.
 */
public class RSSService extends Service {

    private boolean running = false;
    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d("RSSService","Service created");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        running = true;
        final long getFeedDelayInMinutes = 60000 * 1;

        handler.postDelayed(runnable, getFeedDelayInMinutes);

        runnable = new Runnable() {
            @Override
            public void run() {
                XMLProcessor xmlProcessor = new XMLProcessor("http://blog.parse.com/feed/");
                xmlProcessor.execute();

                if (running){
                    handler.postDelayed(runnable, getFeedDelayInMinutes);
                }
                
            }
        };

        runnable.run();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d("RSSService","Service destroyed");
        running = false;
        super.onDestroy();
    }

    public class XMLProcessor extends AsyncTask<String, Void, String> {

        private String rssURL;
        private ArrayList<Post> posts;
        private StringBuilder buffer;

        public XMLProcessor(String rssURL){

            this.rssURL = rssURL;
            posts = new ArrayList<Post>();
        }

        @Override
        protected String doInBackground(String... params) {

            buffer = new StringBuilder();

            try {

                URL url = new URL(rssURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                // HTTP Status "OK" => 200
                // NOT FOUND => 404
                int httpResponse = httpURLConnection.getResponseCode();
                if (httpResponse != 200){
                    throw new Exception("HTTP Error-code: " + httpResponse);
                }

                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");

                char[] temp = new char[400];
                int chars = 0;

                while (chars != -1){

                    chars = reader.read(temp);

                    buffer.append(temp, 0, chars);
                }

                reader.close();

                return buffer.toString();

            }catch (Exception ex) {
                Log.e("XMLProcessor", ex.getMessage());
                Log.e("XMLProcessor", ex.getStackTrace().toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);

            parse();
        }

        protected void parse(){

            String rawXML = buffer.toString();
            Post aPost = null;
            boolean isProcessingItem = false;
            String innerValue = "";

            try {
                XmlPullParserFactory pullParserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = pullParserFactory.newPullParser();

                parser.setInput(new StringReader(rawXML));
                int event = parser.getEventType();

                while (event != XmlPullParser.END_DOCUMENT){

                    String tag = parser.getName();

                    switch (event){

                        case XmlPullParser.START_TAG:
                            if (tag.equals("item")){
                                Log.d("XMLProcessor", "Neuer Post!");
                                isProcessingItem = true;
                                aPost = new Post();
                            }
                            break;

                        case XmlPullParser.TEXT:
                            innerValue = parser.getText();
                            break;

                        case XmlPullParser.END_TAG:
                            if (isProcessingItem) {
                                if (tag.equals("item")) {
                                    posts.add(aPost);
                                    isProcessingItem = false;
                                } else if (tag.equals("title")) {
                                    aPost.setTitle(innerValue);
                                }
                            }
                            break;
                    }

                    event = parser.next();
                }


                EventDispatcher.dispatchEvent(posts);

            }catch (Exception ex){
                Log.e("XMLProcessor", ex.getMessage());
            }

        }
    }

}
