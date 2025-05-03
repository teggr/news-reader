package dev.rebelcraft.news_reader.feeds;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndLink;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.SyndFeedOutput;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class SyndFeedRepository {

    private final File localStore;

    public SyndFeedRepository() {

        File workingDirectory = new File("").getAbsoluteFile();

        localStore = new File(workingDirectory, ".rss-feeds");

        localStore.mkdirs();

    }

    public String save(SyndFeed feed) {

        try {

            // TODO: save the feed to the file system

            // lookup the key
            String feedKey = FeedKey.fromFeed(feed);

            System.out.println("Save: " + feed.getUri() + " using key: " + feedKey);

            File feedDirectory = new File(localStore, feedKey);

            if (!feedDirectory.exists()) {

                feedDirectory.mkdirs();

            }

            File cachedFeed = new File(feedDirectory, "cached-feed.xml");
            if (!cachedFeed.exists()) {

                // Use try-with-resources to ensure the writer is closed.
                try (Writer writer = new FileWriter(cachedFeed)) {
                    SyndFeedOutput output = new SyndFeedOutput();

                    output.output(feed, writer);

                    System.out.println("RSS feed successfully written to: " + cachedFeed.getAbsolutePath());

                } catch (IOException e) {
                    throw new IOException("Error writing RSS feed to file: " + cachedFeed.getAbsolutePath(), e); //Wrap for more context
                } catch (FeedException e) {
                    throw new FeedException("Error serializing RSS feed: " + e.getMessage(), e);
                } finally {
                    //  No need to close the writer here; try-with-resources handles it.
                    // System.clearProperty("com.rometools.rome.xml.prettyPrint"); //cleanup
                }

            }

            return feedKey;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public List<FeedDetails> findAll() {

        File[] rssDirectories = localStore.listFiles();

        return Stream.of(rssDirectories)
                .map( SyndFeedRepository::readSynd )
                .filter(Objects::nonNull)
                .map( SyndFeedRepository::mapSyndToFeedDetails )
                .sorted(Comparator.comparing(FeedDetails::getPublishedDate).reversed())
                .collect(Collectors.toList());

    }

    private static FeedDetails mapSyndToFeedDetails(SyndFeed syndFeed) {
        return new FeedDetails() {

            @Override
            public String getId() {
                return syndFeed.getUri();
            }

            @Override
            public String getTitle() {
                return syndFeed.getTitle();
            }

            @Override
            public Date getPublishedDate() {
                return syndFeed.getPublishedDate();
            }

            @Override
            public FeedDetailsLinks getLinks() {
                Optional<SyndLink> feed = syndFeed.getLinks().stream().filter(l -> l.getRel().equals("self")).findFirst();
                Optional<SyndLink> site = syndFeed.getLinks().stream().filter(l -> l.getRel().equals("alternative")).findFirst();
                return new FeedDetailsLinks() {

                    @Override
                    public String getSiteHref() {
                        return site.map(SyndLink::getHref).orElse(syndFeed.getLink());
                    }

                    @Override
                    public String getFeedHref() {
                        return feed.map(SyndLink::getHref).orElse(null);
                    }
                };
            }

        };
    }

    private static SyndFeed readSynd(File rssDirectory) {

        try {

            File cachedFile = new File(rssDirectory, "cached-feed.xml");

            if (!cachedFile.exists()) {
                return null;
            }

            SyndFeedInput input = new SyndFeedInput();
            return input.build(cachedFile);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (FeedException e) {
            throw new RuntimeException(e);
        }

    }

}
