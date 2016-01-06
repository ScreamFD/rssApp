package de.lamber.sascha.rss;

import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Sascha on 06.01.2016.
 */
public class XMLProcessor extends AsyncTask<String, Void, String> {

    private String rssURL;
    private PostParserDelegate delegate;
    private ArrayList<Post> posts;
    private StringBuilder buffer;

    public XMLProcessor(String rssURL, PostParserDelegate delegate){

        this.rssURL = rssURL;
        this.delegate = delegate;
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
            InputStreamReader reader = new InputStreamReader(inputStream);

            while (reader.read() != -1){

                buffer.append(reader.read(new char[400]));
            }

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

        // TODO: XML parsing

        delegate.xmlFeedParsed(posts);
    }
}
