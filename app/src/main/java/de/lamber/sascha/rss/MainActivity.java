package de.lamber.sascha.rss;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements EventDispatcher.EventObserver{

    ListView articleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("RSS - Parse.com Blog Posts");

        articleList = (ListView) findViewById(R.id.articleList);

        startService(new Intent(this, RSSService.class));
    }

    @Override
    protected void onStart() {
        super.onStart();

        EventDispatcher.addObserver(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        EventDispatcher.deleteObserver(this);
    }

    @Override
    public void feedUpdated(ArrayList<Post> posts) {

        ArrayAdapter<Post> arrayAdapter = new ArrayAdapter<Post>(
                MainActivity.this, android.R.layout.simple_list_item_1, posts);

        articleList.setAdapter(arrayAdapter);

    }
}
