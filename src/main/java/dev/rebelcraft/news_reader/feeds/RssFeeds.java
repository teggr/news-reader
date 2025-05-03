package dev.rebelcraft.news_reader.feeds;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndLink;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;

@Component
public class RssFeeds {

    private final SyndFeedRepository syndFeeds;

    public RssFeeds(SyndFeedRepository syndFeeds) {
        this.syndFeeds = syndFeeds;
    }

    public String add(String feedUrl)  {

        try {

            // TODO: this could be done offline, queued list of things to load

            SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(feedUrl)));

            List<SyndEntry> entries = feed.getEntries();

            List<SyndLink> links = feed.getLinks();

            System.out.println(feed.getTitle() + " " + entries.size() + " " + links.size());

            return syndFeeds.save( feed );

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }

    public List<FeedDetails> getAllFeedDetails() {
        return syndFeeds.findAll();
    }

}
