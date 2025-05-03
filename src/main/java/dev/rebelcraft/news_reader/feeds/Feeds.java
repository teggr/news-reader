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
public class Feeds {

    private final FeedFileSystemRepository feedRepository;

    public Feeds(FeedFileSystemRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    public String add(String feedUrl)  {

        try {

            FeedId feedId = new FeedId(feedUrl);

            // TODO: this could be done offline, queued list of things to load

            SyndFeed syndFeed = new SyndFeedInput().build(new XmlReader(new URL(feedUrl)));

            List<SyndEntry> entries = syndFeed.getEntries();

            List<SyndLink> links = syndFeed.getLinks();

            System.out.println(syndFeed.getTitle() + " " + entries.size() + " " + links.size());

            Feed feed = new Feed(feedId, feedUrl, syndFeed);

            return feedRepository.save( feed );

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }

    public List<FeedDetails> getAllFeedDetails() {
        return feedRepository.findAll();
    }

}
