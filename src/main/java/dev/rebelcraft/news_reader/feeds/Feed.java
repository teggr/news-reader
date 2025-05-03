package dev.rebelcraft.news_reader.feeds;

import com.rometools.rome.feed.synd.SyndFeed;

public class Feed {

    private FeedId id;
    private String url;
    private SyndFeed syndFeed;

    public Feed(FeedId id, String url, SyndFeed syndFeed) {
        this.id = id;
        this.url = url;
        this.syndFeed = syndFeed;
    }

    public String getUrl() {
        return url;
    }

    public FeedId getId() {
        return id;
    }

    public SyndFeed getSyndFeed() {
        return syndFeed;
    }



}
