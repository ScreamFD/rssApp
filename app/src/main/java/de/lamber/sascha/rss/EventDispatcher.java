package de.lamber.sascha.rss;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sascha on 06.01.2016.
 */
public class EventDispatcher {

    public interface EventObserver {

        void feedUpdated(ArrayList<Post> posts);

    }

    private static List<EventObserver> observers = new ArrayList<EventObserver>();

    public static void addObserver(EventObserver observer){
        observers.add(observer);
    }

    public static void deleteObserver(EventObserver observer){
        observers.remove(observer);
    }

    public static void dispatchEvent(ArrayList<Post> posts){
        for (EventObserver observer : observers){
            observer.feedUpdated(posts);
        }
    }
}
