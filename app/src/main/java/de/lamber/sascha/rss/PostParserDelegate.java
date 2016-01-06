package de.lamber.sascha.rss;

import java.util.ArrayList;

/**
 * Created by Sascha on 06.01.2016.
 */
public interface PostParserDelegate {

    void xmlFeedParsed(ArrayList<Post> posts);

}
