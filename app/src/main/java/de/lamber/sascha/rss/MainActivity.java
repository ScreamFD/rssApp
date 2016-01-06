package de.lamber.sascha.rss;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PostParserDelegate {

    ListView articleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        articleList = (ListView) findViewById(R.id.articleList);

        XMLProcessor processor = new XMLProcessor("", this);
        processor.execute();
    }

    @Override
    public void xmlFeedParsed(ArrayList<Post> posts) {

        // TODO: Fill list

    }
}
