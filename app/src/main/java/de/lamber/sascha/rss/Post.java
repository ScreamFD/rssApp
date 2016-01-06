package de.lamber.sascha.rss;

/**
 * Created by Sascha on 06.01.2016.
 */
public class Post {

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
