package dev.rebelcraft.news_reader.feeds;

import java.util.Date;

public interface FeedDetails {

    String getId();

    String getTitle();

    Date getPublishedDate();

    FeedDetailsLinks getLinks();
}
